package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.model.Libro;

/** Contrato de almacenamiento de libros (equivalente a IClienteRepositorio). */
public interface ILibroRepositorio {

    void guardar(Libro libro);

    Libro buscar(String isbn);

    void eliminar(String isbn);

    List<Libro> listar();
}
