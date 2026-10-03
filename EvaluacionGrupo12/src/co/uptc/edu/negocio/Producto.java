package co.uptc.edu.negocio;

public class Producto {
    private String id;
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento;
    private double impuestoIVA;

    public Producto(String id, String nombre, double precioBase, int stock, double porcentajeDescuento, double impuestoIVA) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }


   
    public double calcularPrecioFinal() {
        double precioConDescuento = precioBase * (1.0 - (porcentajeDescuento / 100.0));
        return precioConDescuento * (1.0 + (impuestoIVA / 100.0));
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public double getImpuestoIVA() { return impuestoIVA; }


    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}