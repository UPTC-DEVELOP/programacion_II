package co.edu.uptc.interfaces;

import co.edu.uptc.modelo.Cliente;
import java.util.List;

public interface IGestionCliente {
    boolean agregarCliente(Cliente cliente);
    Cliente buscarPorIdentificacion(String identificacion);
    Cliente buscarPorCorreo(String correo);
    List<Cliente> obtenerTodosLosClientes();
    boolean actualizarCliente(Cliente cliente);
    boolean eliminarCliente(String identificacion);
}
