package co.edu.uptc.tienda.negocio;

import java.sql.Timestamp;
import java.util.List;

import co.edu.uptc.tienda.modelo.Cliente;

public class GestionCliente implements IGestionCliente {

	private final IRepositorioCliente repositorio;
	private final ValidadorCliente validador;

	public GestionCliente(IRepositorioCliente repositorio) {
		this.repositorio = repositorio;
		this.validador = new ValidadorCliente();
	}

	@Override
	public void agregarCliente(Cliente cliente) {
		validador.validar(cliente);

		if (repositorio.buscarPorIdentificacion(cliente.getIdentificacion()) != null) {
			throw new NegocioException(
					"Ya existe un cliente registrado con la identificación: " + cliente.getIdentificacion());
		}

		cliente.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
		cliente.setIntentosFallidos(0);
		repositorio.guardar(cliente);
	}

	@Override
	public List<Cliente> listarClientes() {
		return repositorio.listar();
	}

	@Override
	public Cliente buscarCliente(String identificacion) {
		return repositorio.buscarPorIdentificacion(identificacion);
	}

	@Override
	public void actualizarCliente(Cliente clienteActualizado) {
		validador.validar(clienteActualizado);

		Cliente clienteActual = repositorio.buscarPorIdentificacion(clienteActualizado.getIdentificacion());
		if (clienteActual == null) {
			throw new NegocioException("No existe un cliente con la identificación: "
					+ clienteActualizado.getIdentificacion());
		}

		// Datos que el usuario no puede modificar desde el formulario
		clienteActualizado.setIdCliente(clienteActual.getIdCliente());
		clienteActualizado.setFechaRegistro(clienteActual.getFechaRegistro());
		clienteActualizado.setIntentosFallidos(clienteActual.getIntentosFallidos());

		if (clienteActualizado.getContrasenia() == null || clienteActualizado.getContrasenia().trim().isEmpty()) {
			clienteActualizado.setContrasenia(clienteActual.getContrasenia());
		}

		if (!repositorio.actualizar(clienteActualizado)) {
			throw new NegocioException("No se pudo actualizar la información del cliente.");
		}
	}

	@Override
	public void eliminarCliente(String identificacion) {
		if (!repositorio.eliminar(identificacion)) {
			throw new NegocioException("No existe un cliente con la identificación: " + identificacion);
		}
	}
}
