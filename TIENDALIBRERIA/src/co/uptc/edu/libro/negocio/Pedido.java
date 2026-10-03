package co.uptc.edu.libro.negocio;

import co.uptc.edu.libro.modelo.Libro;

public class Pedido {

    private Libro libro;
    private int cantidad;
    private double total;

    public Pedido(Libro libro, int cantidad, double total) {
        this.libro = libro;
        this.cantidad = cantidad;
        this.total = total;
    }

    public Libro getLibro() {
        return libro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getTotal() {
        return total;
    }
}