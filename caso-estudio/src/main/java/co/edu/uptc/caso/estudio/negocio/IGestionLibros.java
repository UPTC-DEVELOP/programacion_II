package co.edu.uptc.caso.estudio.negocio;

import java.util.List;

import co.edu.uptc.caso.estudio.model.Libro;

public interface IGestionLibros {
    List<Libro> listarLibros();
    Libro buscarLibroPorIsbn(String isbn);
    String registrarLibro(Libro libro);
    String actualizarLibro(Libro libro);
    boolean eliminarLibro(String isbn);
    void ajustarStock(String isbn, int delta);
}
