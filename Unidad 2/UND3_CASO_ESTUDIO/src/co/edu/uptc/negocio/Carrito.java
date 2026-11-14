package co.edu.uptc.negocio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Carrito {

    private final String correoCliente;
    private final Map<String, CarritoItem> items = new LinkedHashMap<>();

    public Carrito(String correoCliente) {
        if (correoCliente == null || correoCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El carrito debe pertenecer a un cliente.");
        }
        this.correoCliente = correoCliente.trim().toLowerCase();
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void agregar(CarritoItem item) {
        CarritoItem existente = items.get(item.getIsbn());
        if (existente == null) {
            items.put(item.getIsbn(), item);
        } else {
            existente.aumentarCantidad(item.getCantidad());
        }
    }

    public boolean contiene(String isbn) {
        return items.containsKey(isbn);
    }

    public CarritoItem buscarItem(String isbn) {
        return items.get(isbn);
    }

    public void aumentar(String isbn) {
        CarritoItem item = exigirItem(isbn);
        item.aumentarCantidad(1);
    }

    public void disminuir(String isbn) {
        CarritoItem item = exigirItem(isbn);
        item.disminuirCantidad(1);
        if (item.getCantidad() == 0) {
            items.remove(isbn);
        }
    }

    public void eliminar(String isbn) {
        items.remove(isbn);
    }

    public void vaciar() {
        items.clear();
    }

    public List<CarritoItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public int getCantidadTotal() {
        int total = 0;
        for (CarritoItem item : items.values()) {
            total += item.getCantidad();
        }
        return total;
    }

    public double getSubtotal() {
        double total = 0;
        for (CarritoItem item : items.values()) {
            total += item.getSubtotal();
        }
        return redondear(total);
    }

    public double getImpuestos() {
        double total = 0;
        for (CarritoItem item : items.values()) {
            total += item.getImpuestoTotal();
        }
        return redondear(total);
    }

    public double getTotalAntesDescuentoPremium() {
        return redondear(getSubtotal() + getImpuestos());
    }

    private CarritoItem exigirItem(String isbn) {
        CarritoItem item = items.get(isbn);
        if (item == null) {
            throw new IllegalArgumentException("El libro no está en el carrito.");
        }
        return item;
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
