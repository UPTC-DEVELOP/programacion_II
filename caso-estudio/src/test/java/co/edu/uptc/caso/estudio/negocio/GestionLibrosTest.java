package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.persistence.IRepositorioLibros;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

public class GestionLibrosTest extends TestCase {

    private GestionLibrosImpl gestionLibros;
    private RepoLibrosMemoria repoMySQL;
    private RepoLibrosMemoria repoJson;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        repoMySQL = new RepoLibrosMemoria();
        repoJson = new RepoLibrosMemoria();
        gestionLibros = new GestionLibrosImpl(repoMySQL, repoJson, null);
    }

    public void testRegistrarYBuscarLibro() {
        Libro libro = new Libro("978-1", "Clean Code", "Robert C. Martin", 2008,
                "Tecnologia", "Prentice Hall", 464, 120000, 10, "Físico", 19);

        String error = gestionLibros.registrarLibro(libro);
        assertNull("El registro valido del libro no debe fallar", error);

        Libro encontrado = gestionLibros.buscarLibroPorIsbn("978-1");
        assertNotNull("Debe encontrar el libro por ISBN", encontrado);
        assertEquals("Clean Code", encontrado.getTitulo());
    }

    public void testRegistrarLibroPrecioNegativo() {
        Libro invalido = new Libro("978-2", "Libro Malo", "Autor", 2020,
                "Ficcion", "Editorial", 200, -5000, 5, "Digital", 5);

        String error = gestionLibros.registrarLibro(invalido);
        assertEquals("El precio de venta debe ser mayor a $0.", error);
    }

    public void testAjustarStock() {
        Libro libro = new Libro("978-3", "Design Patterns", "GoF", 1994,
                "Tecnologia", "Addison-Wesley", 395, 150000, 8, "Físico", 19);
        gestionLibros.registrarLibro(libro);

        gestionLibros.ajustarStock("978-3", -3);
        assertEquals(5, gestionLibros.buscarLibroPorIsbn("978-3").getStock());

        // No debe bajar de 0
        gestionLibros.ajustarStock("978-3", -20);
        assertEquals(0, gestionLibros.buscarLibroPorIsbn("978-3").getStock());
    }

    public void testEliminarLibro() {
        Libro libro = new Libro("978-4", "Refactoring", "Martin Fowler", 2018,
                "Tecnologia", "Addison-Wesley", 448, 140000, 4, "Físico", 19);
        gestionLibros.registrarLibro(libro);

        assertTrue(gestionLibros.eliminarLibro("978-4"));
        assertNull(gestionLibros.buscarLibroPorIsbn("978-4"));
    }

    // --- Repositorio simulado en memoria ---
    private static class RepoLibrosMemoria implements IRepositorioLibros {
        private final Map<String, Libro> libros = new HashMap<>();

        @Override
        public boolean guardarLibro(Libro libro) {
            libros.put(libro.getIsbn(), libro);
            return true;
        }

        @Override
        public boolean actualizarLibro(Libro libro) {
            if (!libros.containsKey(libro.getIsbn())) return false;
            libros.put(libro.getIsbn(), libro);
            return true;
        }

        @Override
        public boolean eliminarLibro(String isbn) {
            return libros.remove(isbn) != null;
        }

        @Override
        public Libro buscarPorIsbn(String isbn) {
            return libros.get(isbn);
        }

        @Override
        public List<Libro> obtenerTodos() {
            return new ArrayList<>(libros.values());
        }
    }
}