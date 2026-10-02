package co.edu.uptc.negocio;


public final class LibroFactory {

    private LibroFactory() {
    }

    public static Libro crear(FormatoLibro formato, String isbn, String titulo, String autor,
                              int anioPublicacion, String categoria, String editorial,
                              int numeroPaginas, double precioBase, int cantidadDisponible) {
        switch (formato) {
            case FISICO:
                return new LibroFisico(isbn, titulo, autor, anioPublicacion, categoria,
                        editorial, numeroPaginas, precioBase, cantidadDisponible);
            case DIGITAL:
                return new LibroDigital(isbn, titulo, autor, anioPublicacion, categoria,
                        editorial, precioBase, cantidadDisponible);
            default:
                throw new IllegalArgumentException("Formato no soportado: " + formato);
        }
    }
}

