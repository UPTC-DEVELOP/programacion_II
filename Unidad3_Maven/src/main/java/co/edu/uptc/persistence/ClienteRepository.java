
package co.edu.uptc.persistence;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Cliente;

public interface ClienteRepository {

    List<Cliente> listar() throws PersistenciaException;

    Optional<Cliente> buscarPorCorreo(String correo) throws PersistenciaException;

    void guardar(Cliente cliente) throws PersistenciaException;
}

