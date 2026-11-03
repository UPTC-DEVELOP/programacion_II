package co.edu.uptc.negocio;

/**
 * IMPUESTO DE TASA GENERAL (19%). Ver CalculadoraImpuesto para el mapeo de
 * categorías que usan esta tasa (CIENCIA_FICCION, ROMANCE y OTROS).
 */
public class ImpuestoTasaGeneral extends CalculadoraImpuesto {

    /** 19% expresado en decimal (0.19). Constante para no "magiar" el número. */
    private static final double TASA = 0.19;

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
