package co.edu.uptc.model;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precioBase;
    private int stock;
<<<<<<< HEAD
    private double porcentajeDescuento;
=======
    private double impuestoIVA;
>>>>>>> origin/feature/gestion-productos

    public double calcularPrecioFinal() {
<<<<<<< HEAD
        return precioBase * (1 - porcentajeDescuento / 100.0);
=======
        return precioBase * (1 + impuestoIVA / 100.0);
>>>>>>> origin/feature/gestion-productos
    }

    public abstract String getDescripcionEntrega();
}
