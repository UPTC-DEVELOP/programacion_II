package co.edu.uptc.model;

import java.util.ArrayList;

// Un libro digital se entrega como archivo y no requiere envío físico.
public class LibroDigital extends Libro {
    public LibroDigital(String isbn, String titulo, ArrayList<String> autores, int anioPublicacion,
                        Categoria categoria, String editorial, int numeroPaginas, double precioBase,
                        int stock, double porcentajeDescuento, double impuestoIVA) {
        super(isbn, titulo, autores, anioPublicacion, categoria, editorial, numeroPaginas,
                precioBase, stock, porcentajeDescuento, impuestoIVA);
    }
    @Override public FormatoLibro getFormato() { return FormatoLibro.DIGITAL; }
    @Override public String getDescripcionEntrega() { return "Descarga digital después del pago"; }
}
