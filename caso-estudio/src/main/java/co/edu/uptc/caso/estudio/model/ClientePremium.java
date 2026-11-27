package co.edu.uptc.caso.estudio.model;

public class ClientePremium extends Cliente {

    // Porcentaje de descuento fijo (10 %) para clientes con membresía premium
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

    // Aplica el descuento premium únicamente sobre compras con total positivo
    @Override
    public double calcularDescuento(double totalCompra) {
        if (totalCompra <= 0.0) {
            return 0.0;
        }
        return totalCompra * DESCUENTO_PREMIUM;
    }
}