package co.edu.uptc.negocio.cliente;

import java.sql.Timestamp;
import java.util.List;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.Cliente;

/**
 * Clase de negocio de los clientes.
 * Revisa que los datos esten bien antes de guardarlos.
 * Para guardar usa la interfaz IClienteRepositorio, asi no sabe
 * si los datos estan en una lista o en una base de datos.
 */
public class GestionCliente implements IGestionCliente {

	private IClienteRepositorio persistencia;

	//constructor: inyeccion de dependencias
	public GestionCliente(IClienteRepositorio persistencia) {
	    this.persistencia = persistencia;
	}

	@Override
	public void agregarCliente(Cliente cliente) throws ReglaNegocioException {
	    validarDatos(cliente);
	    if (cliente.getContrasenia().isEmpty()) {
	        throw new ReglaNegocioException("La contraseña es obligatoria.");
	    }
	    if (persistencia.buscar(cliente.getIdentificacion()) != null) {
	        throw new ReglaNegocioException("Ya existe un cliente con esa identificación.");
	    }
	    if (persistencia.buscarPorCorreo(cliente.getCorreoElectronico()) != null) {
	        throw new ReglaNegocioException("Ya existe un cliente con ese correo.");
	    }
	    cliente.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
	    persistencia.guardar(cliente);
	}

	@Override
	public List<Cliente> listarClientes() {
	    return persistencia.listar();
	}

	@Override
	public Cliente buscarCliente(String identificacion) {
	    return persistencia.buscar(identificacion);
	}

	@Override
	public void actualizarCliente(Cliente cliente) throws ReglaNegocioException {
	    Cliente existente = persistencia.buscar(cliente.getIdentificacion());
	    if (existente == null) {
	        throw new ReglaNegocioException("El cliente no existe.");
	    }
	    validarDatos(cliente);

	    //se conservan los datos que no se editan en el formulario
	    cliente.setIdCliente(existente.getIdCliente());
	    cliente.setFechaRegistro(existente.getFechaRegistro());
	    if (cliente.getContrasenia().isEmpty()) {
	        cliente.setContrasenia(existente.getContrasenia());
	    }
	    persistencia.actualizar(cliente);
	}

	@Override
	public void eliminarCliente(String identificacion) throws ReglaNegocioException {
	    if (persistencia.buscar(identificacion) == null) {
	        throw new ReglaNegocioException("El cliente no existe.");
	    }
	    persistencia.eliminar(identificacion);
	}

	//campos obligatorios del formulario
	private void validarDatos(Cliente cliente) throws ReglaNegocioException {
	    if (cliente.getPrimerNombre().isEmpty() || cliente.getPrimerApellido().isEmpty()) {
	        throw new ReglaNegocioException("El nombre y el apellido son obligatorios.");
	    }
	    if (cliente.getIdentificacion().isEmpty()) {
	        throw new ReglaNegocioException("La identificación es obligatoria.");
	    }
	    if (!cliente.getCorreoElectronico().contains("@")) {
	        throw new ReglaNegocioException("El correo no es válido.");
	    }
	}
}
