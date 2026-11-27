package co.edu.uptc.caso.estudio.model;

import java.util.ArrayList;
import java.util.Iterator;
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

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    // Agrega un libro o suma cantidad si ya existe en el carrito
    public void agregarItem(Libro libro, int cantidad) {
        if (libro == null || cantidad <= 0) {
            return;
        }
        if (items == null) {
            items = new ArrayList<>();
        }

        ItemCarrito existente = buscarItem(libro.getIsbn());
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            return;
        }
        items.add(new ItemCarrito(libro, cantidad));
    }

    public ItemCarrito buscarItem(String isbn) {
        if (isbn == null || items == null) {
            return null;
        }
        for (ItemCarrito item : items) {
            if (item != null && item.getLibro() != null && isbn.equals(item.getLibro().getIsbn())) {
                return item;
            }
        }
        return null;
    }

    // Reemplaza la cantidad del item. Retorna false si la cantidad es <= 0 o el ISBN no está
    public boolean actualizarCantidad(String isbn, int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        ItemCarrito item = buscarItem(isbn);
        if (item == null) {
            return false;
        }
        item.setCantidad(cantidad);
        return true;
    }

    // Elimina todos los items cuyo ISBN esté en la lista. Retorna true si quitó al menos uno
    public boolean eliminarItems(List<String> isbns) {
        if (isbns == null || items == null) {
            return false;
        }
        boolean eliminado = false;
        Iterator<ItemCarrito> it = items.iterator();
        while (it.hasNext()) {
            ItemCarrito item = it.next();
            if (item != null && item.getLibro() != null && isbns.contains(item.getLibro().getIsbn())) {
                it.remove();
                eliminado = true;
            }
        }
        return eliminado;
    }

    public void vaciar() {
        if (items != null) {
            items.clear();
        }
    }

    public boolean estaVacio() {
        return items == null || items.isEmpty();
    }

    public double calcularSubtotalSinIva() {
        if (items == null) {
            return 0.0;
        }
        double subtotal = 0.0;
        for (ItemCarrito item : items) {
            if (item != null && item.getLibro() != null) {
                subtotal += item.getSubtotalSinIva();
            }
        }
        return subtotal;
    }

    public double calcularTotalImpuestos() {
        if (items == null) {
            return 0.0;
        }
        double impuestos = 0.0;
        for (ItemCarrito item : items) {
            if (item != null && item.getLibro() != null) {
                impuestos += item.getValorIva();
            }
        }
        return impuestos;
    }

    public double calcularTotal() {
        if (items == null) {
            return 0.0;
        }
        double total = 0.0;
        for (ItemCarrito item : items) {
            if (item != null && item.getLibro() != null) {
                total += item.getSubtotalConIva();
            }
        }
        return total;
    }

    // Polimorfismo (LSP): delega el descuento a Cliente (Regular = 0, Premium = 10%)
    public double calcularDescuento(Cliente cliente) {
        if (cliente == null) {
            return 0.0;
        }
        return cliente.calcularDescuento(calcularTotal());
    }

    // Total con IVA menos el descuento del cliente (nunca menor a 0.0)
    public double calcularTotalFinal(Cliente cliente) {
        double totalConIva = calcularTotal();
        double descuento = cliente != null ? cliente.calcularDescuento(totalConIva) : 0.0;
        double finalCalculado = totalConIva - descuento;
        return finalCalculado < 0.0 ? 0.0 : finalCalculado;
    }
}