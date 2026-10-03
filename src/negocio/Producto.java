package negocio; // Revisa que este package coincida con tu proyecto

public class Producto {
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento;
    private double impuestoIVA; // 1. Atributo IVA

    public Producto(String nombre, double precioBase, int stock, double porcentajeDescuento, double impuestoIVA) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }

    // 2. Método modificado considerando el IVA (y manteniendo estructura)
    public double calcularPrecioFinal() {
        double precioConDescuento = precioBase * (1 - (porcentajeDescuento / 100.0));
        return precioConDescuento + (precioConDescuento * (impuestoIVA / 100.0));
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    
    // 3. Getter y Setter de impuestoIVA
    public double getImpuestoIVA() { return impuestoIVA; }
    public void setImpuestoIVA(double impuestoIVA) { this.impuestoIVA = impuestoIVA; }

    @Override
    public String toString() {
        return nombre;
    }
}
