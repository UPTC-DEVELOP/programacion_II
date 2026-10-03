package co.edu.uptc.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.modelo.Libro;


/**
 * CLASE LibroRepositorioMemoria  (paquete: persistencia)  implements ILibroRepositorio
 * ---------------------------------------------------------------------------
 * IMPLEMENTACIÓN TEMPORAL: guarda el catálogo en una lista en MEMORIA RAM.
 *
 * ¿Por qué existe? La guía de la unidad 2 dice que por ahora NO va persistencia
 * (solo frontend y backend). Pero el negocio necesita "algo" donde guardar los
 * libros mientras la aplicación está abierta. Al cerrar la app los datos se pierden.
 *
 * ¿Por qué no se pierde nada de la arquitectura? Porque GestionLibro solo
 * conoce la INTERFAZ ILibroRepositorio (principio DIP). Cuando el profesor pida
 * persistencia, se crea LibroRepositorioJson (o Jdbc) que implemente la misma
 * interfaz y se cambia UNA línea en AppLibros. Nada más se toca (OCP + LSP).
 */
public class LibroRepositorioMemoria implements ILibroRepositorio {

    private final List<Libro> libros = new ArrayList<>();

    @Override
    public List<Libro> listarTodos() {
        return new ArrayList<>(libros);          // copia: nadie altera la lista interna
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        for (Libro l : libros) {
            if (l.getIsbn().equals(isbn)) return l;
        }
        return null;
    }

    @Override
    public void guardar(Libro libro) {
        libros.add(libro);
    }

    @Override
    public void actualizar(Libro libro) {
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getIsbn().equals(libro.getIsbn())) {
                libros.set(i, libro);
                return;
            }
        }
    }

    @Override
    public void eliminar(String isbn) {
        libros.removeIf(l -> l.getIsbn().equals(isbn));
    }
}
