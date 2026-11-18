package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Compra confirmada. Se arma a partir del carrito.
 */
public class Compra {

    private long id;
    private String metodoPago;
    private java.time.LocalDate fecha;
    private String correoCliente;
    private double total;
    private List<ItemCompra> items;

    public Compra() {
        this.items = new ArrayList<ItemCompra>();
        this.fecha = java.time.LocalDate.now();
    }

    public Compra(String metodoPago) {
        this();
        this.metodoPago = metodoPago;
    }

    public Compra(String metodoPago, String correoCliente) {
        this(metodoPago);
        this.correoCliente = correoCliente;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<ItemCompra> getItems() {
        return items;
    }

    public void setItems(List<ItemCompra> items) {
        this.items = items;
    }
}
