package co.edu.uptc.negocio;

/**
 * DESCUENTO PREMIUM: 10% sobre el subtotal (RF13 y RF06).
 * Debe coincidir con el porcentaje de ClientePremium.calcularDescuento().
 */
public class DescuentoPremium extends CalculadoraDescuento {

    /** 10% expresado en decimal (0.10). Misma constante lógica que ClientePremium. */
    private static final double PORCENTAJE_DESCUENTO = 0.10;

    @Override
    public double calcularDescuento(double subtotal) {
        if (subtotal <= 0) {
            return 0.0; // no tiene sentido descontar sobre una base no positiva
        }
        // Redondeo a 2 decimales: coherencia con el resto del cálculo monetario (RF13).
        return Math.round(subtotal * PORCENTAJE_DESCUENTO * 100.0) / 100.0;
    }
}
