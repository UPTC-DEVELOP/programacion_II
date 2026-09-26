package negocio;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Carrito de compras del cliente. Guarda pares (Libro, cantidad).
 */
public class CarritoCompras {

    private Map<Libro, Integer> items;

    public CarritoCompras() {
        this.items = new LinkedHashMap<Libro, Integer>();
    }

    public void agregarLibro(Libro libro, int cantidad) {
        if (libro == null || cantidad <= 0) {
            return;
        }
        Integer actual = items.get(libro);
        if (actual == null) {
            items.put(libro, cantidad);
        } else {
            items.put(libro, actual + cantidad);
        }
    }

    public void modificarCantidad(Libro libro, int nuevaCantidad) {
        if (libro == null || !items.containsKey(libro)) {
            return;
        }
        if (nuevaCantidad <= 0) {
            items.remove(libro);
        } else {
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
            if (libro.getPorcentajeIva() == porcentaje) {
                total += libro.getPrecio() * e.getValue() * (porcentaje / 100.0);
            }
        }
        return total;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA19() + calcularIVA5();
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
