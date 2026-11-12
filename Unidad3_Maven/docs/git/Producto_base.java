package co.edu.uptc.model;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precioBase;
    private int stock;

    protected Producto(String codigo, String nombre, double precioBase, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
    }

    public double calcularPrecioFinal() {
        return precioBase;
    }

    public abstract String getDescripcionEntrega();
}
