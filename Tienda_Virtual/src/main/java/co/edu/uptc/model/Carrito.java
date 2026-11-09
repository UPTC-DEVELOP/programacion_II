package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private String clienteId;
    private List<ItemCarrito> items;

    public Carrito() {
        this.items = new ArrayList<>();
    }

    public Carrito(String clienteId) {
        this.clienteId = clienteId;
        this.items = new ArrayList<>();
    }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public List<ItemCarrito> getItems() { return items; }
    public void setItems(List<ItemCarrito> items) { this.items = items; }

     // Agrega un libro al carrito. Si el libro (mismo ISBN) ya está, suma la cantidad.
    public void agregarItem(Libro libro, int cantidad) {
        if (libro == null || cantidad <= 0) return;
        if (items == null) items = new ArrayList<>();

        for (ItemCarrito item : items) {
            if (item.getLibro() != null && item.getLibro().getIsbn() != null
                    && item.getLibro().getIsbn().equals(libro.getIsbn())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(libro, cantidad));
    }

    public double calcularTotal() {
        double total = 0;
        if (items == null) return total;
        for (ItemCarrito item : items) {
            if (item.getLibro() == null) continue;
            total += item.getSubtotalConIva();
        }
        return total;
    }
}