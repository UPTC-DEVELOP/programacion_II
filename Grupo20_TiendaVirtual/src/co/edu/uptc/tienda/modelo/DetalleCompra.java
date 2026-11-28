package co.edu.uptc.tienda.modelo;

public class DetalleCompra {

    private String isbn;
    private String titulo;
    private int cantidad;
    private double precioUnitario;
    private double ivaPorcentaje;

    public DetalleCompra(String isbn, String titulo, int cantidad, double precioUnitario, double ivaPorcentaje) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.ivaPorcentaje = ivaPorcentaje;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getIvaPorcentaje() {
        return ivaPorcentaje;
    }

    public double obtenerSubtotal() {
        return precioUnitario * cantidad;
    }

    public double obtenerImpuesto() {
        return obtenerSubtotal() * ivaPorcentaje / 100.0;
    }
}
