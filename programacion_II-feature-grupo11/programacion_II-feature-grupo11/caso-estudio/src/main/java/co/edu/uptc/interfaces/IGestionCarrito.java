package co.edu.uptc.interfaces;

import java.util.List;
import co.edu.uptc.model.Carrito;

public interface IGestionCarrito {

    List<Carrito> obtenerItems();

    void recargarDesdeJson();

    void agregarItem(String idProducto, String titulo, int cantidad, double precioUnitario) throws Exception;

    void agregarItem(Carrito item) throws Exception;

    void modificarCantidad(int indice, int nuevaCantidad) throws Exception;

    void eliminarItem(int indice) throws Exception;

    void vaciarCarrito();

    double calcularTotal();

    boolean estaVacio();

    String[] procesarCompra(String nombreCliente) throws Exception;
}