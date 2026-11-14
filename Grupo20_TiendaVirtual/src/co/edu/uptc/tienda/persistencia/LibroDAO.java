
package persistencia;

import java.util.List;
import negocio.Libro;

public interface LibroDAO {

    // Registrar un libro
    boolean registrar(Libro libro);

    // Buscar un libro por ISBN
    Libro buscarPorIsbn(String isbn);

    // Listar todos los libros
    List<Libro> listar();

    // Actualizar un libro
    boolean actualizar(Libro libro);

    // Eliminar un libro
    boolean eliminar(String isbn);
}

