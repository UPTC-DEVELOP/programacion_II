package co.uptc.edu.modelo;

public class excepcionLibro extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public excepcionLibro(String mensaje) {
        super(mensaje);
    }
}