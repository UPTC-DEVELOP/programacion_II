package co.edu.uptc.negocio;

public class Producto {
    private String id;
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento;
    private double impuestoIVA;

    public Producto(String id, String nombre, double precioBase, int stock, 
                    double porcentajeDescuento, double impuestoIVA) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }

    public double calcularPrecioFinal(int cantidad) {
        double precioConDescuento = this.precioBase * (1 - (this.porcentajeDescuento / 100.0));
        double precioConIVA = precioConDescuento * (1 + (this.impuestoIVA / 100.0));
        return precioConIVA * cantidad;
    }

    public void reducirStock(int cantidad) {
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public double getImpuestoIVA() { return impuestoIVA; }
    
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return nombre; 
    }
}