package co.edu.uptc.tienda.persistencia;

import java.sql.Timestamp;
import java.util.List;

import co.edu.uptc.tienda.interfaces.IGestionCliente;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Clientes;

/**
 * Implementación de persistencia local en memoria para la gestión de clientes.
 * Asigna identificadores autoincrementables y estampas de tiempo de creación.
 */
public class LocalCliente implements IGestionCliente {
	
	private Clientes clientes;
	private int siguienteId;

	public LocalCliente() {
	    clientes = new Clientes();
	    siguienteId = 1;
	}

	@Override
	public void agregarCliente(Cliente cliente) {
		cliente.setIdCliente(siguienteId);
		cliente.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
		cliente.setIntentosFallidos(0);
		siguienteId++;
		clientes.agregarCliente(cliente);
	}

	@Override
	public List<Cliente> listarClientes() {
		return clientes.listarClientes();
	}

	@Override
	public Cliente buscarCliente(String identificacion) {
		return clientes.buscarCliente(identificacion);
	}

	@Override
	public boolean actualizarCliente(Cliente clienteActualizado) {
		return clientes.actualizarCliente(clienteActualizado);
	}

	@Override
	public boolean eliminarCliente(String identificacion) {
		return clientes.eliminarCliente(identificacion);
	}
}