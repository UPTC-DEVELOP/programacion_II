package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.interfaces.IGestionCliente;
import co.edu.uptc.tienda.modelo.Cliente;

public class GestionCliente {

	private IGestionCliente gestionCliente;

	public GestionCliente(IGestionCliente gestionCliente) {
		this.gestionCliente = gestionCliente;
	}

	public void agregarCliente(Cliente cliente) {
		gestionCliente.agregar(cliente);
	}

	public List<Cliente> listarClientes() {
		return gestionCliente.listar();
	}

	public Cliente buscarCliente(String identificacion) {
		return gestionCliente.buscar(identificacion);
	}

	public boolean actualizarCliente(Cliente clienteActualizado) {
		return gestionCliente.actualizar(clienteActualizado);
	}

	public boolean eliminarCliente(String identificacion) {
		return gestionCliente.eliminar(identificacion);
	}
}