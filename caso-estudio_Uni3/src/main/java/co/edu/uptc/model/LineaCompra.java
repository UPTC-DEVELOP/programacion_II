package co.edu.uptc.model;

// Esta clase evita duplicar los cálculos entre una línea editable del carrito y una línea histórica.
public abstract class LineaCompra {
    private String isbn;
    private String titulo;
    private double precioBase;
    private double porcentajeDescuento;
    private double impuestoIVA;
    private int cantidad;

    protected LineaCompra(String isbn, String titulo, double precioBase, double descuento,
                          double impuestoIVA, int cantidad) {
        this.isbn = isbn; this.titulo = titulo; this.precioBase = precioBase;
        this.porcentajeDescuento = descuento; this.impuestoIVA = impuestoIVA; this.cantidad = cantidad;
    }
    public double calcularPrecioSinIva() { return precioBase * (1 - porcentajeDescuento / 100.0); }
    public double calcularSubtotal() { return calcularPrecioSinIva() * cantidad; }
    public double calcularImpuesto() { return calcularSubtotal() * impuestoIVA / 100.0; }
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public double getPrecioBase() { return precioBase; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public double getImpuestoIVA() { return impuestoIVA; }
    public int getCantidad() { return cantidad; }
    protected void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
