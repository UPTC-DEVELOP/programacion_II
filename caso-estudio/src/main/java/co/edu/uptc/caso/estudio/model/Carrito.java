package co.edu.uptc.caso.estudio.model;

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

    public void agregarItem(Libro libro, int cantidad) {
        if (libro == null || cantidad <= 0) return;
        if (items == null) items = new ArrayList<>();

        ItemCarrito existente = buscarItem(libro.getIsbn());
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            return;
        }
        items.add(new ItemCarrito(libro, cantidad));
    }

    public ItemCarrito buscarItem(String isbn) {
        if (isbn == null || items == null) return null;
        for (ItemCarrito item : items) {
            if (item.getLibro() != null && isbn.equals(item.getLibro().getIsbn())) {
                return item;
            }
        }
        return null;
    }

    public boolean actualizarCantidad(String isbn, int cantidad) {
        if (cantidad <= 0) return false;
        ItemCarrito item = buscarItem(isbn);
        if (item == null) return false;
        item.setCantidad(cantidad);
        return true;
    }

    public boolean eliminarItems(List<String> isbns) {
        if (isbns == null || items == null) return false;
        return items.removeIf(item -> item.getLibro() != null && isbns.contains(item.getLibro().getIsbn()));
    }

    public void vaciar() {
        if (items != null) items.clear();
    }

    public boolean estaVacio() {
        return items == null || items.isEmpty();
    }

    public double calcularSubtotalSinIva() {
        if (items == null) return 0.0;
        return items.stream()
                .filter(item -> item.getLibro() != null)
                .mapToDouble(ItemCarrito::getSubtotalSinIva)
                .sum();
    }

    public double calcularTotalImpuestos() {
        if (items == null) return 0.0;
        return items.stream()
                .filter(item -> item.getLibro() != null)
                .mapToDouble(ItemCarrito::getValorIva)
                .sum();
    }

    public double calcularTotal() {
        if (items == null) return 0.0;
        return items.stream()
                .filter(item -> item.getLibro() != null)
                .mapToDouble(ItemCarrito::getSubtotalConIva)
                .sum();
    }

    // Polimorfismo (LSP): delega el descuento a Cliente (Regular = 0, Premium = 10%)
    public double calcularDescuento(Cliente cliente) {
        return cliente != null ? cliente.calcularDescuento(calcularTotal()) : 0.0;
    }

    public double calcularTotalFinal(Cliente cliente) {
        return calcularTotal() - calcularDescuento(cliente);
    }
}