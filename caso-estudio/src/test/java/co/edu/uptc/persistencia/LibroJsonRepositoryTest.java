package co.edu.uptc.persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.edu.uptc.excepciones.LibroConVentasException;
import co.edu.uptc.excepciones.LibroExistenteException;
import co.edu.uptc.excepciones.LibroNoEncontradoException;
import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.excepciones.ValidacionException;
import co.edu.uptc.interfaces.IVentaRepository;
import co.edu.uptc.modelo.Venta;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.dto.RegistroAuditoriaDto;
import co.edu.uptc.modelo.enums.AccionAuditoria;
import co.edu.uptc.modelo.enums.Categoria;
import co.edu.uptc.modelo.enums.EstadoVenta;
import co.edu.uptc.modelo.enums.Formato;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.ValidadorDatos;

/**
 * Pruebas de integración del repositorio JSON de libros y del servicio CRUD.
 *
 * <p>Cada prueba usa un directorio temporal propio (TemporaryFolder), de modo
 * que nunca se altera data/libros.json ni data/auditoria.json del proyecto.</p>
 */
public class LibroJsonRepositoryTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private FakeVentaRepository ventas;
    private LibroJsonRepository repositorio;
    private AuditoriaRepository auditoria;
    private GestionLibro servicio;
    private Path rutaLibros;

    @Before
    public void setUp() {
        ventas = new FakeVentaRepository();
        rutaLibros = carpetaTemporal.getRoot().toPath().resolve("data").resolve("libros.json");
        Path rutaAuditoria = carpetaTemporal.getRoot().toPath().resolve("data").resolve("auditoria.json");

        repositorio = new LibroJsonRepository(rutaLibros.toString(), ventas);
        auditoria = new AuditoriaRepository(rutaAuditoria.toString());
        servicio = new GestionLibro(repositorio, new ValidadorDatos(), auditoria);
    }

    // ------------------------------ CREATE ------------------------------

    @Test
    public void testRegistrarYListarOrdenadoAlfabeticamente() throws Exception {
        servicio.guardarLibro(crearLibro("9780000000001", "Zeta", "Autor Z", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO));
        servicio.guardarLibro(crearLibro("9780000000002", "Alfa", "Autor A", 2001, 20.0, 1,
                Categoria.TECNOLOGIA, Formato.DIGITAL));

        List<LibroDto> libros = servicio.listar(null);

        assertEquals(2, libros.size());
        assertEquals("Alfa", libros.get(0).getTitulo());
        assertEquals("Zeta", libros.get(1).getTitulo());
    }

    @Test
    public void testRegistrarIsbnDuplicadoLanzaExcepcion() throws Exception {
        servicio.guardarLibro(crearLibro("9780000000003", "Primero", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO));

        LibroDto duplicado = crearLibro("9780000000003", "Segundo", "Autor", 2001, 20.0, 2,
                Categoria.HISTORIA, Formato.FISICO);

        assertThrows(LibroExistenteException.class, () -> servicio.guardarLibro(duplicado));
        assertEquals(1, servicio.listar(null).size());
    }

    // ------------------------------ UPDATE ------------------------------

    @Test
    public void testActualizarLibroExistente() throws Exception {
        servicio.guardarLibro(crearLibro("9780000000004", "Original", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO));

        LibroDto actualizado = crearLibro("9780000000004", "Actualizado", "Autor", 2000, 25.5, 9,
                Categoria.ROMANCE, Formato.DIGITAL);
        servicio.actualizarLibro(actualizado);

        LibroDto guardado = servicio.consultarPorIsbn("9780000000004");
        assertNotNull(guardado);
        assertEquals("Actualizado", guardado.getTitulo());
        assertEquals(25.5, guardado.getPrecioVenta(), 0.0001);
        assertEquals(9, guardado.getStock());
        assertEquals(Categoria.ROMANCE, guardado.getCategoria());
    }

    @Test
    public void testActualizarLibroInexistenteLanzaExcepcion() {
        LibroDto inexistente = crearLibro("9780000000005", "Fantasma", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO);

        assertThrows(LibroNoEncontradoException.class, () -> servicio.actualizarLibro(inexistente));
    }

    // ------------------------------ DELETE ------------------------------

    @Test
    public void testEliminarLibroSinVentas() throws Exception {
        servicio.guardarLibro(crearLibro("9780000000006", "Borrable", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO));

        servicio.eliminarLibro("9780000000006");

        assertNull(servicio.consultarPorIsbn("9780000000006"));
        assertFalse(repositorio.existePorIsbn("9780000000006"));
    }

    @Test
    public void testEliminarLibroConVentasLanzaExcepcion() throws Exception {
        String isbn = "9780000000007";
        servicio.guardarLibro(crearLibro(isbn, "Con ventas", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO));

        ventas.agregar(new Venta(1, 1, "Cliente", LocalDate.now(),
                List.of(isbn), 11.9, EstadoVenta.COMPLETADA));

        assertThrows(LibroConVentasException.class, () -> servicio.eliminarLibro(isbn));
        assertNotNull(servicio.consultarPorIsbn(isbn));
    }

    // ------------------------------ READ / FILTROS ------------------------------

    @Test
    public void testFiltrosPorTituloAutorCategoriaYPrecio() throws Exception {
        servicio.guardarLibro(crearLibro("9780000000010", "Clean Code", "Robert Martin",
                2008, 45.99, 5, Categoria.TECNOLOGIA, Formato.FISICO));
        servicio.guardarLibro(crearLibro("9780000000011", "1984", "George Orwell",
                1949, 20.0, 5, Categoria.CIENCIA_FICCION, Formato.DIGITAL));
        servicio.guardarLibro(crearLibro("9780000000012", "Effective Java", "Joshua Bloch",
                2018, 55.0, 5, Categoria.TECNOLOGIA, Formato.FISICO));

        assertEquals(1, servicio.listar(new FiltroLibroDto("clean", null, null)).size());
        assertEquals(1, servicio.listar(new FiltroLibroDto(null, "orwell", null)).size());
        assertEquals(2, servicio.listar(new FiltroLibroDto(null, null, Categoria.TECNOLOGIA)).size());
        assertEquals(2, servicio.listar(new FiltroLibroDto(null, null, null, 40.0, 60.0)).size());
        assertEquals(1, servicio.listar(new FiltroLibroDto(null, null, null, null, 30.0)).size());
    }

    @Test
    public void testPersistenciaEntreInstancias() throws Exception {
        servicio.guardarLibro(crearLibro("9780000000013", "Persistente", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO));

        LibroJsonRepository segundoRepositorio =
                new LibroJsonRepository(rutaLibros.toString(), ventas);

        assertNotNull(segundoRepositorio.consultarPorIsbn("9780000000013"));
        assertEquals("Persistente", segundoRepositorio.consultarPorIsbn("9780000000013").getTitulo());
    }

    @Test
    public void testArchivoSeCreaAutomaticamente() {
        assertTrue(Files.exists(rutaLibros));
    }

    // ------------------------------ VALIDACIONES ------------------------------

    @Test
    public void testValidacionAnioMenorOIgualA1400() {
        LibroDto libro = crearLibro("9780000000020", "Antiguo", "Autor", 1400, 10.0, 1,
                Categoria.HISTORIA, Formato.FISICO);
        assertThrows(ValidacionException.class, () -> servicio.guardarLibro(libro));
    }

    @Test
    public void testValidacionAnioFuturo() {
        int anioFuturo = LocalDate.now().getYear() + 1;
        LibroDto libro = crearLibro("9780000000021", "Futuro", "Autor", anioFuturo, 10.0, 1,
                Categoria.CIENCIA_FICCION, Formato.FISICO);
        assertThrows(ValidacionException.class, () -> servicio.guardarLibro(libro));
    }

    @Test
    public void testValidacionPrecioNoPositivo() {
        LibroDto libro = crearLibro("9780000000022", "Gratis", "Autor", 2000, 0.0, 1,
                Categoria.OTROS, Formato.FISICO);
        assertThrows(ValidacionException.class, () -> servicio.guardarLibro(libro));
    }

    @Test
    public void testValidacionStockNegativo() {
        LibroDto libro = crearLibro("9780000000023", "Stock negativo", "Autor", 2000, 10.0, -1,
                Categoria.OTROS, Formato.FISICO);
        assertThrows(ValidacionException.class, () -> servicio.guardarLibro(libro));
    }

    @Test
    public void testValidacionTituloMayorA100Caracteres() {
        LibroDto libro = crearLibro("9780000000024", "A".repeat(101), "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO);
        assertThrows(ValidacionException.class, () -> servicio.guardarLibro(libro));
    }

    @Test
    public void testValidacionIsbnDistintoDe13Digitos() {
        LibroDto libro = crearLibro("123456789012", "ISBN corto", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO);
        assertThrows(ValidacionException.class, () -> servicio.guardarLibro(libro));
    }

    @Test
    public void testValidacionTituloVacio() {
        LibroDto libro = crearLibro("9780000000025", "   ", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO);
        assertThrows(ReglaNegocioException.class, () -> servicio.guardarLibro(libro));
    }

    // ------------------------------ AUDITORÍA ------------------------------

    @Test
    public void testAuditoriaRegistraCreateUpdateDelete() throws Exception {
        String isbn = "9780000000030";
        servicio.guardarLibro(crearLibro(isbn, "Auditado", "Autor", 2000, 10.0, 1,
                Categoria.OTROS, Formato.FISICO));
        servicio.actualizarLibro(crearLibro(isbn, "Auditado v2", "Autor", 2000, 12.0, 2,
                Categoria.OTROS, Formato.FISICO));
        servicio.eliminarLibro(isbn);

        List<RegistroAuditoriaDto> registros = auditoria.listar();
        assertEquals(3, registros.size());
        assertEquals(AccionAuditoria.CREATE, registros.get(0).getAccion());
        assertEquals(AccionAuditoria.UPDATE, registros.get(1).getAccion());
        assertEquals(AccionAuditoria.DELETE, registros.get(2).getAccion());
        assertEquals("admin", registros.get(0).getUsuario());
        assertEquals(isbn, registros.get(2).getIsbn());
    }

    // ------------------------------ Auxiliares ------------------------------

    private LibroDto crearLibro(String isbn, String titulo, String autor, int anio,
                                double precio, int stock, Categoria categoria, Formato formato) {
        LibroDto dto = new LibroDto();
        dto.setIsbn(isbn);
        dto.setTitulo(titulo);
        dto.setAutores(List.of(autor));
        dto.setAnioPublicacion(anio);
        dto.setEditorial("Editorial de prueba");
        dto.setNumPaginas(100);
        dto.setPrecioVenta(precio);
        dto.setStock(stock);
        dto.setCategoria(categoria);
        dto.setFormato(formato);
        return dto;
    }

    /**
     * Repositorio de ventas falso para las pruebas. Permite simular que un
     * libro tiene ventas asociadas sin depender de LocalVenta.
     */
    private static final class FakeVentaRepository implements IVentaRepository {

        private final List<Venta> ventas = new ArrayList<>();

        void agregar(Venta venta) {
            ventas.add(venta);
        }

        @Override
        public List<Venta> listarTodas() {
            return new ArrayList<>(ventas);
        }

        @Override
        public List<Venta> listarRecientes(int cantidad) {
            return new ArrayList<>(ventas);
        }

        @Override
        public int contarVentas() {
            return ventas.size();
        }

        @Override
        public double sumarIngresos() {
            return ventas.stream().mapToDouble(Venta::getTotal).sum();
        }
    }
}
