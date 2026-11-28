package co.edu.uptc.model;

// Esta clase concentra los datos y cálculos comunes de cualquier producto.
// Las subclases solo completan la forma en que se entrega el producto.
public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento;
    private double impuestoIVA;

    protected Producto(String codigo, String nombre, double precioBase, int stock,
                       double porcentajeDescuento, double impuestoIVA) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }

    public double calcularPrecioSinIva() {
        return precioBase * (1 - porcentajeDescuento / 100.0);
    }

    public double calcularPrecioFinal() {
        return calcularPrecioSinIva() * (1 + impuestoIVA / 100.0);
    }

    public double calcularSubtotal(int cantidad) {
        return calcularPrecioSinIva() * cantidad;
    }

    public double calcularImpuesto(int cantidad) {
        return calcularSubtotal(cantidad) * impuestoIVA / 100.0;
    }

    public boolean hayStock(int cantidad) {
        return cantidad > 0 && cantidad <= stock;
    }

    public void descontarStock(int cantidad) {
        if (!hayStock(cantidad)) {
            throw new IllegalArgumentException("La cantidad supera el stock disponible.");
        }
        stock -= cantidad;
    }

    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva.");
        }
        stock += cantidad;
    }

    public abstract String getDescripcionEntrega();
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }
    public double getImpuestoIVA() { return impuestoIVA; }
    public void setImpuestoIVA(double impuestoIVA) { this.impuestoIVA = impuestoIVA; }

    @Override
    public String toString() { return codigo + " - " + nombre; }
}
