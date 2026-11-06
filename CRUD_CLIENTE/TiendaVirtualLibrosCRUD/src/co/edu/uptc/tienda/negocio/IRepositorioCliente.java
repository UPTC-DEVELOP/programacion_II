package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.modelo.Cliente;

/**
 * Contrato de almacenamiento de clientes. Lo define el negocio y lo implementa
 * la persistencia (memoria local, MySQL, etc.).
 */
public interface IRepositorioCliente {

	void guardar(Cliente cliente);

	List<Cliente> listar();

	Cliente buscarPorIdentificacion(String identificacion);

	boolean actualizar(Cliente cliente);

	boolean eliminar(String identificacion);
}
