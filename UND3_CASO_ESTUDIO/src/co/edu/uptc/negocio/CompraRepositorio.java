package co.edu.uptc.negocio;

import java.util.List;
import java.util.Optional;

public interface CompraRepositorio {

    long siguienteId();

    void guardar(Compra compra);

    Optional<Compra> buscarPorId(long id);

    List<Compra> obtenerTodas();

    List<Compra> obtenerPorCliente(String correoCliente);
}
