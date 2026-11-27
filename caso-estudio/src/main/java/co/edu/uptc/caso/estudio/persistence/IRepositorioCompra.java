package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.Compra;
import java.util.Collections;
import java.util.List;

// Contrato de persistencia para registrar y consultar compras realizadas
public interface IRepositorioCompra {

    boolean registrarCompra(Cliente cliente, Carrito carrito, String metodoPago);

    // Método por defecto para compatibilidad con implementaciones que aún no exponen historial
    default List<Compra> obtenerComprasPorCliente(String clienteId) {
        return Collections.emptyList();
    }
}