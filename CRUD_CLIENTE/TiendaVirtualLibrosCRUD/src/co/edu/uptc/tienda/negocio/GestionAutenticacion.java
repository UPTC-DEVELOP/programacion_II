package co.edu.uptc.tienda.negocio;

// TODO: validar contra los usuarios registrados cuando exista la persistencia en BD
public class GestionAutenticacion implements IGestionAutenticacion {

	private static final int LONGITUD_MINIMA_CONTRASENA = 8;

	@Override
	public void autenticar(String usuario, char[] contrasena) {
		if (usuario == null || usuario.trim().isEmpty()) {
			throw new NegocioException("Ingrese el Usuario.");
		}

		if (contrasena == null || contrasena.length < LONGITUD_MINIMA_CONTRASENA) {
			throw new NegocioException(
					"La contraseña debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.");
		}
	}
}
