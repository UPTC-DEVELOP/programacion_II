package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.modelo.Cliente;

/**
 * Servicio de inicio de sesión. Lanza {@link NegocioException} si las
 * credenciales no son válidas.
 */
public interface IGestionAutenticacion {

	/**
	 * Valida las credenciales contra los clientes registrados.
	 *
	 * @param correo     correo electrónico del cliente
	 * @param contrasena contraseña digitada; se limpia de memoria al terminar
	 * @return el cliente que inició sesión
	 */
	Cliente autenticar(String correo, char[] contrasena);
}
