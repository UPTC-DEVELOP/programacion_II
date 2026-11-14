package co.edu.uptc.persistence;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Libro;

public interface LibroRepository {
    List<Libro> listar() throws PersistenciaException;
    Optional<Libro> buscarPorIsbn(String isbn) throws PersistenciaException;
    void guardar(Libro libro) throws PersistenciaException;
    void eliminar(String isbn) throws PersistenciaException;
    boolean existe(String isbn) throws PersistenciaException;
}
