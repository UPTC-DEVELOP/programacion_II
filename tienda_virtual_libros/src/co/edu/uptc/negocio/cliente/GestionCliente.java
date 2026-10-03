package co.edu.uptc.negocio.cliente;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.Cliente;

/**
 * CLASE GestionCliente  (paquete: negocio.cliente)  implements IGestionCliente
 * ---------------------------------------------------------------------------
 * REGLAS DE NEGOCIO de clientes: validaciones, duplicados, autenticación.
 * Depende SOLO del contrato IClienteRepositorio (DIP): no sabe si los datos
 * están en RAM, en un archivo o en una base de datos.
 */
public class GestionCliente implements IGestionCliente {

	/*
	 * Mismas reglas que el login:
	 * - Correo terminado en '@gmail.com'
	 * - Contrasenia: min. 8 caracteres, 1 mayuscula, 1 numero y 1 caracter especial
	 */
	private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._\\-#])[A-Za-z\\d@$!%*?&._\\-#]{8,}$";

	private final IClienteRepositorio persistencia;

	//constructor: inyeccion de dependencias
	public GestionCliente(IClienteRepositorio persistencia) {
	    this.persistencia = persistencia;
	}

	@Override
	public void agregarCliente(Cliente cliente) throws ReglaNegocioException {
	    validarDatos(cliente, true);
	    if (persistencia.buscar(cliente.getIdentificacion()) != null) {
	        throw new ReglaNegocioException("Ya existe un cliente con esa identificación.");
	    }
	    if (persistencia.buscarPorCorreo(cliente.getCorreoElectronico()) != null) {
	        throw new ReglaNegocioException("Ya existe una cuenta con ese correo.");
	    }
	    cliente.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
	    cliente.setIntentosFallidos(0);
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
	public Cliente buscarClientePorCorreo(String correo) {
	    return persistencia.buscarPorCorreo(correo.trim());
	}

	/**
	 * La identificación y el tipo de cliente no cambian; si la contraseña
	 * viene vacía se conserva la anterior.
	 */
	@Override
	public void actualizarCliente(Cliente cliente) throws ReglaNegocioException {
	    Cliente existente = persistencia.buscar(cliente.getIdentificacion());
	    if (existente == null) {
	        throw new ReglaNegocioException("El cliente ya no existe.");
	    }
	    validarDatos(cliente, false);
	    Cliente otro = persistencia.buscarPorCorreo(cliente.getCorreoElectronico());
	    if (otro != null && !otro.getIdentificacion().equals(cliente.getIdentificacion())) {
	        throw new ReglaNegocioException("Ya existe otra cuenta con ese correo.");
	    }
	    cliente.setIdCliente(existente.getIdCliente());
	    cliente.setFechaRegistro(existente.getFechaRegistro());
	    cliente.setIntentosFallidos(existente.getIntentosFallidos());
	    if (vacio(cliente.getContrasenia())) {
	        cliente.setContrasenia(existente.getContrasenia());
	    }
	    persistencia.actualizar(cliente);
	}

	@Override
	public void eliminarCliente(String identificacion) throws ReglaNegocioException {
	    if (persistencia.buscar(identificacion) == null) {
	        throw new ReglaNegocioException("El cliente ya no existe.");
	    }
	    persistencia.eliminar(identificacion);
	}

	/**
	 * Inicio de sesion del cliente.
	 * @return el cliente si el correo y la contrasenia coinciden, null en otro caso.
	 */
	@Override
	public Cliente autenticar(String correo, String contrasenia) {
	    Cliente cliente = persistencia.buscarPorCorreo(correo.trim());
	    if (cliente == null) {
	        return null;
	    }
	    if (!cliente.getContrasenia().equals(contrasenia)) {
	        cliente.setIntentosFallidos(cliente.getIntentosFallidos() + 1);
	        persistencia.actualizar(cliente);
	        return null;
	    }
	    cliente.setIntentosFallidos(0);
	    persistencia.actualizar(cliente);
	    return cliente;
	}

	public static boolean validarCorreo(String correo) {
	    return correo != null && Pattern.matches(EMAIL_REGEX, correo.trim());
	}

	public static boolean validarContrasenia(String clave) {
	    return clave != null && Pattern.matches(PASSWORD_REGEX, clave);
	}

	private void validarDatos(Cliente cliente, boolean claveObligatoria) throws ReglaNegocioException {
	    if (vacio(cliente.getPrimerNombre()) || vacio(cliente.getPrimerApellido())) {
	        throw new ReglaNegocioException("El primer nombre y el primer apellido son obligatorios.");
	    }
	    if (vacio(cliente.getTipoIdentificacion()) || vacio(cliente.getIdentificacion())) {
	        throw new ReglaNegocioException("El tipo y número de identificación son obligatorios.");
	    }
	    if (!cliente.getIdentificacion().matches("\\d{5,12}")) {
	        throw new ReglaNegocioException("La identificación debe tener entre 5 y 12 dígitos.");
	    }
	    if (!validarCorreo(cliente.getCorreoElectronico())) {
	        throw new ReglaNegocioException("Correo inválido (debe terminar en @gmail.com).");
	    }
	    if (!vacio(cliente.getCelular()) && !cliente.getCelular().matches("\\d{10}")) {
	        throw new ReglaNegocioException("El celular debe tener 10 dígitos.");
	    }
	    boolean hayClave = !vacio(cliente.getContrasenia());
	    if ((claveObligatoria || hayClave) && !validarContrasenia(cliente.getContrasenia())) {
	        throw new ReglaNegocioException(
	                "Contraseña débil: mín. 8 caracteres, 1 mayúscula, 1 número y 1 carácter especial.");
	    }
	}

	private static boolean vacio(String texto) {
	    return texto == null || texto.trim().isEmpty();
	}
}
