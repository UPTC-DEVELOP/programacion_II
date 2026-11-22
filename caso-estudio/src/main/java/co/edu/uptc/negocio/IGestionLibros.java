package co.edu.uptc.negocio;

import java.util.List;
import co.edu.uptc.model.Libro;

public interface IGestionLibros {
    List<Libro> listarLibros();
    Libro buscarLibroPorIsbn(String isbn);
    String registrarLibro(Libro libro);
    String actualizarLibro(Libro libro);
    boolean eliminarLibro(String isbn);
    void ajustarStock(String isbn, int delta);
}
