package co.edu.uptc.exception;

public class CredencialesInvalidasException extends TiendaException {
    private static final long serialVersionUID = 1L;
    public CredencialesInvalidasException(String mensaje) { super(mensaje); }
}
