package co.edu.uptc.caso.estudio.model;

public class LibroDigital extends Libro {

    public LibroDigital() {
        setFormato("Digital");
    }

    public LibroDigital(String isbn, String titulo, String autor, int anio, String categoria,
                        String editorial, int paginas, double precioVenta, int stock,
                        double porcentajeIva) {
        super(isbn, titulo, autor, anio, categoria, editorial, paginas,
                precioVenta, stock, "Digital", porcentajeIva);
    }

    @Override
    public boolean requiereEnvioFisico() {
        return false;
    }
}