package co.edu.uptc.negocio;

public class LibroDigital extends Libro {

    public static final double IVA_DIGITAL = 5.0;

    public LibroDigital(String isbn, String titulo, String autor, int anioPublicacion,
                        String categoria, String editorial, double precioBase,
                        int cantidadDisponible, double porcentajeDescuento) {
        super(isbn, titulo, autor, anioPublicacion, categoria, editorial,
              precioBase, cantidadDisponible, porcentajeDescuento, IVA_DIGITAL);
    }

    public FormatoLibro getFormato() {
        return FormatoLibro.DIGITAL;
    }

    public int getNumeroPaginas() {
        return 0; // No aplica a libros digitales
    }
}
