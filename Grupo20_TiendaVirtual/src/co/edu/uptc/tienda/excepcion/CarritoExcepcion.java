package co.edu.uptc.tienda.excepcion;

public class CarritoExcepcion extends Exception {

    private static final long serialVersionUID = 1L;

    public CarritoExcepcion(String mensaje) {
        super(mensaje);
    }
}