package co.uptc.edu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Carrito de compras (RF-05): agregar libros, ajustar cantidades, eliminar ítems
 * y calcular subtotal, IVA y total a pagar.
 * No depende de Swing. Los errores de validación se informan con IllegalArgumentException.
 */
public class CarritoDeCompras {

    /** Tasa de IVA aplicada sobre el subtotal (19 %). */
    public static final double TASA_IVA = 0.19;

    private final List<ItemCarrito> items = new ArrayList<>();

    // ---------- Read ----------
    public List<ItemCarrito> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    /** Busca el ítem por ISBN. Devuelve null si el libro no está en el carrito. */
    public ItemCarrito buscarItem(String isbn) {
        if (isbn == null) {
            return null;
        }
        for (ItemCarrito item : items) {
            if (item.getLibro().getIsbn().equals(isbn)) {
                return item;
            }
        }
        return null;
    }

    // ---------- Create ----------
    /** Agrega un libro (1 unidad). Si ya está en el carrito suma una unidad, sin superar el stock. */
    public Tienda.ResultadoCarrito agregarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo.");
        }
        ItemCarrito existente = buscarItem(libro.getIsbn());
        if (existente != null) {
            if (existente.getCantidad() < libro.getStock()) {
                existente.setCantidad(existente.getCantidad() + 1);
                return Tienda.ResultadoCarrito.CANTIDAD_INCREMENTADA;
            }
            return Tienda.ResultadoCarrito.SIN_STOCK;
        }
        if (libro.getStock() < 1) {
            return Tienda.ResultadoCarrito.SIN_STOCK;
        }
        items.add(new ItemCarrito(libro, 1));
        return Tienda.ResultadoCarrito.AGREGADO;
    }

    // ---------- Update ----------
    /** Fija la cantidad de un ítem. Debe ser al menos 1 y no superar el stock del libro. */
    public void ajustarCantidad(String isbn, int nuevaCantidad) {
        ItemCarrito item = requerir(isbn);
        if (nuevaCantidad < 1) {
            throw new IllegalArgumentException("La cantidad mínima es 1. Usa eliminar para quitar el libro del carrito.");
        }
        if (nuevaCantidad > item.getLibro().getStock()) {
            throw new IllegalArgumentException("Solo hay " + item.getLibro().getStock()
                    + " unidades disponibles de '" + item.getLibro().getTituloLibro() + "'.");
        }
        item.setCantidad(nuevaCantidad);
    }

    public void incrementarCantidad(String isbn) {
        ajustarCantidad(isbn, requerir(isbn).getCantidad() + 1);
    }

    public void decrementarCantidad(String isbn) {
        ajustarCantidad(isbn, requerir(isbn).getCantidad() - 1);
    }

    // ---------- Delete ----------
    public void eliminarLibro(String isbn) {
        items.remove(requerir(isbn));
    }

    public void vaciarCarrito() {
        items.clear();
    }

    // ---------- Cálculos ----------
    public double calcularSubtotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public double calcularImpuestoTotal() {
        return Math.round(calcularSubtotal() * TASA_IVA * 100.0) / 100.0;
    }

    public double calcularTotalPagar() {
        return calcularSubtotal() + calcularImpuestoTotal();
    }

    // ---------- Auxiliares ----------
    private ItemCarrito requerir(String isbn) {
        ItemCarrito item = buscarItem(isbn);
        if (item == null) {
            throw new IllegalArgumentException("El libro no está en el carrito.");
        }
        return item;
    }
}
