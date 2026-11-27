package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Cliente;
import java.util.List;

// Contrato de persistencia para operaciones CRUD y búsqueda de clientes
public interface IRepositorioUsuario {

    boolean guardarUsuario(Cliente cliente);

    boolean actualizarUsuario(Cliente cliente);

    boolean eliminarUsuario(String id);

    Cliente buscarPorCorreo(String correo);

    Cliente buscarPorId(String id);

    List<Cliente> obtenerTodos();
}