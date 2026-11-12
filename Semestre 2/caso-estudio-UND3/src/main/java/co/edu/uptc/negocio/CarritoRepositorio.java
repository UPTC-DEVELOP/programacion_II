package co.edu.uptc.negocio;

import java.util.Optional;

public interface CarritoRepositorio {

    Optional<Carrito> buscarPorCliente(String correoCliente);

    void guardar(Carrito carrito);

    void eliminar(String correoCliente);
}
