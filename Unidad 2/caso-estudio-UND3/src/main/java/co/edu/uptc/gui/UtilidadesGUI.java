package co.edu.uptc.gui;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JOptionPane;

public final class UtilidadesGUI {

    private static final String TITULO = "Tienda Virtual de Libros - UPTC";

    // Solo se usa desde el hilo de eventos (EDT)
    private static final DecimalFormat FORMATO_MONEDA = new DecimalFormat("$ #,##0.##",
            DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-CO")));

    private static final DecimalFormat FORMATO_PORCENTAJE = new DecimalFormat("#,##0.##",
            DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-CO")));

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private UtilidadesGUI() {
    }

    public static String formatearMoneda(double valor) {
        return FORMATO_MONEDA.format(valor);
    }

    /** Ejemplo: 19 -> "19 %", 7.5 -> "7,5 %". */
    public static String formatearPorcentaje(double valor) {
        return FORMATO_PORCENTAJE.format(valor) + " %";
    }

    public static String formatearFecha(LocalDateTime fecha) {
        return fecha.format(FORMATO_FECHA);
    }

    public static void mostrarInformacion(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostrarAdvertencia(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Validación", JOptionPane.WARNING_MESSAGE);
    }

    public static void mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(Component padre, String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(padre, mensaje, TITULO,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }
}
