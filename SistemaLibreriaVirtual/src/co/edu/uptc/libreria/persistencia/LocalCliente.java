package co.edu.uptc.libreria.persistencia;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.uptc.libreria.interfaces.IGestionCliente;
import co.edu.uptc.libreria.modelo.Cliente;

public class LocalCliente implements IGestionCliente {

	// la llave es el correo; LinkedHashMap conserva el orden de registro
	private Map<String, Cliente> clientes;

	public LocalCliente() {
		super();
		clientes = new LinkedHashMap<String, Cliente>();
	}

	@Override
	public void guardar(Cliente cliente) {
		clientes.put(cliente.getCorreo(), cliente);
	}

	@Override
	public void actualizar(Cliente cliente) {
		// put sobre una llave existente reemplaza el valor sin cambiar la posicion
		clientes.put(cliente.getCorreo(), cliente);
	}

	@Override
	public void eliminar(String correo) {
		clientes.remove(correo);
	}

	@Override
	public Cliente buscar(String correo) {
		return clientes.get(correo);
	}

	@Override
	public List<Cliente> listar() {
		return new ArrayList<Cliente>(clientes.values());
	}

}