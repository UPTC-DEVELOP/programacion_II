package negocio;

/**
 * Cliente con suscripcion Premium: obtiene descuento sobre la compra.
 */
public class ClientePremium extends Cliente {

    public static final double DESCUENTO = 0.10;

    public ClientePremium() {
        super();
    }

    public ClientePremium(String nombreCompleto, String correo, String contrasena) {
        super(nombreCompleto, correo, contrasena);
    }

    @Override
    public double calcularDescuento() {
        return DESCUENTO;
    }
}
