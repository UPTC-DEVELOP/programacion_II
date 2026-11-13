package co.edu.uptc.persistence;

import java.util.List;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Pedido;

public interface PedidoRepository {
    List<Pedido> listar() throws PersistenciaException;
    List<Pedido> listarPorCliente(String correo) throws PersistenciaException;
    long siguienteId() throws PersistenciaException;
    void guardar(Pedido pedido) throws PersistenciaException;
    void eliminar(long id) throws PersistenciaException;
}
