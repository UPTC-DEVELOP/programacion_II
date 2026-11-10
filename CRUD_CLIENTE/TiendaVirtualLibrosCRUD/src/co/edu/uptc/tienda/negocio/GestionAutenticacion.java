package co.edu.uptc.tienda.negocio;

import java.util.Arrays;
import java.util.regex.Pattern;

import co.edu.uptc.tienda.modelo.Cliente;

/**
 * Valida el inicio de sesión contra los clientes registrados. El usuario es el
 * correo electrónico del cliente.
 */
public class GestionAutenticacion implements IGestionAutenticacion {

	private static final int LONGITUD_MINIMA_CONTRASENA = 8;

	// Formato básico: texto@dominio.extension
	private static final Pattern FORMATO_CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	// si se quiere que solo termine en @uptc.edu,co
	//private static final String DOMINIO_PERMITIDO = "@uptc.edu.co";
	
	

	// Mensaje único para no revelar si el usuario existe o no
	private static final String CREDENCIALES_INVALIDAS = "Correo o contraseña incorrectos.";

	private final IRepositorioCliente repositorioCliente;

	public GestionAutenticacion(IRepositorioCliente repositorioCliente) {
		this.repositorioCliente = repositorioCliente;
	}

	@Override
	public Cliente autenticar(String correo, char[] contrasena) {
		try {
			validarFormato(correo, contrasena);

			Cliente cliente = buscarPorCorreo(correo.trim());
			if (cliente == null || !contrasenaCoincide(cliente, contrasena)) {
				throw new NegocioException(CREDENCIALES_INVALIDAS);
			}

			return cliente;

		} finally {
			// La contraseña no debe quedar en memoria más tiempo del necesario
			if (contrasena != null) {
				Arrays.fill(contrasena, '\0');
			}
		}
	}

	private void validarFormato(String correo, char[] contrasena) {
		if (correo == null || correo.trim().isEmpty()) {
			throw new NegocioException("Ingrese el Correo electrónico.");
		}
//	// si se quiere que solo termine en @uptc.edu,co
		if (!FORMATO_CORREO.matcher(correo.trim()).matches()) {
			throw new NegocioException("Ingrese un Correo electrónico válido (ejemplo: usuario@dominio.com).");
		}

		if (contrasena == null || contrasena.length == 0) {
			throw new NegocioException("Ingrese la Contraseña.");
		}

		if (contrasena.length < LONGITUD_MINIMA_CONTRASENA) {
			throw new NegocioException(
					"La contraseña debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.");
		}
	}

	private Cliente buscarPorCorreo(String correo) {
		for (Cliente cliente : repositorioCliente.listar()) {
			if (cliente.getCorreoElectronico() != null && cliente.getCorreoElectronico().trim().equalsIgnoreCase(correo)) {
				return cliente;
			}
		}
		return null;
	}

	private boolean contrasenaCoincide(Cliente cliente, char[] contrasena) {
		String registrada = cliente.getContrasenia();
		if (registrada == null || registrada.isEmpty()) {
			return false;
		}

		char[] esperada = registrada.toCharArray();
		try {
			return Arrays.equals(esperada, contrasena);
		} finally {
			Arrays.fill(esperada, '\0');
		}
	}
}
