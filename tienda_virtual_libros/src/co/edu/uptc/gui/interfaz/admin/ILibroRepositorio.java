package co.edu.uptc.gui.interfaz.admin;


import java.util.List;

import co.edu.uptc.negocio.modelo.Libro;


/**
 * INTERFAZ ILibroRepositorio  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de almacenamiento de libros. La capa de negocio solo conoce esta
 * interfaz, nunca sabe si los datos están en memoria (HOY), en JSON o en una
 * base de datos con JDBC (MÁS ADELANTE). Cuando se pida persistencia se crea una
 * nueva clase que implemente esta misma interfaz y NO se toca el negocio (DIP + OCP).
 */
public interface ILibroRepositorio {

    List<Libro> listarTodos();

    /** @return el libro o null si no existe. */
    Libro buscarPorIsbn(String isbn);

    void guardar(Libro libro);        // inserta uno nuevo

    void actualizar(Libro libro);     // reemplaza el que tenga el mismo ISBN

    void eliminar(String isbn);       // remoción física del registro de la colección
}