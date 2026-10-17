package co.edu.uptc.negocio;

/**
 * IMPUESTO DE TASA REDUCIDA (5%). Ver CalculadoraImpuesto para el mapeo de
 * categorías que usan esta tasa (TECNOLOGIA y HISTORIA).
 */
public class ImpuestoTasaReducida extends CalculadoraImpuesto {

    /** 5% expresado en decimal (0.05). Constante para no "magiar" el número. */
    private static final double TASA = 0.05;

    @Override
    public double getTasa() {
        return TASA;
    }

    @Override
    public double calcularImpuesto(double baseImponible) {
        // Redondeo a 2 decimales: coherencia con ItemCarrito/CarritoCompras (RF13).
        return Math.round(baseImponible * TASA * 100.0) / 100.0;
    }
}
