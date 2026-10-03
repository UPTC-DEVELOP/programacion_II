package co.edu.uptc.negocio;

public class CarritoItem {

    private final String isbn;
    private String titulo;
    private FormatoLibro formato;
    private double precioBase;
    private double porcentajeDescuento;
    private double impuestoIVA;
    private int cantidad;

    public CarritoItem(String isbn, String titulo, FormatoLibro formato, double precioBase,
                       double porcentajeDescuento, double impuestoIVA, int cantidad) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.formato = formato;
        this.precioBase = precioBase;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
        this.cantidad = cantidad;
    }

    public double getPrecioUnitarioSinIva() {
        return redondear(precioBase * (1 - porcentajeDescuento / 100.0));
    }

    public double getSubtotal() {
        return redondear(getPrecioUnitarioSinIva() * cantidad);
    }

    public double getImpuestoTotal() {
        return redondear(getSubtotal() * impuestoIVA / 100.0);
    }

    public double getTotal() {
        return redondear(getSubtotal() + getImpuestoTotal());
    }

    public void aumentarCantidad(int incremento) {
        if (incremento <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        cantidad += incremento;
    }

    public void disminuirCantidad(int decremento) {
        if (decremento <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        if (decremento >= cantidad) {
            cantidad = 0;
        } else {
            cantidad -= decremento;
        }
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public FormatoLibro getFormato() { return formato; }
    public double getPrecioBase() { return precioBase; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public double getImpuestoIVA() { return impuestoIVA; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public void actualizarDatos(String titulo, FormatoLibro formato, double precioBase,
                               double porcentajeDescuento, double impuestoIVA) {
        this.titulo = titulo;
        this.formato = formato;
        this.precioBase = precioBase;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }
}
