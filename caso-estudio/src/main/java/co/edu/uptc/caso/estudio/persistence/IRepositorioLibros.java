package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Libro;
import java.util.List;

// Contrato de persistencia para operaciones CRUD y consulta de libros
public interface IRepositorioLibros {

    boolean guardarLibro(Libro libro);

    boolean actualizarLibro(Libro libro);

    boolean eliminarLibro(String isbn);

    Libro buscarPorIsbn(String isbn);

    List<Libro> obtenerTodos();
}