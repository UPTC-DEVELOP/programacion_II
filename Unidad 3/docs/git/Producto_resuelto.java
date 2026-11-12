package co.edu.uptc.model;

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

    public double calcularPrecioFinal() {
        double precioConDescuento = precioBase * (1 - porcentajeDescuento / 100.0);
        return precioConDescuento * (1 + impuestoIVA / 100.0);
    }

    public abstract String getDescripcionEntrega();
}
