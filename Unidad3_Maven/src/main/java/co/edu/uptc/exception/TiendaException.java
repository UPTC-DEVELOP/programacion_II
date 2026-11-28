package co.edu.uptc.exception;

// Esta es la excepción base del proyecto. Los controladores la capturan para mostrar mensajes claros.
public class TiendaException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public TiendaException(String mensaje) { super(mensaje); }
    public TiendaException(String mensaje, Throwable causa) { super(mensaje, causa); }
}
