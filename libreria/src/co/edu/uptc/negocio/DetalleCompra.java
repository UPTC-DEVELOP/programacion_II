package co.edu.uptc.negocio;

public class DetalleCompra {

    private final String isbn;
    private final String titulo;
    private final FormatoLibro formato;
    private final int cantidad;
    private final double precioUnitarioSinIva;
    private final double subtotal;
    private final double impuesto;
    private final double total;

    public DetalleCompra(String isbn, String titulo, FormatoLibro formato, int cantidad,
                         double precioUnitarioSinIva, double subtotal, double impuesto, double total) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.formato = formato;
        this.cantidad = cantidad;
        this.precioUnitarioSinIva = precioUnitarioSinIva;
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.total = total;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public FormatoLibro getFormato() { return formato; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitarioSinIva() { return precioUnitarioSinIva; }
    public double getSubtotal() { return subtotal; }
    public double getImpuesto() { return impuesto; }
    public double getTotal() { return total; }
}
