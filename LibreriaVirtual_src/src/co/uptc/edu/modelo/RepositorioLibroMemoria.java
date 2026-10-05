package co.uptc.edu.modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import co.uptc.edu.negocio.Libro;


public class RepositorioLibroMemoria implements RepositorioLibro {

    private final Map<String, Libro> libros = new LinkedHashMap<>();

    @Override
    public void guardar(Libro libro) {
        libros.put(libro.getIsbn(), libro);
    }

    @Override
    public Optional<Libro> buscarPorIsbn(String isbn) {
        return Optional.ofNullable(libros.get(isbn));
    }

    @Override
    public boolean existe(String isbn) {
        return libros.containsKey(isbn);
    }

    @Override
    public List<Libro> listar() {
        return new ArrayList<>(libros.values());
    }

    @Override
    public void eliminar(String isbn) {
        libros.remove(isbn);
    }

    @Override
    public void eliminarTodos() {
        libros.clear();
    }
}
