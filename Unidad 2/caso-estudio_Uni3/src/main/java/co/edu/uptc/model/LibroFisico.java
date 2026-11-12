package co.edu.uptc.model;

import java.util.ArrayList;

// Un libro físico se entrega como un ejemplar impreso y necesita dirección de envío.
public class LibroFisico extends Libro {
    public LibroFisico(String isbn, String titulo, ArrayList<String> autores, int anioPublicacion,
                       Categoria categoria, String editorial, int numeroPaginas, double precioBase,
                       int stock, double porcentajeDescuento, double impuestoIVA) {
        super(isbn, titulo, autores, anioPublicacion, categoria, editorial, numeroPaginas,
                precioBase, stock, porcentajeDescuento, impuestoIVA);
    }
    @Override public FormatoLibro getFormato() { return FormatoLibro.FISICO; }
    @Override public String getDescripcionEntrega() { return "Entrega física a la dirección registrada"; }
}
