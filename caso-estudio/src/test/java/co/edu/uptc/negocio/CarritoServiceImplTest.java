package co.edu.uptc.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.excepciones.StockInsuficienteException;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.ItemCarritoDto;
import co.edu.uptc.modelo.dto.TotalesCarritoDto;
import co.edu.uptc.modelo.enums.Categoria;
import co.edu.uptc.modelo.enums.Formato;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.persistencia.CarritoJsonRepository;

/**
 * Pruebas de integración entre CarritoServiceImpl, el repositorio JSON de
 * carritos y las abstracciones del catálogo y de clientes.
 */
public class CarritoServiceImplTest {

    private static final String ISBN_FISICO = "9780000000101";
    private static final String ISBN_DIGITAL = "9780000000102";
    private static final int CLIENTE_PREMIUM = 100;
    private static final int CLIENTE_REGULAR = 200;

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private CarritoJsonRepository carritoRepository;
    private CarritoServiceImpl servicio;

    @Before
    public void setUp() {
        Path ruta = carpetaTemporal.getRoot().toPath()
                .resolve("data").resolve("carritos.json");
        carritoRepository = new CarritoJsonRepository(ruta.toString());

        FakeLibroRepositorio libros = new FakeLibroRepositorio();
        libros.guardar(crearLibro(ISBN_FISICO, "Libro fisico", 100.0, 5, Formato.FISICO));
        libros.guardar(crearLibro(ISBN_DIGITAL, "Libro digital", 100.0, 5, Formato.DIGITAL));

        FakeClienteService clientes = new FakeClienteService();
        clientes.registrar(CLIENTE_PREMIUM, TipoCliente.PREMIUM);
        clientes.registrar(CLIENTE_REGULAR, TipoCliente.REGULAR);

        servicio = new CarritoServiceImpl(carritoRepository, libros, clientes);
    }

    @Test
    public void testAgregarLibroFisicoAplicaIvaDelCincoPorCiento() throws Exception {
        ItemCarritoDto item = servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_FISICO, 1);

        assertEquals(100.0, item.getSubtotal(), 0.001);
        assertEquals(5.0, item.getIva(), 0.001);
    }

    @Test
    public void testAgregarLibroDigitalAplicaIvaDelDiecinuevePorCiento() throws Exception {
        ItemCarritoDto item = servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_DIGITAL, 1);

        assertEquals(100.0, item.getSubtotal(), 0.001);
        assertEquals(19.0, item.getIva(), 0.001);
    }

    @Test
    public void testAcumulaCantidadSinDuplicarLinea() throws Exception {
        servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_DIGITAL, 1);
        ItemCarritoDto item = servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_DIGITAL, 2);

        assertEquals(3, item.getCantidad());
        assertEquals(1, servicio.obtenerItems(CLIENTE_PREMIUM).size());
    }

    @Test
    public void testValidaStockDisponible() throws Exception {
        servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_DIGITAL, 4);

        assertThrows(StockInsuficienteException.class,
                () -> servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_DIGITAL, 2));
    }

    @Test
    public void testCalculaTotalesConDescuentoPremium() throws Exception {
        servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_DIGITAL, 2);

        TotalesCarritoDto totales = servicio.calcularTotales(CLIENTE_PREMIUM);

        assertEquals(200.0, totales.getSubtotalGeneral(), 0.001);
        assertEquals(38.0, totales.getTotalImpuestos(), 0.001);
        assertEquals(20.0, totales.getDescuento(), 0.001);
        assertEquals(218.0, totales.getTotalFinal(), 0.001);
    }

    @Test
    public void testPersisteElCarritoTrasCadaOperacion() throws Exception {
        servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_DIGITAL, 2);

        CarritoCompras persistido = carritoRepository.obtenerCarritoPorCliente(CLIENTE_PREMIUM);
        assertNotNull(persistido);
        assertEquals(TipoCliente.PREMIUM, persistido.getTipoCliente());
        assertEquals(2, persistido.obtenerItems().get(0).getCantidad());

        servicio.eliminarItem(CLIENTE_PREMIUM, ISBN_DIGITAL);

        CarritoCompras despuesDeEliminar =
                carritoRepository.obtenerCarritoPorCliente(CLIENTE_PREMIUM);
        assertNotNull(despuesDeEliminar);
        assertTrue(despuesDeEliminar.estaVacio());
    }

    @Test
    public void testRechazaCantidadCero() {
        assertThrows(ReglaNegocioException.class,
                () -> servicio.agregarLibro(CLIENTE_PREMIUM, ISBN_DIGITAL, 0));
    }

    private Libro crearLibro(String isbn, String titulo, double precio, int stock, Formato formato) {
        return new Libro(isbn, titulo, new ArrayList<>(List.of("Autor")), 2020,
                "Editorial", 100, precio, stock, formato, Categoria.OTROS);
    }

    private static final class FakeLibroRepositorio implements ILibroRepositorio {

        private final List<Libro> libros = new ArrayList<>();

        @Override
        public void guardar(Libro libro) {
            libros.add(libro);
        }

        @Override
        public void actualizar(Libro libro) {
            for (int i = 0; i < libros.size(); i++) {
                if (libros.get(i).getIsbn().equals(libro.getIsbn())) {
                    libros.set(i, libro);
                    return;
                }
            }
        }

        @Override
        public void eliminar(String isbn) {
            libros.removeIf(libro -> libro.getIsbn().equals(isbn));
        }

        @Override
        public boolean existePorIsbn(String isbn) {
            return consultarPorIsbn(isbn) != null;
        }

        @Override
        public Libro consultarPorIsbn(String isbn) {
            return libros.stream()
                    .filter(libro -> libro.getIsbn().equals(isbn))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Libro> listarTodos() {
            return new ArrayList<>(libros);
        }

        @Override
        public List<Libro> listarConFiltro(FiltroLibroDto filtro) {
            return listarTodos();
        }
    }

    private static final class FakeClienteService implements IGestionCliente {

        private final List<ClienteDto> clientes = new ArrayList<>();

        void registrar(int idCliente, TipoCliente tipoCliente) {
            ClienteDto dto = new ClienteDto();
            dto.setIdCliente(idCliente);
            dto.setTipoCliente(tipoCliente);
            clientes.add(dto);
        }

        @Override
        public boolean registrarCliente(ClienteDto clienteDto) {
            return false;
        }

        @Override
        public boolean actualizarCliente(ClienteDto clienteDto) {
            return false;
        }

        @Override
        public boolean eliminarCliente(int idCliente) {
            return false;
        }

        @Override
        public ClienteDto buscarPorId(int idCliente) {
            return clientes.stream()
                    .filter(cliente -> cliente.getIdCliente() == idCliente)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public ClienteDto buscarPorIdentificacion(String identificacion) {
            return null;
        }

        @Override
        public List<ClienteDto> listarClientes() {
            return new ArrayList<>(clientes);
        }

        @Override
        public List<ClienteDto> buscarClientes(String texto) {
            return new ArrayList<>();
        }

        @Override
        public boolean iniciarSesion(String correo, String contrasena) {
            return false;
        }
    }

}
