package co.edu.uptc.tienda.persistencia;

import java.sql.Timestamp;
import java.util.List;

import co.edu.uptc.tienda.interfaces.IGestionable;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Clientes;

/**
 * Implementación de persistencia local en memoria para la gestión de clientes.
 * Asigna identificadores autoincrementables y estampas de tiempo de creación.
 */
public class LocalCliente implements IGestionable<Cliente> {

	private Clientes clientes;
	private int siguienteId;

	public LocalCliente() {
		clientes = new Clientes();
		siguienteId = 1;
	}

	@Override
	public void agregar(Cliente cliente) {
		cliente.setIdCliente(siguienteId);
		cliente.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
		cliente.setIntentosFallidos(0);
		siguienteId++;
		clientes.agregarCliente(cliente);
	}

	@Override
	public List<Cliente> listar() {
		return clientes.listarClientes();
	}

	@Override
	public Cliente buscar(String identificacion) {
		return clientes.buscarCliente(identificacion);
	}

	@Override
	public boolean actualizar(Cliente clienteActualizado) {
		return clientes.actualizarCliente(clienteActualizado);
	}

	@Override
	public boolean eliminar(String identificacion) {
		return clientes.eliminarCliente(identificacion);
	}

}
