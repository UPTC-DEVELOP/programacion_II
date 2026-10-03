package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCarrito;
import co.edu.uptc.tienda.modelo.Carrito;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;

public class GestionCarrito implements IGestionCarrito {

    private final Carrito carrito;

    public GestionCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    @Override
    public void agregarLibro(Libro libro) throws CarritoExcepcion {
        agregarLibro(libro, 1);
    }

    @Override
    public void agregarLibro(Libro libro, int cantidad) throws CarritoExcepcion {
        if (libro == null) {
            throw new CarritoExcepcion("Debe seleccionar un libro.");
        }
        if (cantidad <= 0) {
            throw new CarritoExcepcion("La cantidad debe ser mayor que 0.");
        }
        ItemCarrito existente = carrito.buscarItem(libro.getIsbn());
        int enCarrito = (existente == null) ? 0 : existente.getCantidad();
        int totalSolicitado = enCarrito + cantidad;
        if (totalSolicitado > libro.getCantidadInventario()) {
            throw new CarritoExcepcion("No hay inventario suficiente. Solicitado: " + totalSolicitado
                    + ", disponible: " + libro.getCantidadInventario() + ".");
        }
        if (existente == null) {
            carrito.agregarItem(new ItemCarrito(libro, cantidad));
        } else {
            existente.setCantidad(totalSolicitado);
        }
    }

    @Override
    public void cambiarCantidad(String isbn, int nuevaCantidad) throws CarritoExcepcion {
        ItemCarrito item = carrito.buscarItem(isbn);
        if (item == null) {
            throw new CarritoExcepcion("El libro con ISBN " + isbn + " no esta en el carrito.");
        }
        if (nuevaCantidad <= 0) {
            throw new CarritoExcepcion("La cantidad debe ser mayor que 0. Para quitar el libro use Eliminar.");
        }
        if (nuevaCantidad == item.getCantidad()) {
            throw new CarritoExcepcion("La nueva cantidad debe ser diferente a la actual.");
        }
        if (nuevaCantidad > item.getLibro().getCantidadInventario()) {
            throw new CarritoExcepcion("No hay inventario suficiente. Solicitado: " + nuevaCantidad
                    + ", disponible: " + item.getLibro().getCantidadInventario() + ".");
        }
        item.setCantidad(nuevaCantidad);
    }

    @Override
    public void eliminarLibro(String isbn) throws CarritoExcepcion {
        ItemCarrito item = carrito.buscarItem(isbn);
        if (item == null) {
            throw new CarritoExcepcion("El libro con ISBN " + isbn + " no esta en el carrito.");
        }
        carrito.quitarItem(item);
    }

    @Override
    public List<ItemCarrito> obtenerItems() {
        return carrito.getItems();
    }

    @Override
    public double calcularSubtotal() {
        double suma = 0;
        for (ItemCarrito item : carrito.getItems()) {
            suma += item.obtenerSubtotalItem();
        }
        return suma;
    }

    @Override
    public double calcularImpuestos() {
        double suma = 0;
        for (ItemCarrito item : carrito.getItems()) {
            suma += item.obtenerImpuestoItem();
        }
        return suma;
    }

    @Override
    public double calcularTotal() {
        return calcularSubtotal() + calcularImpuestos();
    }

    @Override
    public double calcularTotal(double porcentajeDescuento) {
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        return calcularTotal() * (1 - porcentajeDescuento / 100.0);
    }
}
