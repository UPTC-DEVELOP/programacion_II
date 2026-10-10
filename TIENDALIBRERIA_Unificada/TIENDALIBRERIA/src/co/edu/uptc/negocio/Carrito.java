package co.edu.uptc.negocio;

import java.util.ArrayList;
import java.util.List;
import co.uptc.edu.libro.modelo.Libro;
public class Carrito {
    private List<Libro> items;
    private boolean esPremium;

    public Carrito() {
        this.items = new ArrayList<>();
        this.esPremium = false;
    }

    public void agregarLibro(Libro libro) {
        items.add(libro);
    }

    public void eliminarLibro(int indice) {
        if (indice >= 0 && indice < items.size()) {
            items.remove(indice);
        }
    }

    public void vaciarCarrito() {
        items.clear();
    }

    public double calcularSubtotal() {
        double subtotal = 0.0;
        for (Libro l : items) {
            subtotal += l.getPrecioVenta();
        }
        return subtotal;
    }

    public double calcularImpuestos() {
        double totalIVA = 0.0;
        for (Libro l : items) {
            totalIVA += l.getPrecioVenta() * 0.19;
        }
        return totalIVA;
    }

    public double calcularDescuento() {
        if (!esPremium) {
            return 0.0;
        }
        double totalSinDescuento = calcularSubtotal() + calcularImpuestos();
        return totalSinDescuento * 0.10;
    }

    public double calcularTotalFinal() {
        double totalSinDescuento = calcularSubtotal() + calcularImpuestos();
        return totalSinDescuento - calcularDescuento();
    }

    public List<Libro> getItems() {
        return items;
    }

    public void setEsPremium(boolean esPremium) {
        this.esPremium = esPremium;
    }

    public boolean isEsPremium() {
        return esPremium;
    }
}