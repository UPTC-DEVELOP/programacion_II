package co.edu.uptc.exception;

public class StockInsuficienteException extends TiendaException {
    private static final long serialVersionUID = 1L;
    public StockInsuficienteException(String mensaje) { super(mensaje); }
}
