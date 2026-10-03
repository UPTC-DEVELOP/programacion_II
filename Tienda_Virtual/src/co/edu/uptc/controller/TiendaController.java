package co.edu.uptc.controller;

import co.edu.uptc.model.Libro;
import co.edu.uptc.persistence.IRepositorioLibros;
import co.edu.uptc.persistence.RepositorioLibrosTexto;
import java.util.List;
import co.edu.uptc.model.CarritoCompras;
import co.edu.uptc.persistence.IRepositorioCarrito;
import co.edu.uptc.persistence.RepositorioCarritoMemoria;

public class TiendaController {
    private IRepositorioLibros repositorioLibros;
    private IRepositorioCarrito repositorioCarrito;

    public TiendaController() {
        this.repositorioLibros = new RepositorioLibrosTexto();
        this.repositorioCarrito = new RepositorioCarritoMemoria();
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
    

    // carrito
    public boolean agregarAlCarrito(String isbn, int cantidad) {
        Libro libro = repositorioLibros.buscarPorIsbn(isbn);
        if (libro == null) {
            return false;
        }
        return repositorioCarrito.agregarItem(libro, cantidad);
    }

    public boolean actualizarCantidadCarrito(String isbn, int nuevaCantidad) {
        return repositorioCarrito.actualizarCantidad(isbn, nuevaCantidad);
    }

    public boolean eliminarDelCarrito(String isbn) {
        return repositorioCarrito.eliminarItem(isbn);
    }

    public void vaciarCarrito() {
        repositorioCarrito.vaciar();
    }

    public CarritoCompras obtenerCarrito() {
        return repositorioCarrito.obtenerCarrito();
    }
}