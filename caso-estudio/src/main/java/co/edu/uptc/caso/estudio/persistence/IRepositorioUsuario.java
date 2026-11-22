package co.edu.uptc.caso.estudio.persistence;
import java.util.List;

import co.edu.uptc.caso.estudio.model.Cliente;

public interface IRepositorioUsuario {
    boolean guardarUsuario(Cliente cliente);
    boolean actualizarUsuario(Cliente cliente);
    boolean eliminarUsuario(String id);
    Cliente buscarPorCorreo(String correo);
    Cliente buscarPorId(String id);
    List<Cliente> obtenerTodos();
}