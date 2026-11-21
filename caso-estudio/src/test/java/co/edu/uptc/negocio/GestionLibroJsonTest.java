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
 * Verifica que GestionLibro persista el CRUD de libros únicamente en JSON,
 * sin repositorio JDBC ni espejo.
 */
public class GestionLibroJsonTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private LibroJsonRepository repositorio;
    private GestionLibro servicio;

    @Before
    public void setUp() {
        Path base = carpetaTemporal.getRoot().toPath().resolve("data");
        repositorio = new LibroJsonRepository(base.resolve("libros.json").toString(), null);
        servicio = new GestionLibro(repositorio, new ValidadorDatos(), null);
    }

    @Test
    public void testGuardarPersisteEnJson() throws Exception {
        servicio.guardarLibro(crearLibro("9780000001001", "Original"));

        assertNotNull(repositorio.consultarPorIsbn("9780000001001"));
        assertEquals("Original", repositorio.consultarPorIsbn("9780000001001").getTitulo());
    }

    @Test
    public void testActualizarPersisteEnJson() throws Exception {
        servicio.guardarLibro(crearLibro("9780000001002", "Original"));
        servicio.actualizarLibro(crearLibro("9780000001002", "Actualizado"));

        assertEquals("Actualizado", repositorio.consultarPorIsbn("9780000001002").getTitulo());
    }

    @Test
    public void testEliminarPersisteEnJson() throws Exception {
        servicio.guardarLibro(crearLibro("9780000001003", "Borrable"));
        servicio.eliminarLibro("9780000001003");

        assertNull(repositorio.consultarPorIsbn("9780000001003"));
        assertFalse(repositorio.existePorIsbn("9780000001003"));
    }

    @Test
    public void testGuardarEnJsonActualizaSiElIsbnYaExiste() {
        Libro primero = aEntidad(crearLibro("9780000001004", "Primero"));
        Libro segundo = aEntidad(crearLibro("9780000001004", "Segundo"));

        repositorio.guardar(primero);
        repositorio.guardar(segundo);

        assertEquals(1, repositorio.listarTodos().size());
        assertEquals("Segundo", repositorio.consultarPorIsbn("9780000001004").getTitulo());
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
        repositorio.guardar(aEntidad(crearLibro("9780000001005", "Anterior")));

        repositorio.sincronizar(List.of(
                aEntidad(crearLibro("9780000001006", "Nuevo uno")),
                aEntidad(crearLibro("9780000001007", "Nuevo dos"))));

        assertEquals(2, repositorio.listarTodos().size());
        assertNull(repositorio.consultarPorIsbn("9780000001005"));
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
