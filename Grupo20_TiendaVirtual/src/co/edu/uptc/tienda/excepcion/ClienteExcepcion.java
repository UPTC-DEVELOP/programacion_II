package co.edu.uptc.tienda.excepcion;

public class ClienteExcepcion extends Exception {

    private static final long serialVersionUID = 1L;

    public ClienteExcepcion(String mensaje) {
        super(mensaje);
    }
}
