package co.edu.uptc.persistence;
import java.util.List;
import co.edu.uptc.model.Cliente;

public interface IRepositorioUsuario {
    boolean guardarUsuario(Cliente cliente);
    boolean actualizarUsuario(Cliente cliente);
    boolean eliminarUsuario(String id);
    Cliente buscarPorCorreo(String correo);
    Cliente buscarPorId(String id);
    List<Cliente> obtenerTodos();
}