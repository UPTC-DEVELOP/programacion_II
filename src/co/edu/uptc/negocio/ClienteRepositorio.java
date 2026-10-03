package co.edu.uptc.negocio;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositorio {

    List<Cliente> obtenerTodos();

    Optional<Cliente> buscarPorCorreo(String correoElectronico);

    void guardar(Cliente cliente);

    boolean eliminar(String correoElectronico);
}
