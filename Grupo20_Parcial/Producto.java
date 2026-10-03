public class Producto {
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento; // 0.10 = 10%

    public Producto(String nombre, double precioBase, int stock, double porcentajeDescuento) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }

    public double calcularPrecioFinal() {
        return precioBase * (1 - porcentajeDescuento);
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