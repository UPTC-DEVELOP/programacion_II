package co.edu.uptc.model;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precioBase;
    private int stock;
    private double impuestoIVA;

    protected Producto(String codigo, String nombre, double precioBase, int stock,
                       double impuestoIVA) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.impuestoIVA = impuestoIVA;
    }

    public double calcularPrecioFinal() {
        return precioBase * (1 + impuestoIVA / 100.0);
    }

    public abstract String getDescripcionEntrega();
}
