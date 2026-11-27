package com.letsread.controller;

import java.util.List;

import com.letsread.model.Libro;
import com.letsread.service.LibroService;

/**
 * Recibe los datos de la ventana (como texto), los convierte y
 * delega las reglas de negocio en LibroService.
 */
public class LibroController {

    private final LibroService servicio;

    public LibroController(LibroService servicio) {
        this.servicio = servicio;
    }

    public List<Libro> listar() {
        return servicio.listar();
    }

    public Libro buscarPorIsbn(String isbn) {
        return servicio.buscarPorIsbn(isbn);
    }

    public void registrar(String isbn, String titulo, String autor, String anio,
            String categoria, String editorial, String paginas, String precio,
            double iva, String stock, String formato) throws Exception {
        servicio.registrar(construirLibro(isbn, titulo, autor, anio, categoria,
                editorial, paginas, precio, iva, stock, formato));
    }

    public void actualizar(String isbnOriginal, String isbn, String titulo, String autor,
            String anio, String categoria, String editorial, String paginas, String precio,
            double iva, String stock, String formato) throws Exception {
        servicio.actualizar(isbnOriginal, construirLibro(isbn, titulo, autor, anio,
                categoria, editorial, paginas, precio, iva, stock, formato));
    }

    public void eliminar(String isbn) throws Exception {
        servicio.eliminar(isbn);
    }

    private Libro construirLibro(String isbn, String titulo, String autor, String anio,
            String categoria, String editorial, String paginas, String precio,
            double iva, String stock, String formato) throws Exception {
        int anioNum;
        int paginasNum;
        int stockNum;
        double precioNum;
        try {
            anioNum = Integer.parseInt(anio.trim());
            paginasNum = Integer.parseInt(paginas.trim());
            precioNum = Double.parseDouble(precio.trim());
            stockNum = Integer.parseInt(stock.trim());
        } catch (NumberFormatException ex) {
            throw new Exception("Año, Páginas, Precio y Stock deben ser valores numéricos válidos.");
        }
        return new Libro(isbn.trim(), titulo.trim(), autor.trim(), anioNum,
                categoria.trim(), editorial.trim(), paginasNum, precioNum,
                iva, stockNum, formato);
    }
}