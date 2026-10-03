package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompras {
    private List<ItemCarrito> items;
    private Cliente cliente;

    public CarritoCompras(Cliente cliente) {
        this.cliente = cliente;
        this.items = new ArrayList<>();
    }

    // Agregar un libro al carrito o incrementar su cantidad si ya existe
    public boolean agregarLibro(Libro libro, int cantidad) {
        if (cantidad > libro.getCantidadInventario()) {
            return false; // Supera el inventario disponible
        }

        for (ItemCarrito item : items) {
            if (item.getLibro().getIsbn().equalsIgnoreCase(libro.getIsbn())) {
                int nuevaCantidad = item.getCantidad() + cantidad;
                if (nuevaCantidad > libro.getCantidadInventario()) {
                    return false;
                }
                item.setCantidad(nuevaCantidad);
                return true;
            }
        }

        items.add(new ItemCarrito(libro, cantidad));
        return true;
    }

    // Eliminar un libro del carrito por su ISBN
    public boolean eliminarLibro(String isbn) {
        return items.removeIf(item -> item.getLibro().getIsbn().equalsIgnoreCase(isbn));
    }

    // Modificar la cantidad de un libro existente
    public boolean actualizarCantidad(String isbn, int nuevaCantidad) {
        for (ItemCarrito item : items) {
            if (item.getLibro().getIsbn().equalsIgnoreCase(isbn)) {
                if (nuevaCantidad <= 0) {
                    return eliminarLibro(isbn);
                }
                if (nuevaCantidad > item.getLibro().getCantidadInventario()) {
                    return false;
                }
                item.setCantidad(nuevaCantidad);
                return true;
            }
        }
        return false;
    }

    // Suma de los subtotales de todos los items
    public double calcularSubtotalTotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.calcularSubtotal();
        }
        return subtotal;
    }

    // Suma de todos los impuestos (IVA)
    public double calcularTotalImpuestos() {
        double impuestos = 0;
        for (ItemCarrito item : items) {
            impuestos += item.calcularImpuesto();
        }
        return impuestos;
    }

    // Calcula el valor del descuento aplicado según el tipo de cliente (Regular / Premium)
    public double calcularDescuento() {
        double subtotal = calcularSubtotalTotal();
        if (cliente != null) {
            return cliente.calcularDescuento(subtotal);
        }
        return 0.0;
    }

    // Total final a pagar: (Subtotal + Impuestos) - Descuento
    public double calcularTotalPagar() {
        return (calcularSubtotalTotal() + calcularTotalImpuestos()) - calcularDescuento();
    }

    // Vaciar el carrito
    public void limpiarCarrito() {
        items.clear();
    }

    // Getters y Setters
    public List<ItemCarrito> getItems() {
        return items;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}