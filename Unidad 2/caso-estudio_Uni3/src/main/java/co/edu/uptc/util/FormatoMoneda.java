package co.edu.uptc.util;

import java.text.NumberFormat;
import java.util.Locale;

// Esta utilidad mantiene una sola forma de mostrar valores monetarios en pesos colombianos.
public final class FormatoMoneda {
    private static final Locale LOCALE_COLOMBIA = Locale.forLanguageTag("es-CO");
    private FormatoMoneda() { }
    public static String formatear(double valor) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(LOCALE_COLOMBIA);
        formato.setMaximumFractionDigits(0);
        formato.setMinimumFractionDigits(0);
        return formato.format(Math.round(valor));
    }
}
