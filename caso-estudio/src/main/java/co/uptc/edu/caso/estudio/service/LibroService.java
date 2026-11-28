package co.uptc.edu.caso.estudio.service;

import java.util.List;

import co.uptc.edu.caso.estudio.negocio.Compra;
import co.uptc.edu.caso.estudio.negocio.DetalleCompra;
import co.uptc.edu.caso.estudio.negocio.Libro;
import co.uptc.edu.caso.estudio.repository.LibroRepository;

public class LibroService {

    private LibroRepository repositorio;

    public LibroService(LibroRepository repositorio) {
        this.repositorio = repositorio;
    }

    public boolean registrar(Libro libro) {

        if (libro == null) {
            return false;
        }

        if (libro.getIsbn() == null
                || libro.getIsbn().trim().isEmpty()
                || libro.getIsbn().length() != 13) {
            return false;
        }

        if (libro.getTitulo() == null
                || libro.getTitulo().trim().isEmpty()) {
            return false;
        }

        if (libro.getAutores() == null
                || libro.getAutores().trim().isEmpty()) {
            return false;
        }

        if (libro.getAnioPublicacion() <= 0) {
            return false;
        }

        if (libro.getCategoria() == null
                || libro.getCategoria().trim().isEmpty()) {
            return false;
        }

        if (libro.getEditorial() == null
                || libro.getEditorial().trim().isEmpty()) {
            return false;
        }

        if (libro.getNumeroPaginas() <= 0) {
            return false;
        }

        if (libro.getPrecioVenta() <= 0) {
            return false;
        }

        if (libro.getCantidadInventario() < 0) {
            return false;
        }

        if (!formatoValido(libro.getFormato())) {
            return false;
        }

        if (repositorio.buscarPorIsbn(libro.getIsbn()) != null) {
            return false;
        }

        repositorio.guardar(libro);

        return true;
    }

    public List<Libro> listar() {
        return repositorio.listar();
    }

    public Libro buscarPorIsbn(String isbn) {

        if (isbn == null || isbn.trim().isEmpty()) {
            return null;
        }

        return repositorio.buscarPorIsbn(isbn);
    }

    public boolean actualizar(Libro libro) {

        if (libro == null) {
            return false;
        }

        if (libro.getIsbn() == null
                || libro.getIsbn().trim().isEmpty()
                || libro.getIsbn().length() != 13) {
            return false;
        }

        if (repositorio.buscarPorIsbn(libro.getIsbn()) == null) {
            return false;
        }

        if (libro.getTitulo() == null
                || libro.getTitulo().trim().isEmpty()) {
            return false;
        }

        if (libro.getAutores() == null
                || libro.getAutores().trim().isEmpty()) {
            return false;
        }

        if (libro.getAnioPublicacion() <= 0) {
            return false;
        }

        if (libro.getCategoria() == null
                || libro.getCategoria().trim().isEmpty()) {
            return false;
        }

        if (libro.getEditorial() == null
                || libro.getEditorial().trim().isEmpty()) {
            return false;
        }

        if (libro.getNumeroPaginas() <= 0) {
            return false;
        }

        if (libro.getPrecioVenta() <= 0) {
            return false;
        }

        if (libro.getCantidadInventario() < 0) {
            return false;
        }

        if (!formatoValido(libro.getFormato())) {
            return false;
        }

        repositorio.actualizar(libro);

        return true;
    }

    public boolean eliminar(String isbn) {

        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        isbn = isbn.trim();

        Libro libro = repositorio.buscarPorIsbn(isbn);

        if (libro == null) {
            return false;
        }

        if (tieneVentasAsociadas(isbn)) {
            return false;
        }

        repositorio.eliminar(isbn);

        return true;
    }
    
    private boolean tieneVentasAsociadas(String isbn) {

        List<Compra> compras = Compra.obtenerTodas();

        for (Compra compra : compras) {

            if (compra == null || compra.getDetalles() == null) {
                continue;
            }

            for (DetalleCompra detalle : compra.getDetalles()) {

                if (detalle == null || detalle.getLibro() == null) {
                    continue;
                }

                if (isbn.equals(detalle.getLibro().getIsbn())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean formatoValido(String formato) {

        if (formato == null) {
            return false;
        }

        return formato.equalsIgnoreCase("Fisico")
                || formato.equalsIgnoreCase("Digital");
    }
}