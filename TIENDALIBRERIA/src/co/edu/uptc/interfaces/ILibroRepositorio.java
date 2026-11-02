package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.model.Libro;


public interface ILibroRepositorio {

    void guardar(Libro libro);

    Libro buscar(String isbn);

    void eliminar(String isbn);
   
    void actualizar(Libro libro);

    List<Libro> listar();
}
