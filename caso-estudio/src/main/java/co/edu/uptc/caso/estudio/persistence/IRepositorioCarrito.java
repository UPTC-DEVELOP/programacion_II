package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;

// Contrato de persistencia para el carrito de compras por cliente
public interface IRepositorioCarrito {

    Carrito obtenerCarritoPorCliente(String clienteId);

    boolean guardarCarrito(Carrito carrito);

    boolean vaciarCarrito(String clienteId);
}