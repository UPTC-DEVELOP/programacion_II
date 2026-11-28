package co.uptc.edu.controlador;

// Se lanza cuando el usuario en sesión no tiene permiso para la operación solicitada
public class AccesoDenegadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
