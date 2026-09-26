package co.uptc.edu.negocio;

public class DetalleCompra {

	private int idDetalle;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private Libro libro;

    public DetalleCompra() {}

    public DetalleCompra(int idDetalle, int cantidad, double precioUnitario, Libro libro) {
        this.idDetalle = idDetalle;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.libro = libro;
        this.subtotal = calcularSubtotal();
    }

    // Método del diagrama de negocio
    public double calcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
        return this.subtotal;
    }

    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }

    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }
	
}
