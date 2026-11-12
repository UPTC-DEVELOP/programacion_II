package co.edu.uptc.model;

import java.util.ArrayList;

// La fábrica centraliza la creación de libros y evita repetir condicionales en otras capas.
public final class LibroFactory {
    private LibroFactory() { }

    public static Libro crear(FormatoLibro formato, String isbn, String titulo, ArrayList<String> autores,
                              int anio, Categoria categoria, String editorial, int paginas,
                              double precio, int stock, double descuento, double iva) {
        if (formato == FormatoLibro.FISICO) {
            return new LibroFisico(isbn, titulo, autores, anio, categoria, editorial, paginas,
                    precio, stock, descuento, iva);
        }
        return new LibroDigital(isbn, titulo, autores, anio, categoria, editorial, paginas,
                precio, stock, descuento, iva);
    }
}
