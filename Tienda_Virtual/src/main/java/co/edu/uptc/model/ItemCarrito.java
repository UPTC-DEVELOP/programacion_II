package co.edu.uptc.model;

public class ItemCarrito {
    private Libro libro;
    private int cantidad;

    public ItemCarrito() {}

    public ItemCarrito(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotalBase() {
        double precioSinIva = libro.getPrecioVenta() / (1 + (libro.getPorcentajeIva() / 100.0));
        return precioSinIva * cantidad;
    }

    public double getImpuestos() {
        return getSubtotalBase() * (libro.getPorcentajeIva() / 100.0);
    }

    public double getSubtotalConIva() {
        return libro.getPrecioVenta() * cantidad;
    }
}