package negocio;

/**
 * Cliente sin beneficios de descuento.
 */
public class ClienteRegular extends Cliente {

    public ClienteRegular() {
        super();
    }

    public ClienteRegular(String nombreCompleto, String correo, String contrasena) {
        super(nombreCompleto, correo, contrasena);
    }

    @Override
    public double calcularDescuento() {
        return 0.0;
    }
}
