package co.edu.uptc.tienda.interfaces;

import co.edu.uptc.tienda.modelo.Cliente;
import java.util.List;

public interface IGestionCliente {
	
	  void agregarCliente(Cliente cliente);
	  List<Cliente> listarClientes();
	  Cliente buscarCliente(String identificacion);
	  boolean actualizarCliente(Cliente clienteActualizado);
	  boolean eliminarCliente(String identificacion);

}

