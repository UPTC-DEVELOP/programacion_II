package co.edu.uptc.tienda.negocio;

/**
 * Se lanza cuando una operación incumple una regla de negocio. La GUI muestra
 * su mensaje al usuario.
 */
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NegocioException(String mensaje) {
		super(mensaje);
	}
}
