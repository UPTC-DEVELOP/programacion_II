package co.edu.uptc.caso.estudio.model;

public class LibroFisico extends Libro {

    public LibroFisico() {
        setFormato("Físico");
    }

    public LibroFisico(String isbn, String titulo, String autor, int anio, String categoria,
                       String editorial, int paginas, double precioVenta, int stock, double porcentajeIva) {
        super(isbn, titulo, autor, anio, categoria, editorial, paginas, precioVenta, stock, "Físico", porcentajeIva);
    }

    @Override
    public boolean requiereEnvioFisico() {
        return true;
    }
}