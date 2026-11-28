package co.edu.uptc.negocio;

public class LibroFisico extends Libro {

    /** IVA expresado en porcentaje. */
    public static final double IVA_FISICO = 19.0;

    private int numeroPaginas;

    public LibroFisico(String isbn, String titulo, String autor, int anioPublicacion,
                       String categoria, String editorial, int numeroPaginas,
                       double precioBase, int cantidadDisponible, double porcentajeDescuento) {
        super(isbn, titulo, autor, anioPublicacion, categoria, editorial,
              precioBase, cantidadDisponible, porcentajeDescuento, IVA_FISICO);
        this.numeroPaginas = numeroPaginas;
    }

    @Override
    public FormatoLibro getFormato() {
        return FormatoLibro.FISICO;
    }

    @Override
    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }
}
