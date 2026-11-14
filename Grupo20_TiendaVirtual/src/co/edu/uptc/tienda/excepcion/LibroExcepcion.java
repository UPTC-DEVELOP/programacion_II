package co.edu.uptc.tienda.excepcion;

public class LibroExcepcion extends Exception {

    private static final long serialVersionUID = 1L;

    public LibroExcepcion(String mensaje) {
        super(mensaje);
    }
}
