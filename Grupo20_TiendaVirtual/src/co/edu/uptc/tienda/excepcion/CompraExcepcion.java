package co.edu.uptc.tienda.excepcion;

public class CompraExcepcion extends Exception {

    private static final long serialVersionUID = 1L;

    public CompraExcepcion(String mensaje) {
        super(mensaje);
    }
}
