package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.interfaces.IGestionCliente;
import co.edu.uptc.tienda.modelo.Cliente;

public class GestionCliente {

	private IGestionCliente gestionCliente;
	
	//constructorinyeccion de dependencias
	public GestionCliente(IGestionCliente gestionCliente) {
	    this.gestionCliente = gestionCliente;
	}
	
	public void agregarCliente(Cliente cliente) {
	    gestionCliente.agregarCliente(cliente);
	}
	
	public List<Cliente> listarClientes() {
	    return gestionCliente.listarClientes();
	}
	
	public Cliente buscarCliente(String identificacion) {
	    return gestionCliente.buscarCliente(identificacion);
	}
	
	public boolean actualizarCliente(Cliente clienteActualizado) {
	    return gestionCliente.actualizarCliente(clienteActualizado);
	}
	
	public boolean eliminarCliente(String identificacion) {
	    return gestionCliente.eliminarCliente(identificacion);
	}
}