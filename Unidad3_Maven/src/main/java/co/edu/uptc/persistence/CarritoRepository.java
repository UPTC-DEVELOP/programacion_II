package co.edu.uptc.persistence;

import java.util.Optional;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Carrito;

public interface CarritoRepository {
    Optional<Carrito> buscarPorCliente(String correo) throws PersistenciaException;
    void guardar(Carrito carrito) throws PersistenciaException;
    void eliminar(String correo) throws PersistenciaException;
}
