package co.uptc.edu.controlador;

import java.util.List;

import co.uptc.edu.model.CarritoDeCompras;
import co.uptc.edu.model.ItemCarrito;
import co.uptc.edu.model.Libro;
import co.uptc.edu.model.Tienda;

// Controlador del carrito y la compra (RF-05, RF-06)
public class CarritoControlador {

    private final Tienda tienda;
    private final SesionControlador sesion;

    public CarritoControlador(Tienda tienda, SesionControlador sesion) {
        this.tienda = tienda;
        this.sesion = sesion;
    }

    public List<ItemCarrito> getItems() {
        return tienda.getCarrito();
    }

    public Tienda.ResultadoCarrito agregarLibro(Libro libro) {
        return tienda.agregarAlCarrito(libro);
    }

    public void incrementar(String isbn) {
        tienda.incrementarCantidadCarrito(isbn);
    }

    public void decrementar(String isbn) {
        tienda.decrementarCantidadCarrito(isbn);
    }

    // Recibe la cantidad como texto desde el diálogo de la pantalla
    public void ajustarCantidad(String isbn, String cantidadTexto) {
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto == null ? "" : cantidadTexto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ingresa un número entero válido.");
        }
        tienda.ajustarCantidadCarrito(isbn, cantidad);
    }

    public void eliminar(String isbn) {
        tienda.eliminarDelCarrito(isbn);
    }

    public void vaciar() {
        tienda.vaciarCarrito();
    }

    public double getSubtotal() { return tienda.getSubtotalCarrito(); }

    public double getIva() { return tienda.getIvaCarrito(); }

    public double getTotal() { return tienda.getTotalCarrito(); }

    public int getPorcentajeIva() { return (int) Math.round(CarritoDeCompras.TASA_IVA * 100); }

    // Registra la compra y devuelve el nombre del comprador para el recibo
    public String finalizarCompra() {
        sesion.exigirSesion();
        if (tienda.getCarrito().isEmpty()) {
            throw new IllegalArgumentException("El carrito se encuentra vacío.");
        }
        tienda.finalizarCompra();
        return sesion.getNombreUsuarioActual();
    }
}
