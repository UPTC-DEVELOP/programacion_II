package main.java.co.edu.uptc.model;

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
}