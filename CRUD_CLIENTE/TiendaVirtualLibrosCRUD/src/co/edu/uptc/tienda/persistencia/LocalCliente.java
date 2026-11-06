package co.edu.uptc.tienda.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.negocio.IRepositorioCliente;

/**
 * Repositorio de clientes en memoria.
 */
public class LocalCliente implements IRepositorioCliente {

	private final List<Cliente> clientes;
	private int siguienteId;

	public LocalCliente() {
		clientes = new ArrayList<>();
		siguienteId = 1;
	}

	@Override
	public void guardar(Cliente cliente) {
		cliente.setIdCliente(siguienteId);
		siguienteId++;
		clientes.add(cliente);
	}

	@Override
	public List<Cliente> listar() {
		return new ArrayList<>(clientes);
	}

	@Override
	public Cliente buscarPorIdentificacion(String identificacion) {
		for (Cliente cliente : clientes) {
			if (cliente.getIdentificacion().equals(identificacion)) {
				return cliente;
			}
		}
		return null;
	}

	@Override
	public boolean actualizar(Cliente clienteActualizado) {
		for (int i = 0; i < clientes.size(); i++) {
			if (clientes.get(i).getIdentificacion().equals(clienteActualizado.getIdentificacion())) {
				clientes.set(i, clienteActualizado);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean eliminar(String identificacion) {
		return clientes.removeIf(cliente -> cliente.getIdentificacion().equals(identificacion));
	}

}
