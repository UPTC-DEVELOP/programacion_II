package co.edu.uptc.tienda.negocio;

/**
 * Servicio de inicio de sesión. Lanza {@link NegocioException} si las
 * credenciales no son válidas.
 */
public interface IGestionAutenticacion {

	void autenticar(String usuario, char[] contrasena);
}
