package excepciones;

public class ExcepcionValidacion extends Exception {
    private static final long serialVersionUID = 1L;

    public ExcepcionValidacion(String mensaje) {
        super(mensaje);
    }
}
