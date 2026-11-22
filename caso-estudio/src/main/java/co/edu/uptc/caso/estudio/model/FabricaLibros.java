package co.edu.uptc.caso.estudio.model;

public class FabricaLibros {

    private FabricaLibros() {}

    public static Libro crear(String isbn, String titulo, String autor, int anio, String categoria,
                              String editorial, int paginas, double precioVenta, int stock,
                              String formato, double porcentajeIva) {
        if ("Digital".equalsIgnoreCase(formato)) {
            return new LibroDigital(isbn, titulo, autor, anio, categoria, editorial, paginas, precioVenta, stock, porcentajeIva);
        }
        return new LibroFisico(isbn, titulo, autor, anio, categoria, editorial, paginas, precioVenta, stock, porcentajeIva);
    }
}