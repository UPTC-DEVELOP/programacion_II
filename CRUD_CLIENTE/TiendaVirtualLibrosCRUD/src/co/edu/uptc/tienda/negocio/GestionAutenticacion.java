package co.edu.uptc.tienda.negocio;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import co.edu.uptc.tienda.conexion.DataBaseConfig;
import co.edu.uptc.tienda.modelo.Administrador;
import co.edu.uptc.tienda.modelo.Cliente;

public class GestionAutenticacion implements IGestionAutenticacion {

	private Properties adminProps;
	private static final int LONGITUD_MINIMA_CONTRASENA = 8;

	// Formato básico: texto@dominio.extension
	private static final Pattern FORMATO_CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	// si se quiere que solo termine en @uptc.edu,co
	// private static final String DOMINIO_PERMITIDO = "@uptc.edu.co";

	// Mensaje único para no revelar si el usuario existe o no
	private static final String CREDENCIALES_INVALIDAS = "Correo o contraseña incorrectos.";

	private final IRepositorioCliente repositorioCliente;

	public GestionAutenticacion(IRepositorioCliente repositorioCliente) {
		this.repositorioCliente = repositorioCliente;

		adminProps = new Properties();

		try (InputStream input = DataBaseConfig.class.getClassLoader().getResourceAsStream("adminUser.properties")) {

			adminProps.load(input);

		} catch (Exception e) {
			System.err.println("Error: No se puede cargar el archivo (properties) \n:" + e.getMessage());
		}

	}

	@Override
	public Cliente autenticar(String correo, char[] contrasena) {

		String correoAdmin = this.adminProps.getProperty("admin.correo");
		String passwordAdmin = this.adminProps.getProperty("admin.contrasenia");

		try {
			validarFormato(correo, contrasena);

			if (correoAdmin != null && passwordAdmin != null && contrasena != null) {

				char[] passwordAdminChar = passwordAdmin.toCharArray();

				boolean correoCoincide = correo.equals(correoAdmin);
				boolean passwordCoincide = Arrays.equals(contrasena, passwordAdminChar);

				if (correoCoincide && passwordCoincide) {
					return construirAdministradorDesdeProperties();
				}
			}

			Cliente cliente = buscarPorCorreo(correo.trim());
			if (cliente == null || !contrasenaCoincide(cliente, contrasena)) {
				throw new NegocioException(CREDENCIALES_INVALIDAS);
			}

			return cliente;

		} finally {
			if (contrasena != null) {
				Arrays.fill(contrasena, '\0');
			}
		}
	}

	private Administrador construirAdministradorDesdeProperties() {

		int idCliente = Integer.parseInt(this.adminProps.getProperty("admin.id"));
		String primerNombre = this.adminProps.getProperty("admin.primerNombre");
		String otrosNombres = this.adminProps.getProperty("admin.otrosNombres");
		String primerApellido = this.adminProps.getProperty("admin.primerApellido");
		String otrosApellidos = this.adminProps.getProperty("admin.otrosApellidos");
		String telefono = this.adminProps.getProperty("admin.telefono");
		String direccion = this.adminProps.getProperty("admin.direccion");
		String tipoIdentificacion = this.adminProps.getProperty("admin.tipoIdentificacion");
		String identificacion = this.adminProps.getProperty("admin.identificacion");
		String correo = this.adminProps.getProperty("admin.correo");
		String contrasenia = this.adminProps.getProperty("admin.contrasenia");
		int intentosFallidos = Integer.parseInt(this.adminProps.getProperty("admin.intentosFallidos"));

		return new Administrador(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion,
				identificacion, correo, telefono, direccion, idCliente, contrasenia,
				new Timestamp(System.currentTimeMillis()), intentosFallidos);

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
			if (cliente.getCorreoElectronico() != null
					&& cliente.getCorreoElectronico().trim().equalsIgnoreCase(correo)) {
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