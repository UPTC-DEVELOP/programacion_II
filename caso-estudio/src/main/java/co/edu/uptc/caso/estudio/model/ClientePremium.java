package co.edu.uptc.caso.estudio.model;

public class ClientePremium extends Cliente {

    private static final double DESCUENTO_PREMIUM = 0.10;

    public ClientePremium() {
        setTipoCliente("PREMIUM");
        setPorcentajeDescuento(DESCUENTO_PREMIUM);
    }

    public ClientePremium(String id, String nombreCompleto, String correo, String direccionEnvio,
                          String departamento, String ciudad, String telefono) {
        super(id, nombreCompleto, correo, direccionEnvio, departamento, ciudad, telefono, "PREMIUM");
        setPorcentajeDescuento(DESCUENTO_PREMIUM);
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return totalCompra * DESCUENTO_PREMIUM;
    }
}