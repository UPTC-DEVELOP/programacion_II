package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.modelo.Cliente;

public interface IGestionCliente {

	void agregarCliente(Cliente cliente);

	List<Cliente> listarClientes();

	Cliente buscarCliente(String identificacion);

	void actualizarCliente(Cliente cliente);

	void eliminarCliente(String identificacion);
}
