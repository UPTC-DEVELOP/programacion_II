package co.uptc.edu.caso.estudio.controller;

import java.util.List;

import co.uptc.edu.caso.estudio.negocio.Libro;
import co.uptc.edu.caso.estudio.service.LibroService;

public class LibroController {

    private LibroService servicio;

    public LibroController(LibroService servicio) {
        this.servicio = servicio;
    }

    public boolean registrar(Libro libro) {
        return servicio.registrar(libro);
    }

    public List<Libro> listar() {
        return servicio.listar();
    }

    public Libro buscarPorIsbn(String isbn) {
        return servicio.buscarPorIsbn(isbn);
    }

    public boolean actualizar(Libro libro) {
        return servicio.actualizar(libro);
    }

    public boolean eliminar(String isbn) {
        return servicio.eliminar(isbn);
    }
}