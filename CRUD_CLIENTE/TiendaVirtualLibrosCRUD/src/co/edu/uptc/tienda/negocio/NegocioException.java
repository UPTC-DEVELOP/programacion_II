package co.edu.uptc.tienda.negocio;


public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NegocioException(String mensaje) {
		super(mensaje);
	}
}
