package co.uptc.edu.modelo;

import java.util.List;
import java.util.Optional;

import co.uptc.edu.negocio.Libro;



public interface RepositorioLibro {

   
    void guardar(Libro libro);

    Optional<Libro> buscarPorIsbn(String isbn);

    boolean existe(String isbn);

  
    List<Libro> listar();

    void eliminar(String isbn);

    void eliminarTodos();
}
