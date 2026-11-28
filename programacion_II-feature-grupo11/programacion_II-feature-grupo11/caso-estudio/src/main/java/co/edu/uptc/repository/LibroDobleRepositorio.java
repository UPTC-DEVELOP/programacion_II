package co.edu.uptc.repository;

import java.util.List;

import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.model.Libro;

public class LibroDobleRepositorio implements ILibroRepositorio {

    private final ILibroRepositorio principal; // MySQL: de aqui se lee
    private final ILibroRepositorio respaldo;  // JSON: copia de lo escrito

    public LibroDobleRepositorio(ILibroRepositorio principal, ILibroRepositorio respaldo) {
        this.principal = principal;
        this.respaldo = respaldo;
        sincronizar();
    }

    // Copia al JSON los libros que estan en MySQL pero aun no en el JSON
    private void sincronizar() {
        for (Libro libro : principal.listar()) {
            if (respaldo.buscar(libro.getIsbn()) == null) {
                respaldo.guardar(libro);
            }
        }
    }

    @Override
    public void guardar(Libro libro) {
        principal.guardar(libro);
        respaldo.guardar(libro);
    }

    @Override
    public Libro buscar(String isbn) {
        return principal.buscar(isbn);
    }

    @Override
    public void actualizar(Libro libro) {
        principal.actualizar(libro);
        respaldo.actualizar(libro);
    }

    @Override
    public void eliminar(String isbn) {
        principal.eliminar(isbn);
        respaldo.eliminar(isbn);
    }

    @Override
    public List<Libro> listar() {
        return principal.listar();
    }
}