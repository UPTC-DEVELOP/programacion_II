package co.edu.uptc.caso.estudio.model;

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

     //el precio de venta del libro YA incluye IVA, por eso el valor sin IVA se obtiene
     //dividiendo: precioVenta / (1 + tasaIva). Para el total con IVA no se recalcula nada
    public double getSubtotalBase() {
        if (libro == null) return 0.0;
        double precioSinIva = libro.getPrecioVenta() / (1.0 + libro.getTasaIvaDecimal());
        return precioSinIva * cantidad;
    }

    public double getImpuestos() {
        return getSubtotalBase() * (libro != null ? libro.getTasaIvaDecimal() : 0.0);
    }

    public double getSubtotalConIva() {
        return libro != null ? libro.getPrecioVenta() * cantidad : 0.0;
    }

    // Alias compatibles con Carrito y el resto del sistema
    public double getSubtotalSinIva() { return getSubtotalBase(); }
    public double getValorIva() { return getImpuestos(); }
    public double calcularSubtotal() { return getSubtotalBase(); }
    public double calcularImpuesto() { return getImpuestos(); }
}