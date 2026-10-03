public class Producto {
    private String nombre;
    private double precioBase;
    private int stock;
    private double impuestoIVA; // 0.19 = 19%

    public Producto(String nombre, double precioBase, int stock, double impuestoIVA) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.impuestoIVA = impuestoIVA;
    }

    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public double getImpuestoIVA() { return impuestoIVA; }

    public double calcularPrecioFinal() {
        return precioBase * (1 + impuestoIVA);
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

bash
git status
git add .
git commit -m "Agrega impuestoIVA y aplica IVA en calcularPrecioFinal"
git pull --no-rebase origin feature/grupo20