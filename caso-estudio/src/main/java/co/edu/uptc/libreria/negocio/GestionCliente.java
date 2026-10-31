package co.edu.uptc.libreria.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.libreria.interfaces.IGestionCliente;
import co.edu.uptc.libreria.interfaces.IRegistroOperaciones;
import co.edu.uptc.libreria.modelo.Cliente;

public class GestionCliente {

	private static final String REGEX_CORREO = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
	private static final String REGEX_TELEFONO = "\\d{7,10}";

	private IGestionCliente persistencia;
	private IRegistroOperaciones registro;

	public GestionCliente(IGestionCliente persistencia, IRegistroOperaciones registro) {
		super();
		this.persistencia = persistencia;
		this.registro = registro;
	}

	public void registrarCliente(Cliente cliente) {
		normalizar(cliente);
		validar(cliente);
		if (persistencia.buscar(cliente.getCorreo()) != null) {
			throw new IllegalArgumentException("Ya existe un cliente registrado con el correo " + cliente.getCorreo());
		}
		persistencia.guardar(cliente);
		registro.registrar("REGISTRAR_CLIENTE", cliente.getCorreo() + " (" + cliente.getTipo() + ")");
	}

	public void actualizarCliente(Cliente cliente) {
		normalizar(cliente);
		validar(cliente);
		if (persistencia.buscar(cliente.getCorreo()) == null) {
			throw new IllegalArgumentException("No existe un cliente con el correo " + cliente.getCorreo());
		}
		persistencia.actualizar(cliente);
		registro.registrar("ACTUALIZAR_CLIENTE", cliente.getCorreo() + " (" + cliente.getTipo() + ")");
	}

	public void eliminarCliente(String correo) {
		String clave = normalizarCorreo(correo);
		if (persistencia.buscar(clave) == null) {
			throw new IllegalArgumentException("No existe un cliente con el correo " + clave);
		}
		persistencia.eliminar(clave);
		registro.registrar("ELIMINAR_CLIENTE", clave);
	}

	public Cliente buscarCliente(String correo) {
		return persistencia.buscar(normalizarCorreo(correo));
	}

	public List<Cliente> listarClientes() {
		return persistencia.listar();
	}

	/** Filtra por coincidencia parcial en nombre o correo; sin texto devuelve todos. */
	public List<Cliente> filtrarClientes(String texto) {
		if (texto == null || texto.isBlank()) {
			return listarClientes();
		}
		String criterio = texto.trim().toLowerCase();
		List<Cliente> resultado = new ArrayList<Cliente>();
		for (Cliente cliente : listarClientes()) {
			boolean porNombre = cliente.getNombreCompleto().toLowerCase().contains(criterio);
			boolean porCorreo = cliente.getCorreo().contains(criterio);
			if (porNombre || porCorreo) {
				resultado.add(cliente);
			}
		}
		return resultado;
	}

	private String normalizarCorreo(String correo) {
		return correo == null ? "" : correo.trim().toLowerCase();
	}

	private void normalizar(Cliente cliente) {
		if (cliente == null) {
			throw new IllegalArgumentException("No hay información del cliente");
		}
		cliente.setNombreCompleto(cliente.getNombreCompleto() == null ? "" : cliente.getNombreCompleto().trim());
		cliente.setCorreo(normalizarCorreo(cliente.getCorreo()));
		cliente.setDireccionEnvio(cliente.getDireccionEnvio() == null ? "" : cliente.getDireccionEnvio().trim());
		cliente.setTelefono(cliente.getTelefono() == null ? "" : cliente.getTelefono().trim());
	}

	private void validar(Cliente cliente) {
		if (cliente.getNombreCompleto().isEmpty()) {
			throw new IllegalArgumentException("El nombre completo es obligatorio");
		}
		if (cliente.getCorreo().isEmpty()) {
			throw new IllegalArgumentException("El correo electrónico es obligatorio");
		}
		if (!cliente.getCorreo().matches(REGEX_CORREO)) {
			throw new IllegalArgumentException("El correo electrónico no tiene un formato válido");
		}
		if (cliente.getDireccionEnvio().isEmpty()) {
			throw new IllegalArgumentException("La dirección de envío es obligatoria");
		}
		if (cliente.getTelefono().isEmpty()) {
			throw new IllegalArgumentException("El teléfono de contacto es obligatorio");
		}
		if (!cliente.getTelefono().matches(REGEX_TELEFONO)) {
			throw new IllegalArgumentException("El teléfono debe tener entre 7 y 10 dígitos");
		}
		if (cliente.getTipo() == null) {
			throw new IllegalArgumentException("Debe seleccionar el tipo de cliente");
		}
	}

}