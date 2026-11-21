package co.edu.uptc.persistence;

import co.edu.uptc.model.Carrito;

public interface IRepositorioCompra {
    boolean registrarCompra(String clienteId, Carrito carrito);
}