package co.edu.uptc.controller;

import co.edu.uptc.model.Libro;
import co.edu.uptc.persistence.IRepositorioLibros;
import co.edu.uptc.persistence.RepositorioLibrosTexto;
import java.util.List;

public class TiendaController {
    private IRepositorioLibros repositorioLibros;

    public TiendaController() {
        this.repositorioLibros = new RepositorioLibrosTexto();
    }

    public boolean registrarLibro(Libro libro) {
        return repositorioLibros.guardarLibro(libro);
    }

    public boolean actualizarLibro(Libro libro) {
        return repositorioLibros.actualizarLibro(libro);
    }

    public boolean eliminarLibro(String isbn) {
        return repositorioLibros.eliminarLibro(isbn);
    }

    public List<Libro> listarLibros() {
        return repositorioLibros.obtenerTodos();
    }
}