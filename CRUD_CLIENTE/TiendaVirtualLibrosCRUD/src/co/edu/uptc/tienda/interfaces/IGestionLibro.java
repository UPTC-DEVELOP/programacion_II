package co.edu.uptc.tienda.interfaces;

import java.util.List;
import co.edu.uptc.tienda.modelo.Libro;

public interface IGestionLibro {

    void agregar(Libro libro);

    List<Libro> listar();

    Libro buscar(String titulo);

    boolean actualizar(Libro libro);

    boolean eliminar(String titulo);
}