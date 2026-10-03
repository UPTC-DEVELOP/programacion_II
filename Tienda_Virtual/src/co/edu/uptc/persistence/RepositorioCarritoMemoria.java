package co.edu.uptc.persistence;

import co.edu.uptc.model.CarritoCompras;
import co.edu.uptc.model.Libro;

public class RepositorioCarritoMemoria implements IRepositorioCarrito {

    private CarritoCompras carrito;

    public RepositorioCarritoMemoria() {
        // La tienda aún no maneja sesión de cliente: CarritoCompras acepta cliente nulo (descuento 0)
        this.carrito = new CarritoCompras(null);
    }

    @Override
    public boolean agregarItem(Libro libro, int cantidad) {
        if (libro == null || cantidad <= 0) {
            return false;
        }

        boolean resultado = carrito.agregarLibro(libro, cantidad);
        if (resultado) {
            ArchivoLoggerManager.registrarOperacion(
                "Carrito: agregado " + cantidad + " x " + libro.getTitulo() + " (ISBN: " + libro.getIsbn() + ")"
            );
        }
        return resultado;
    }

    @Override
    public boolean actualizarCantidad(String isbn, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            return false; // Para quitar un libro del carrito se usa eliminarItem
        }

        boolean resultado = carrito.actualizarCantidad(isbn, nuevaCantidad);
        if (resultado) {
            ArchivoLoggerManager.registrarOperacion(
                "Carrito: cantidad del ISBN " + isbn + " actualizada a " + nuevaCantidad
            );
        }
        return resultado;
    }

    @Override
    public boolean eliminarItem(String isbn) {
        boolean resultado = carrito.eliminarLibro(isbn);
        if (resultado) {
            ArchivoLoggerManager.registrarOperacion("Carrito: eliminado libro con ISBN: " + isbn);
        }
        return resultado;
    }

    @Override
    public void vaciar() {
        carrito.limpiarCarrito();
        ArchivoLoggerManager.registrarOperacion("Carrito: vaciado");
    }

    @Override
    public CarritoCompras obtenerCarrito() {
        return carrito;
    }
}