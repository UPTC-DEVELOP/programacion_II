package co.edu.uptc.tienda.modelo;

import java.util.List;
import java.util.ArrayList;

public class Clientes {

	private List<Cliente> listaClientes;

	public Clientes() {
		listaClientes = new ArrayList<>();
	}

	public void agregarCliente(Cliente cliente) {
		listaClientes.add(cliente);
	}

	public List<Cliente> listarClientes() {
		return listaClientes;
	}

	public Cliente buscarCliente(String identificacion) {
		for (Cliente cliente : listaClientes) {
			if (cliente.getIdentificacion().equals(identificacion)) {
				return cliente;
			}
		}

		return null;
	}

	public boolean eliminarCliente(String identificacion) {
		for (Cliente cliente : listaClientes) {
			if (cliente.getIdentificacion().equals(identificacion)) {
				listaClientes.remove(cliente);
				return true;
			}
		}

		return false;
	}

	public boolean actualizarCliente(Cliente clienteActualizado) {
		for (int i = 0; i < listaClientes.size(); i++) {
			Cliente clienteActual = listaClientes.get(i);
			if (clienteActual.getIdentificacion().equals(clienteActualizado.getIdentificacion())) {

				clienteActualizado.setIdCliente(clienteActual.getIdCliente());
				clienteActualizado.setFechaRegistro(clienteActual.getFechaRegistro());
				clienteActualizado.setIntentosFallidos(clienteActual.getIntentosFallidos());

				if (clienteActualizado.getContrasenia() == null
						|| clienteActualizado.getContrasenia().trim().isEmpty()) {
					clienteActualizado.setContrasenia(clienteActual.getContrasenia());
				}

				listaClientes.set(i, clienteActualizado);
				return true;
			}
		}
		return false;
	}

}
