package co.edu.uptc.negocio;

import java.util.List;
import java.util.Optional;

public interface LibroRepositorio {

    List<Libro> obtenerTodos();

    Optional<Libro> buscarPorIsbn(String isbn);

    boolean existe(String isbn);

    void guardar(Libro libro);

    boolean eliminar(String isbn);
}
