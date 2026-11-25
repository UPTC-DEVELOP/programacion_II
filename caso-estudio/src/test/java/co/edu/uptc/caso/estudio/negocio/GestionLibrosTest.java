package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.persistence.IRepositorioLibros;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

// Pruebas unitarias para el catalogo de libros sin tocar MySQL ni JSON
public class GestionLibrosTest extends TestCase {

    private GestionLibrosImpl gestionLibros;
    private RepoLibrosMemoria repoMySQL;
    private RepoLibrosMemoria repoJson;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Preparamos repositorios limpios en memoria antes de cada prueba
        repoMySQL = new RepoLibrosMemoria();
        repoJson = new RepoLibrosMemoria();
        gestionLibros = new GestionLibrosImpl(repoMySQL, repoJson, null);
    }

    // Verifica que un libro con datos validos se guarde y se pueda consultar por ISBN
    public void testRegistrarYBuscarLibro() {
        Libro libro = new Libro("978-1", "Clean Code", "Robert C. Martin", 2008,
                "Tecnologia", "Prentice Hall", 464, 120000, 10, "Físico", 19);

        String error = gestionLibros.registrarLibro(libro);
        assertNull("El registro valido del libro no debe fallar", error);

        Libro encontrado = gestionLibros.buscarLibroPorIsbn("978-1");
        assertNotNull("Debe encontrar el libro por ISBN", encontrado);
        assertEquals("Clean Code", encontrado.getTitulo());
    }

    // Comprueba que el sistema rechace libros con precio negativo o en cero
    public void testRegistrarLibroPrecioNegativo() {
        Libro invalido = new Libro("978-2", "Libro Malo", "Autor", 2020,
                "Ficcion", "Editorial", 200, -5000, 5, "Digital", 5);

        String error = gestionLibros.registrarLibro(invalido);
        assertEquals("El precio de venta debe ser mayor a $0.", error);
    }

    // Valida que al actualizar el precio y stock de un libro existente los cambios se guarden
    public void testActualizarLibroExitoso() {
        Libro libro = new Libro("978-5", "Effective Java", "Joshua Bloch", 2018,
                "Tecnologia", "Addison-Wesley", 416, 130000, 6, "Físico", 19);
        gestionLibros.registrarLibro(libro);

        libro.setPrecioVenta(145000);
        libro.setStock(12);
        String error = gestionLibros.actualizarLibro(libro);

        assertNull("La actualizacion valida no debe retornar error", error);
        Libro actualizado = gestionLibros.buscarLibroPorIsbn("978-5");
        assertEquals(145000.0, actualizado.getPrecioVenta(), 0.01);
        assertEquals(12, actualizado.getStock());
    }

    // Revisa que el stock sume/reste bien y nunca quede por debajo de cero
    public void testAjustarStock() {
        Libro libro = new Libro("978-3", "Design Patterns", "GoF", 1994,
                "Tecnologia", "Addison-Wesley", 395, 150000, 8, "Físico", 19);
        gestionLibros.registrarLibro(libro);

        gestionLibros.ajustarStock("978-3", -3);
        assertEquals(5, gestionLibros.buscarLibroPorIsbn("978-3").getStock());

        // No debe bajar de 0 aunque se resten mas unidades de las disponibles
        gestionLibros.ajustarStock("978-3", -20);
        assertEquals(0, gestionLibros.buscarLibroPorIsbn("978-3").getStock());
    }

    // Verifica que al listar el catalogo devuelva todos los libros registrados
    public void testListarCatalogoLibros() {
        gestionLibros.registrarLibro(new Libro("978-6", "Libro A", "Autor A", 2021, "Ficcion", "Ed", 150, 50000, 3, "Digital", 5));
        gestionLibros.registrarLibro(new Libro("978-7", "Libro B", "Autor B", 2022, "Historia", "Ed", 220, 70000, 5, "Físico", 19));

        List<Libro> catalogo = gestionLibros.obtenerTodosLosLibros();
        assertEquals("El catalogo debe tener exactamente 2 libros", 2, catalogo.size());
    }

    // Comprueba que un libro eliminado deje de aparecer en las busquedas
    public void testEliminarLibro() {
        Libro libro = new Libro("978-4", "Refactoring", "Martin Fowler", 2018,
                "Tecnologia", "Addison-Wesley", 448, 140000, 4, "Físico", 19);
        gestionLibros.registrarLibro(libro);

        assertTrue(gestionLibros.eliminarLibro("978-4"));
        assertNull(gestionLibros.buscarLibroPorIsbn("978-4"));
    }

    // Simula la base de datos de libros en memoria usando un HashMap
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