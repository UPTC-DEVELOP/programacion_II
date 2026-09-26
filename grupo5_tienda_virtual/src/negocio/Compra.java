package negocio;

import java.util.ArrayList;
import java.util.List;

/**
 * Compra confirmada. Se arma a partir del carrito.
 */
public class Compra {

    private String metodoPago;
    private java.time.LocalDate fecha;
    private List<ItemCompra> items;

    public Compra() {
        this.items = new ArrayList<ItemCompra>();
        this.fecha = java.time.LocalDate.now();
    }

    public Compra(String metodoPago) {
        this();
        this.metodoPago = metodoPago;
    }

    public void agregarItem(ItemCompra item) {
        items.add(item);
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemCompra item : items) {
            total += item.getTotalLinea();
        }
        return total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public java.time.LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(java.time.LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<ItemCompra> getItems() {
        return items;
    }

    public void setItems(List<ItemCompra> items) {
        this.items = items;
    }
}
