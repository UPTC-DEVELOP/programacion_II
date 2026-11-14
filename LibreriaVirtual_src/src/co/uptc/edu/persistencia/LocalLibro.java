package co.uptc.edu.persistencia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.uptc.edu.interfaces.IGestionLibro;
import co.uptc.edu.model.Libro;

/** Inventario en memoria: los libros se pierden al cerrar la aplicación. */
public class LocalLibro implements IGestionLibro {

    private final Map<String, Libro> libros = new LinkedHashMap<>();

    @Override
    public void guardar(Libro libro) {
        libros.put(libro.getIsbn(), libro);
    }

    @Override
    public void actualizarStock(String isbn, int stock) {
        Libro libro = libros.get(isbn);
        if (libro != null) {
            libro.setStock(stock);
        }
    }

    @Override
    public List<Libro> listar() {
        return new ArrayList<>(libros.values());
    }
}
