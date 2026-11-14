package co.edu.uptc.tienda.persistencia;

import java.util.List;

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.modelo.Libro;

public interface LibroDAO {

    boolean registrar(Libro libro) throws LibroExcepcion;

    Libro buscarPorIsbn(String isbn) throws LibroExcepcion;

    List<Libro> listar() throws LibroExcepcion;

    boolean actualizar(Libro libro) throws LibroExcepcion;

    boolean eliminar(String isbn) throws LibroExcepcion;
}
