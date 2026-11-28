package co.uptc.edu.caso.estudio.repository;

import java.util.List;
import co.uptc.edu.caso.estudio.negocio.Libro;

public interface LibroRepository {

    void guardar(Libro libro);

    List<Libro> listar();

    Libro buscarPorIsbn(String isbn);

    void actualizar(Libro libro);

    void eliminar(String isbn);
}