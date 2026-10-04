package co.edu.uptc.tienda.modelo;

import java.util.List;
import java.util.ArrayList;

public class Clientes {

	private List<Cliente> listaClientes;

	// Construc
	public Clientes() { // Crea lista
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

	/**
	 * Actualiza los datos de un cliente existente buscando por su identificación.
	 * Conserva el identificador numérico interno (ID) y la fecha de registro
	 * original.
	 * 
	 * @param clienteActualizado Objeto con los nuevos datos a persistir.
	 * @return true si el cliente fue encontrado y actualizado, false en caso
	 *         contrario.
	 */
	public boolean actualizarCliente(Cliente clienteActualizado) {
		for (int i = 0; i < listaClientes.size(); i++) {
			Cliente clienteActual = listaClientes.get(i);
			if (clienteActual.getIdentificacion().equals(clienteActualizado.getIdentificacion())) {
				// Preservar datos de auditoría y clave interna generados en el registro inicial
				clienteActualizado.setIdCliente(clienteActual.getIdCliente());
				clienteActualizado.setFechaRegistro(clienteActual.getFechaRegistro());
				clienteActualizado.setIntentosFallidos(clienteActual.getIntentosFallidos());

				// Si no se proporcionó nueva contraseña en la actualización, conservar la
				// anterior
				if (clienteActualizado.getContrasenia() == null
						|| clienteActualizado.getContrasenia().trim().isEmpty()) {
					clienteActualizado.setContrasenia(clienteActual.getContrasenia());
				}

				// Reemplazar la instancia en la lista para soportar polimorfismo
				// (regular/premium)
				listaClientes.set(i, clienteActualizado);
				return true;
			}
		}
		return false;
	}

}
