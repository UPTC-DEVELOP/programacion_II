package co.edu.uptc.persistence;

import co.edu.uptc.model.CarritoCompras;
import co.edu.uptc.model.Libro;

public interface IRepositorioCarrito {
    boolean agregarItem(Libro libro, int cantidad);
    boolean actualizarCantidad(String isbn, int nuevaCantidad);
    boolean eliminarItem(String isbn);
    void vaciar();
    CarritoCompras obtenerCarrito();
}