package com.letsread.model;

public class ElementoCarrito {
    private Libro libro;
    private int cantidad;

    public ElementoCarrito(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public Libro getLibro() { return libro; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() {
        return libro.getPrecioVenta() * cantidad;
    }

    public double getImpuesto() {
        double precioSinIva = libro.getPrecioVenta() / (1 + (libro.getPorcentajeIva() / 100.0));
        return (libro.getPrecioVenta() - precioSinIva) * cantidad;
    }
}