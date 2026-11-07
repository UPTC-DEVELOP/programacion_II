package main.java.co.edu.uptc.persistence;
import java.util.List;
import main.java.co.edu.uptc.model.Libro;

public interface IRepositorioLibros {
    boolean guardarLibro(Libro libro);
    boolean actualizarLibro(Libro libro);
    boolean eliminarLibro(String isbn);
    Libro buscarPorIsbn(String isbn);
    List<Libro> obtenerTodos();
}