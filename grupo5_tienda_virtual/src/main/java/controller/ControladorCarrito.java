package controller;

import java.util.LinkedHashMap;
import java.util.Map;

import model.CarritoCompras;
import model.Libro;
import persistencia.GestorCarritoJSON;
import service.IServicioCarrito;
import service.IServicioLibros;

public class ControladorCarrito implements IServicioCarrito {

    private final IServicioLibros libros;
    private final GestorCarritoJSON gestor;

    public ControladorCarrito(IServicioLibros libros, GestorCarritoJSON gestor) {
        this.libros = libros;
        this.gestor = gestor;
    }

    @Override
    public CarritoCompras cargar(String correo) {
        CarritoCompras carrito = new CarritoCompras();
        for (Map.Entry<String, Integer> entrada : gestor.cargar(correo).entrySet()) {
            Libro libro = libros.buscar(entrada.getKey());
            if (libro != null) {
                carrito.agregarLibro(libro, entrada.getValue());
            }
        }
        return carrito;
    }

    @Override
    public void guardar(CarritoCompras carrito, String correo) {
        Map<String, Integer> items = new LinkedHashMap<String, Integer>();
        if (carrito != null) {
            for (Map.Entry<Libro, Integer> entrada : carrito.getItems().entrySet()) {
                Libro libro = entrada.getKey();
                if (libro != null && libro.getIsbn() != null) {
                    items.put(libro.getIsbn(), entrada.getValue());
                }
            }
        }
        gestor.guardar(items, correo);
    }

    @Override
    public void vaciar(String correo) {
        gestor.eliminar(correo);
    }
}
