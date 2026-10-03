package co.edu.uptc.persistence;

import co.edu.uptc.model.Libro;
import java.util.List;

public interface IRepositorioLibros {
    boolean guardarLibro(Libro libro);
    boolean actualizarLibro(Libro libro);
    boolean eliminarLibro(String isbn);
    Libro buscarPorIsbn(String isbn);
    List<Libro> obtenerTodos();
}