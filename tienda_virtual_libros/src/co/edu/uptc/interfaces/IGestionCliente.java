package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.Cliente;

/**
 * Interfaz de negocio de los clientes.
 * La GUI usa esta interfaz y no la clase GestionCliente directamente.
 */
public interface IGestionCliente {

    void agregarCliente(Cliente cliente) throws ReglaNegocioException;

    void actualizarCliente(Cliente cliente) throws ReglaNegocioException;

    void eliminarCliente(String identificacion) throws ReglaNegocioException;

    List<Cliente> listarClientes();

    Cliente buscarCliente(String identificacion);
}
