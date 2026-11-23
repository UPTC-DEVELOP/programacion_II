package co.edu.uptc.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.enums.Categoria;
import co.edu.uptc.modelo.enums.Formato;
import co.edu.uptc.persistencia.LibroJsonRepository;

/**
 * Verifica que GestionLibro replique las operaciones confirmadas en la
 * persistencia principal hacia el repositorio JSON de respaldo.
 */
public class GestionLibroDualTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private LibroJsonRepository principal;
    private LibroJsonRepository espejo;
    private GestionLibro servicio;

    @Before
    public void setUp() {
        Path base = carpetaTemporal.getRoot().toPath().resolve("data");
        principal = new LibroJsonRepository(base.resolve("principal.json").toString(), null);
        espejo = new LibroJsonRepository(base.resolve("libros.json").toString(), null);
        servicio = new GestionLibro(principal, new ValidadorDatos(), espejo, null);
    }

    @Test
    public void testGuardarReplicaEnJson() throws Exception {
        servicio.guardarLibro(crearLibro("9780000001001", "Original"));

        assertNotNull(principal.consultarPorIsbn("9780000001001"));
        assertNotNull(espejo.consultarPorIsbn("9780000001001"));
        assertEquals("Original", espejo.consultarPorIsbn("9780000001001").getTitulo());
    }

    @Test
    public void testActualizarReplicaEnJson() throws Exception {
        servicio.guardarLibro(crearLibro("9780000001002", "Original"));
        servicio.actualizarLibro(crearLibro("9780000001002", "Actualizado"));

        assertEquals("Actualizado", principal.consultarPorIsbn("9780000001002").getTitulo());
        assertEquals("Actualizado", espejo.consultarPorIsbn("9780000001002").getTitulo());
    }

    @Test
    public void testEliminarReplicaEnJson() throws Exception {
        servicio.guardarLibro(crearLibro("9780000001003", "Borrable"));
        servicio.eliminarLibro("9780000001003");

        assertNull(principal.consultarPorIsbn("9780000001003"));
        assertNull(espejo.consultarPorIsbn("9780000001003"));
        assertFalse(espejo.existePorIsbn("9780000001003"));
    }

    @Test
    public void testGuardarEnJsonActualizaSiElIsbnYaExiste() {
        Libro primero = aEntidad(crearLibro("9780000001004", "Primero"));
        Libro segundo = aEntidad(crearLibro("9780000001004", "Segundo"));

        espejo.guardar(primero);
        espejo.guardar(segundo);

        assertEquals(1, espejo.listarTodos().size());
        assertEquals("Segundo", espejo.consultarPorIsbn("9780000001004").getTitulo());
    }

    @Test
    public void testRecuperaJsonCorruptoConRespaldo() throws Exception {
        Path base = carpetaTemporal.getRoot().toPath().resolve("corrupto");
        Files.createDirectories(base);
        Path archivo = base.resolve("libros.json");
        Files.writeString(archivo, "{ esto no es json", StandardCharsets.UTF_8);

        LibroJsonRepository recuperado = new LibroJsonRepository(archivo.toString(), null);

        assertTrue(recuperado.listarTodos().isEmpty());
        assertTrue(contieneRespaldo(base, "libros.json.corrupt-"));
    }

    @Test
    public void testSincronizarReemplazaYRespalda() throws Exception {
        espejo.guardar(aEntidad(crearLibro("9780000001005", "Anterior")));

        espejo.sincronizar(List.of(
                aEntidad(crearLibro("9780000001006", "Nuevo uno")),
                aEntidad(crearLibro("9780000001007", "Nuevo dos"))));

        assertEquals(2, espejo.listarTodos().size());
        assertNull(espejo.consultarPorIsbn("9780000001005"));
        assertTrue(contieneRespaldo(carpetaTemporal.getRoot().toPath().resolve("data"),
                "libros.json.bak-"));
    }

    private boolean contieneRespaldo(Path carpeta, String prefijo) throws Exception {
        try (var archivos = Files.list(carpeta)) {
            return archivos.anyMatch(p -> p.getFileName().toString().startsWith(prefijo));
        }
    }

    private Libro aEntidad(LibroDto dto) {
        Libro libro = new Libro();
        libro.setIsbn(dto.getIsbn());
        libro.setTitulo(dto.getTitulo());
        libro.setAutores(dto.getAutores());
        libro.setAnioPublicacion(dto.getAnioPublicacion());
        libro.setEditorial(dto.getEditorial());
        libro.setNumPaginas(dto.getNumPaginas());
        libro.setPrecioVenta(dto.getPrecioVenta());
        libro.setStock(dto.getStock());
        libro.setCategoria(dto.getCategoria());
        libro.setFormato(dto.getFormato());
        return libro;
    }

    private LibroDto crearLibro(String isbn, String titulo) {
        LibroDto dto = new LibroDto();
        dto.setIsbn(isbn);
        dto.setTitulo(titulo);
        dto.setAutores(List.of("Autor"));
        dto.setAnioPublicacion(2024);
        dto.setEditorial("Editorial");
        dto.setNumPaginas(100);
        dto.setPrecioVenta(10000.0);
        dto.setStock(5);
        dto.setCategoria(Categoria.TECNOLOGIA);
        dto.setFormato(Formato.FISICO);
        return dto;
    }
}
