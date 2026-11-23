package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Compra;
import java.util.Collections;
import java.util.List;
import co.edu.uptc.caso.estudio.model.Cliente;

public interface IRepositorioCompra {
    boolean registrarCompra(Cliente cliente, Carrito carrito, String metodoPago);

    default List<Compra> obtenerComprasPorCliente(String clienteId) {
        return Collections.emptyList();
    }
}