package negocio;

/**
 * Producto de la tienda. Guarda los datos basicos y calcula el precio final.
 */
public class Producto {

    private String nombre;
    private double precioBase;
    private int stock;

    public Producto() {
    }

    public Producto(String nombre, double precioBase, int stock) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
    }

    /**
     * Calcula el precio final del producto a partir del precio base.
     */
    public double calcularPrecioFinal() {
        return precioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
