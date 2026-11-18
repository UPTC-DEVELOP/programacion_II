package model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Carrito de compras del cliente. Guarda pares (Libro, cantidad).
 */
public class CarritoCompras {

    private static final double TOLERANCIA_IVA = 0.01;

    private Map<Libro, Integer> items;

    public CarritoCompras() {
        this.items = new LinkedHashMap<Libro, Integer>();
    }
// AGREGAR LIBRO
    
    public void agregarLibro(Libro libro, int cantidad) {
        if (libro == null || cantidad <= 0) {
            return;
        }

        Integer actual = items.get(libro);

        if (actual == null) {
            if (cantidad <= libro.getCantidadDisponible()) {
                items.put(libro, cantidad);
            }
        } else {
            int nuevaCantidad = actual + cantidad;

            if (nuevaCantidad <= libro.getCantidadDisponible()) {
                items.put(libro, nuevaCantidad);
            }
        }
    }
    
    // MODIFCAR CANTIDAD 
    
    public void modificarCantidad(Libro libro, int nuevaCantidad) {
        if (libro == null || !items.containsKey(libro)) {
            return;
        }

        if (nuevaCantidad <= 0) {
            items.remove(libro);
        } else if (nuevaCantidad <= libro.getCantidadDisponible()) {
            items.put(libro, nuevaCantidad);
        }
    }

    public void eliminarLibro(Libro libro) {
        items.remove(libro);
    }

    public void vaciar() {
        items.clear();
    }

    public double calcularSubtotal() {
        double total = 0;
        for (Map.Entry<Libro, Integer> e : items.entrySet()) {
            total += e.getKey().getPrecio() * e.getValue();
        }
        return total;
    }

    public double calcularIVA19() {
        return calcularIvaPorPorcentaje(19.0);
    }

    public double calcularIVA5() {
        return calcularIvaPorPorcentaje(5.0);
    }

    private double calcularIvaPorPorcentaje(double porcentaje) {
        double total = 0;
        for (Map.Entry<Libro, Integer> e : items.entrySet()) {
            Libro libro = e.getKey();
            if (esPorcentaje(libro, porcentaje)) {
                total += libro.getPrecio() * e.getValue() * (libro.getPorcentajeIva() / 100.0);
            }
        }
        return total;
    }

    public double calcularOtrosIva() {
        double total = 0;
        for (Map.Entry<Libro, Integer> e : items.entrySet()) {
            Libro libro = e.getKey();
            if (!esPorcentaje(libro, 19.0) && !esPorcentaje(libro, 5.0)) {
                total += libro.getPrecio() * e.getValue() * (libro.getPorcentajeIva() / 100.0);
            }
        }
        return total;
    }

    public double calcularIVA() {
        double total = 0;
        for (Map.Entry<Libro, Integer> e : items.entrySet()) {
            Libro libro = e.getKey();
            total += libro.getPrecio() * e.getValue() * (libro.getPorcentajeIva() / 100.0);
        }
        return total;
    }

    private boolean esPorcentaje(Libro libro, double porcentaje) {
        return Math.abs(libro.getPorcentajeIva() - porcentaje) < TOLERANCIA_IVA;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA();
    }

 // CALCULAR TOTAL APLICANDO DESCUENTO SOBRE EL SUBTOTAL
    public double calcularTotal(double descuento) {

        double subtotal = calcularSubtotal();

        double valorDescuento = subtotal * descuento;

        return subtotal - valorDescuento + calcularIVA();
    }

    public Map<Libro, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Libro, Integer> items) {
        this.items = items;
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
}
