package co.edu.uptc.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.excepciones.ValidacionException;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IReviewRepository;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ClienteRegular;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * Pruebas unitarias de GestionResena con repositorios falsos en memoria
 * (no requieren PostgreSQL).
 */
public class GestionResenaTest {

    private static final String ISBN = "9780000000101";
    private static final int ANA = 1;
    private static final int LUIS = 2;
    private static final int ADMIN = 3;

    private FakeReviewRepository reviews;
    private GestionResena servicio;

    @Before
    public void setUp() {
        reviews = new FakeReviewRepository();
        ILibroRepositorio libros = (ILibroRepositorio) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[] {ILibroRepositorio.class},
                (p, m, a) -> m.getName().equals("existePorIsbn") && ISBN.equals(a[0]));
        IClienteRepositorio clientes = (IClienteRepositorio) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[] {IClienteRepositorio.class},
                (p, m, a) -> {
                    if (!m.getName().equals("buscarPorId")) {
                        return null;
                    }
                    int id = (Integer) a[0];
                    if (id == ANA || id == LUIS) {
                        return cliente(id, Rol.CLIENTE);
                    }
                    return id == ADMIN ? cliente(id, Rol.ADMIN) : null;
                });
        servicio = new GestionResena(reviews, libros, clientes);
    }

    private static Cliente cliente(int id, Rol rol) {
        return new ClienteRegular("N", "", "A", "", "CC", "" + id, id + "@x.com", "3000000000",
                "dir", id, TipoCliente.REGULAR, "h", new Timestamp(0), 0, rol);
    }

    @Test
    public void creaResenaValida() throws Exception {
        servicio.crear(ANA, ISBN, 5, "  Excelente  ");

        assertEquals(1, servicio.listarPorLibro(ISBN).size());
        assertEquals("Excelente", servicio.listarPorLibro(ISBN).get(0).comentario());
    }

    @Test
    public void rechazaCalificacionFueraDeRango() {
        assertThrows(ValidacionException.class, () -> servicio.crear(ANA, ISBN, 0, "x"));
        assertThrows(ValidacionException.class, () -> servicio.crear(ANA, ISBN, 6, "x"));
    }

    @Test
    public void rechazaComentarioVacioOMuyLargo() {
        assertThrows(ValidacionException.class, () -> servicio.crear(ANA, ISBN, 4, "   "));
        assertThrows(ValidacionException.class,
                () -> servicio.crear(ANA, ISBN, 4, "a".repeat(501)));
    }

    @Test
    public void rechazaLibroOClienteInexistente() {
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(ANA, "no-existe", 4, "x"));
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(99, ISBN, 4, "x"));
    }

    @Test
    public void rechazaSegundaResenaDelMismoCliente() throws Exception {
        servicio.crear(ANA, ISBN, 4, "uno");

        assertThrows(ReglaNegocioException.class, () -> servicio.crear(ANA, ISBN, 5, "dos"));
    }

    @Test
    public void calculaPromedio() throws Exception {
        assertEquals(0.0, servicio.promedioCalificacion(ISBN), 0.001);
        servicio.crear(ANA, ISBN, 5, "a");
        servicio.crear(LUIS, ISBN, 4, "b");

        assertEquals(4.5, servicio.promedioCalificacion(ISBN), 0.001);
    }

    @Test
    public void soloElDuenoActualiza() throws Exception {
        servicio.crear(ANA, ISBN, 3, "ok");
        int id = servicio.listarPorLibro(ISBN).get(0).id();

        assertThrows(ReglaNegocioException.class, () -> servicio.actualizar(LUIS, ISBN, id, 5, "x"));
        assertThrows(ReglaNegocioException.class, () -> servicio.actualizar(ADMIN, ISBN, id, 5, "x"));
        servicio.actualizar(ANA, ISBN, id, 5, "mejor");
        assertEquals(5, servicio.listarPorLibro(ISBN).get(0).calificacion());
    }

    @Test
    public void duenoOAdminEliminan_otroClienteNo() throws Exception {
        servicio.crear(ANA, ISBN, 3, "ok");
        int id = servicio.listarPorLibro(ISBN).get(0).id();

        assertThrows(ReglaNegocioException.class, () -> servicio.eliminar(LUIS, ISBN, id));
        servicio.eliminar(ADMIN, ISBN, id);
        assertTrue(servicio.listarPorLibro(ISBN).isEmpty());

        servicio.crear(ANA, ISBN, 3, "otra");
        servicio.eliminar(ANA, ISBN, servicio.listarPorLibro(ISBN).get(0).id());
        assertTrue(servicio.listarPorLibro(ISBN).isEmpty());
    }

    @Test
    public void devuelveNombresDeAutores() {
        assertEquals("Ana Pérez", servicio.nombresAutores(ISBN).get(ANA));
    }

    @Test
    public void devuelveFechasDeResenasYVacioSiFalla() {
        assertEquals(java.time.LocalDateTime.of(2026, 11, 11, 10, 30),
                servicio.fechasResenas(ISBN).get(1));
        reviews.fallaNombres = true;

        assertTrue(servicio.fechasResenas(ISBN).isEmpty());
    }

    @Test
    public void sinNombresSiElRepositorioFalla() {
        reviews.fallaNombres = true;

        assertTrue(servicio.nombresAutores(ISBN).isEmpty());
    }

    /** Repositorio de reseñas en memoria. */
    private static class FakeReviewRepository implements IReviewRepository {
        private final List<Review> datos = new ArrayList<>();
        private int siguiente = 1;
        private boolean fallaNombres;

        @Override
        public java.util.Map<Integer, java.time.LocalDateTime> fechasResenas(String isbn) {
            if (fallaNombres) {
                throw new co.edu.uptc.excepciones.PersistenciaException("falla simulada", null);
            }
            return java.util.Map.of(1, java.time.LocalDateTime.of(2026, 11, 11, 10, 30));
        }

        @Override
        public java.util.Map<Integer, String> nombresClientes(String isbn) {
            if (fallaNombres) {
                throw new co.edu.uptc.excepciones.PersistenciaException("falla simulada", null);
            }
            return java.util.Map.of(ANA, "Ana Pérez");
        }

        @Override
        public void guardar(Review r) {
            datos.add(0, new Review(siguiente++, r.isbn(), r.idCliente(),
                    r.calificacion(), r.comentario()));
        }

        @Override
        public List<Review> listarPorLibro(String isbn) {
            List<Review> out = new ArrayList<>();
            for (Review r : datos) {
                if (r.isbn().equals(isbn)) {
                    out.add(r);
                }
            }
            return out;
        }

        @Override
        public void actualizar(Review r) {
            for (int i = 0; i < datos.size(); i++) {
                if (datos.get(i).id() == r.id()) {
                    datos.set(i, r);
                }
            }
        }

        @Override
        public void eliminar(int id, int idCliente) {
            datos.removeIf(r -> r.id() == id);
        }
    }
}
