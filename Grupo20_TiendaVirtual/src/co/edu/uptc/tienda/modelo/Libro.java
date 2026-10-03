package co.edu.uptc.tienda.modelo;

public class Libro {

    private final String isbn;
    private final String titulo;
    private final double precioVenta;
    private final int ivaPorcentaje;
    private final int cantidadInventario;

    public Libro(String isbn, String titulo, double precioVenta, int ivaPorcentaje, int cantidadInventario) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.precioVenta = precioVenta;
        this.ivaPorcentaje = ivaPorcentaje;
        this.cantidadInventario = cantidadInventario;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public int getIvaPorcentaje() {
        return ivaPorcentaje;
    }

    public int getCantidadInventario() {
        return cantidadInventario;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
