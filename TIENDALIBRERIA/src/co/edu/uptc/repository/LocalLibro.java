package co.edu.uptc.repository;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.model.Libro;

public class LocalLibro implements ILibroRepositorio {

    private final List<Libro> libros = new ArrayList<>();

    @Override
    public void guardar(Libro libro) {
        libros.add(libro);
    }

    @Override
    public Libro buscar(String isbn) {
        return libros.stream()
                .filter(l -> l.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void eliminar(String isbn) {
        libros.removeIf(l -> l.getIsbn().equals(isbn));
    }

    @Override
    public List<Libro> listar() {
        return libros;
    }
}
