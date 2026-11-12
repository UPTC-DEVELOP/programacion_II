package co.edu.uptc.model;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento;

    protected Producto(String codigo, String nombre, double precioBase, int stock,
                       double porcentajeDescuento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double calcularPrecioFinal() {
        return precioBase * (1 - porcentajeDescuento / 100.0);
    }

    public abstract String getDescripcionEntrega();
}
