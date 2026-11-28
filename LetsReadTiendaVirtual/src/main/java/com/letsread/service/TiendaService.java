package com.letsread.service;

import com.letsread.model.*;
import com.letsread.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class TiendaService {
    private final LibroService libroService;
    private List<ElementoCarrito> carrito = new ArrayList<>();
    private Cliente clienteActual = null;

    public TiendaService(LibroService libroService) {
        this.libroService = libroService;
    }

    public List<Libro> getCatalogo() { return libroService.listar(); }
    public List<ElementoCarrito> getCarrito() { return carrito; }
    public Cliente getClienteActual() { return clienteActual; }
    public void setClienteActual(Cliente cliente) { this.clienteActual = cliente; }

    public void agregarAlCarrito(Libro libro, int cantidad) throws Exception {
        if (cantidad > libro.getCantidadInventario()) {
            throw new Exception("No hay suficiente stock en el inventario de Let's Read.");
        }

        for (ElementoCarrito item : carrito) {
            if (item.getLibro().getIsbn().equals(libro.getIsbn())) {
                if (item.getCantidad() + cantidad > libro.getCantidadInventario()) {
                    throw new Exception("La cantidad supera el stock disponible.");
                }
                item.setCantidad(item.getCantidad() + cantidad);
                CarritoJsonDAO.guardarCarrito(carrito);
                ArchivoTxtDAO.registrarOperacion("Cantidad actualizada en carrito: " + libro.getTitulo());
                return;
            }
        }

        carrito.add(new ElementoCarrito(libro, cantidad));
        CarritoJsonDAO.guardarCarrito(carrito);
        ArchivoTxtDAO.registrarOperacion("Añadido al carrito de Let's Read: " + libro.getTitulo());
    }

    public void procesarCompra() {
        for (ElementoCarrito item : carrito) {
            Libro libro = item.getLibro();
            libro.setCantidadInventario(libro.getCantidadInventario() - item.getCantidad());
            libro.setTieneVentas(true);
            libroService.registrarVenta(libro);
        }
        ArchivoTxtDAO.registrarOperacion("Compra realizada con éxito por " + (clienteActual != null ? clienteActual.getNombreCompleto() : "Cliente Anónimo"));
        carrito.clear();
        CarritoJsonDAO.guardarCarrito(carrito);
    }

    // --- Administracion de catalogo: delega en LibroService (se retira en el commit 4) ---

    public void registrarLibro(Libro nuevoLibro) throws Exception {
        libroService.registrar(nuevoLibro);
    }

    public void actualizarLibro(String isbnOriginal, Libro datosActualizados) throws Exception {
        libroService.actualizar(isbnOriginal, datosActualizados);
    }

    public void eliminarLibro(String isbn) throws Exception {
        libroService.eliminar(isbn);
    }

    public Libro buscarLibroPorIsbn(String isbn) {
        return libroService.buscarPorIsbn(isbn);
    }
}