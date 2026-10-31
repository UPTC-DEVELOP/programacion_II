package co.edu.uptc.libreria.interfaces;
import java.util.List;

import co.edu.uptc.libreria.modelo.Cliente;

/**
 * Contrato de persistencia de clientes. Hoy lo implementa una version en
 * memoria; mas adelante se puede crear otra con JDBC sin tocar la capa de
 * negocio.
 */
public interface IGestionCliente {

	public void guardar(Cliente cliente);

	public void actualizar(Cliente cliente);

	public void eliminar(String correo);

	public Cliente buscar(String correo);

	public List<Cliente> listar();
}