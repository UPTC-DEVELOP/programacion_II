package co.edu.uptc.negocio;

public class Producto {
    private String id;
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento; // Estudiante A
    private double impuestoIVA;         // Estudiante B (Usted)

    // Constructor de 6 parámetros corregido
    public Producto(String id, String nombre, double precioBase, int stock, double porcentajeDescuento, double impuestoIVA) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }

    // Método integrado (Descuento + IVA) que recibe la cantidad
    public double calcularPrecioFinal(int cantidad) {
        double precioConDescuento = this.precioBase * (1 - (this.porcentajeDescuento / 100.0));
        double precioConIVA = precioConDescuento * (1 + (this.impuestoIVA / 100.0));
        return precioConIVA * cantidad;
    }

    // Métodos para el Módulo C
    public void reducirStock(int cantidad) {
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public double getImpuestoIVA() { return impuestoIVA; } // ← Este era el que faltaba y causaba el error en línea 34
    
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return nombre; 
    }
}