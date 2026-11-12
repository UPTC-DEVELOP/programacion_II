package co.edu.uptc.model;

import java.util.ArrayList;

// Carrito solo administra sus líneas y cálculos; la persistencia queda en CarritoRepository.
public class Carrito {
    private String correoCliente;
    private ArrayList<ItemCarrito> items;

    public Carrito(String correoCliente) {
        this.correoCliente = correoCliente;
        this.items = new ArrayList<ItemCarrito>();
    }
    public void agregar(ItemCarrito item) { items.add(item); }
    public ItemCarrito buscar(String isbn) {
        for (ItemCarrito item : items) if (item.getIsbn().equals(isbn)) return item;
        return null;
    }
    public void eliminar(String isbn) {
        ItemCarrito item = buscar(isbn); if (item != null) items.remove(item);
    }
    public void vaciar() { items.clear(); }
    public double getSubtotalGeneral() { double total = 0; for (ItemCarrito i : items) total += i.calcularSubtotal(); return total; }
    public double getTotalImpuestos() { double total = 0; for (ItemCarrito i : items) total += i.calcularImpuesto(); return total; }
    public double getTotalBruto() { return getSubtotalGeneral() + getTotalImpuestos(); }
    public String getCorreoCliente() { return correoCliente; }
    public ArrayList<ItemCarrito> getItems() { return items; }
    public void setItems(ArrayList<ItemCarrito> items) { this.items = items; }
}
