package co.edu.uptc.persistence;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Libro;
import java.util.List;
import java.util.Optional;

public interface LibroRepository {
    List<Libro> listar() throws PersistenciaException;
    Optional<Libro> buscarPorIsbn(String isbn) throws PersistenciaException;
    void guardar(Libro libro) throws PersistenciaException;
    void eliminar(String isbn) throws PersistenciaException;
    boolean existe(String isbn) throws PersistenciaException;
}
