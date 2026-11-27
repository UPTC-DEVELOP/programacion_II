package com.letsread.service;

import java.util.List;

import com.letsread.model.Libro;
import com.letsread.persistence.ArchivoTxtDAO;
import com.letsread.persistence.LibroRepositorio;

/**
 * Reglas de negocio del catalogo de libros.
 * No sabe donde se guardan los datos: depende de LibroRepositorio.
 */
public class LibroService {

    private final LibroRepositorio repositorio;

    public LibroService(LibroRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Libro> listar() {
        return repositorio.listar();
    }

    public Libro buscarPorIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        return repositorio.buscarPorIsbn(isbn.trim());
    }

    public void registrar(Libro nuevoLibro) throws Exception {
        if (buscarPorIsbn(nuevoLibro.getIsbn()) != null) {
            throw new Exception("Ya existe un libro registrado con el ISBN: " + nuevoLibro.getIsbn());
        }
        repositorio.guardar(nuevoLibro);
        ArchivoTxtDAO.registrarOperacion("Admin registró nuevo libro: " + nuevoLibro.getTitulo()
                + " (ISBN: " + nuevoLibro.getIsbn() + ")");
    }

    public void actualizar(String isbnOriginal, Libro datosActualizados) throws Exception {
        Libro existente = buscarPorIsbn(isbnOriginal);
        if (existente == null) {
            throw new Exception("No se encontró el libro a actualizar.");
        }

        boolean cambioIsbn = !isbnOriginal.trim().equalsIgnoreCase(datosActualizados.getIsbn().trim());
        if (cambioIsbn && buscarPorIsbn(datosActualizados.getIsbn()) != null) {
            throw new Exception("El nuevo ISBN ya está asignado a otro libro.");
        }

        // Las ventas no se editan desde el formulario: se conservan
        datosActualizados.setTieneVentas(existente.isTieneVentas());

        if (cambioIsbn) {
            // El ISBN es la clave: se guarda el nuevo y se elimina el anterior
            repositorio.guardar(datosActualizados);
            repositorio.eliminar(existente.getIsbn());
        } else {
            repositorio.actualizar(datosActualizados);
        }

        ArchivoTxtDAO.registrarOperacion("Admin actualizó el libro: " + datosActualizados.getTitulo()
                + " (ISBN: " + datosActualizados.getIsbn() + ")");
    }

    public void eliminar(String isbn) throws Exception {
        Libro libro = buscarPorIsbn(isbn);
        if (libro == null) {
            throw new Exception("Libro no encontrado en el catálogo.");
        }
        if (libro.isTieneVentas()) {
            throw new Exception("No se puede eliminar \"" + libro.getTitulo()
                    + "\" porque ya tiene ventas asociadas.");
        }
        repositorio.eliminar(libro.getIsbn());
        ArchivoTxtDAO.registrarOperacion("Admin eliminó el libro: " + libro.getTitulo()
                + " (ISBN: " + isbn + ")");
    }
}