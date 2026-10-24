package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.model.Cliente;

/** Contrato de la logica de negocio de clientes (equivalente a ILibreria para libros). */
public interface IGestionCliente {

    void guardarCliente(Cliente clienteNuevo);

    void actualizarCliente(Cliente clienteNuevo);

    void eliminarCliente(String correo);

    List<Cliente> listarClientes();

    // Busca por correo o por cédula; devuelve null si no existe
    Cliente buscarCliente(String criterio);

    // Busca únicamente por cédula; devuelve null si no existe
    Cliente buscarClientePorCedula(String cedula);
}
