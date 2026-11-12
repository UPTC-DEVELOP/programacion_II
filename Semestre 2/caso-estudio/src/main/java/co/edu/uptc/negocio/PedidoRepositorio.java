package co.edu.uptc.negocio;

import java.util.List;
import java.util.Optional;

public interface PedidoRepositorio {

    long siguienteId();

    List<Pedido> obtenerTodos();

    Optional<Pedido> buscarPorId(long id);

    void guardar(Pedido pedido);

    boolean eliminar(long id);
}
