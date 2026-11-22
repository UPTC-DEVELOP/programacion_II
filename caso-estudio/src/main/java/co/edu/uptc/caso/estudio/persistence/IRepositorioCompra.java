package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Compra;
import java.util.Collections;
import java.util.List;

public interface IRepositorioCompra {
    boolean registrarCompra(String clienteId, Carrito carrito);

    default List<Compra> obtenerComprasPorCliente(String clienteId) {
        return Collections.emptyList();
    }
}