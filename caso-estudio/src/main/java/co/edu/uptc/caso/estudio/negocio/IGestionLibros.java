package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Libro;
import java.util.List;

// Contrato de negocio para la administración del catálogo e inventario de libros
public interface IGestionLibros {

    List<Libro> listarLibros();

    Libro buscarLibroPorIsbn(String isbn);

    String registrarLibro(Libro libro);

    String actualizarLibro(Libro libro);

    boolean eliminarLibro(String isbn);

    void ajustarStock(String isbn, int delta);

    void sincronizarStockRespaldo(String isbn);
}