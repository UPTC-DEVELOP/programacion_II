package co.edu.uptc.negocio;

public class LibroFisico extends Libro {

    public static final double PORCENTAJE_IVA = 0.19;

    private int numeroPaginas;

    public LibroFisico(String isbn, String titulo, String autor, int anioPublicacion,
                       String categoria, String editorial, int numeroPaginas,
                       double precioBase, int cantidadDisponible) {
        super(isbn, titulo, autor, anioPublicacion, categoria, editorial,
              precioBase, cantidadDisponible);
        this.numeroPaginas = numeroPaginas;
    }

   
    public FormatoLibro getFormato() {
        return FormatoLibro.FISICO;
    }

    
    public double getPorcentajeIva() {
        return PORCENTAJE_IVA;
    }

  
    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }
}

