package co.edu.uptc.negocio.parcial;

public class Producto {

    private String nombre;
    private double precio;
    private int stock;
    private double porcentajeDescuento;
    private double impuestoIVA;

    public Producto(
            String nombre,
            double precio,
            int stock,
            double porcentajeDescuento,
            double impuestoIVA) {

        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(
            double porcentajeDescuento) {

        this.porcentajeDescuento =
                porcentajeDescuento;
    }

    public double getImpuestoIVA() {
        return impuestoIVA;
    }

    public void setImpuestoIVA(
            double impuestoIVA) {

        this.impuestoIVA = impuestoIVA;
    }

    /**
     * Calcula el precio final:
     *
     * 1. Aplica el descuento.
     * 2. Aplica el IVA.
     */
    public double calcularPrecioFinal() {

        double precioConDescuento =
                precio
                - (precio * porcentajeDescuento / 100);

        double precioFinal =
                precioConDescuento
                + (precioConDescuento * impuestoIVA / 100);

        return precioFinal;
    }

    @Override
    public String toString() {
        return nombre;
    }
}