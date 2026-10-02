package co.edu.uptc.negocio;

public class LibroDigital extends Libro {

    public static final double PORCENTAJE_IVA = 0.05;

    public LibroDigital(String isbn, String titulo, String autor, int anioPublicacion,
                        String categoria, String editorial, double precioBase,
                        int cantidadDisponible) {
        super(isbn, titulo, autor, anioPublicacion, categoria, editorial,
              precioBase, cantidadDisponible);
    }

  
    public FormatoLibro getFormato() {
        return FormatoLibro.DIGITAL;
    }

   
    public double getPorcentajeIva() {
        return PORCENTAJE_IVA;
    }

    
    public int getNumeroPaginas() {
        return 0; // No aplica a libros digitales
    }
}
