public class Producto {
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento; // 0.10 = 10%
    private double impuestoIVA;         // 0.19 = 19%

    public Producto(String nombre, double precioBase, int stock,
                    double porcentajeDescuento, double impuestoIVA) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }

    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public double getImpuestoIVA() { return impuestoIVA; }

    public double calcularPrecioFinal() {
        return precioBase * (1 - porcentajeDescuento) * (1 + impuestoIVA);
    }

    public void descontarStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        if (cantidad > stock) throw new IllegalArgumentException("Stock insuficiente. Disponible: " + stock);
        stock -= cantidad;
    }

    public void reponerStock(int cantidad) {
        stock += cantidad;
    }

    @Override
    public String toString() { return nombre; }
}