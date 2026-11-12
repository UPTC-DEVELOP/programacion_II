package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.modelo.Libro;

public class ControlLibro {

    private List<Libro> libros;

    public ControlLibro() {
        libros = new ArrayList<>();
    }

    // Registrar libro
    public boolean registrarLibro(Libro libro) {

        if (libro == null) {
            return false;
        }

        if (buscarLibro(libro.getIsbn()) != null) {
            return false;
        }

        libros.add(libro);
        return true;
    }

    // Buscar libro por ISBN
    public Libro buscarLibro(String isbn) {

        for (Libro libro : libros) {

            if (libro.getIsbn().equalsIgnoreCase(isbn)) {
                return libro;
            }
        }

        return null;
    }

    // Actualizar libro
    public boolean actualizarLibro(String isbn, String titulo,
            String autor, int anioPublicacion, String editorial,
            int numPaginas, double precio, double ivaPorcentaje,
            int stock, String formato) {

        Libro libro = buscarLibro(isbn);

        if (libro == null) {
            return false;
        }

        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnioPublicacion(anioPublicacion);
        libro.setEditorial(editorial);
        libro.setNumPaginas(numPaginas);
        libro.setPrecio(precio);
        libro.setIvaPorcentaje(ivaPorcentaje);
        libro.setStock(stock);
        libro.setFormato(formato);

        return true;
    }

    // Eliminar libro
    public boolean eliminarLibro(String isbn) {

        Libro libro = buscarLibro(isbn);

        if (libro == null) {
            return false;
        }

        libros.remove(libro);
        return true;
    }

    // Listar libros
    public List<Libro> listarLibros() {
        return libros;
    }
}
