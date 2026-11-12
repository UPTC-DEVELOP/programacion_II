package co.edu.uptc.tienda.interfaz;

import java.util.List;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;

public interface IOperacionesCarrito {

    void agregarLibro(Libro libro) throws CarritoExcepcion;

    void agregarLibro(Libro libro, int cantidad) throws CarritoExcepcion;

    List<ItemCarrito> obtenerItems();

    void cambiarCantidad(String isbn, int nuevaCantidad) throws CarritoExcepcion;

    void eliminarLibro(String isbn) throws CarritoExcepcion;

    void vaciar() throws CarritoExcepcion;

    List<String> sincronizarConCatalogo(List<Libro> catalogo) throws CarritoExcepcion;

    void asignarCliente(String idCliente) throws CarritoExcepcion;

    String obtenerIdCliente();
}
