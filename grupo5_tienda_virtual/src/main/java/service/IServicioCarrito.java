package service;

import model.CarritoCompras;

public interface IServicioCarrito {

    CarritoCompras cargar(String correo);

    void guardar(CarritoCompras carrito, String correo);

    void vaciar(String correo);
}
