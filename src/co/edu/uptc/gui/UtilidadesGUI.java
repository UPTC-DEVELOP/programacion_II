package co.edu.uptc.gui;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.JOptionPane;


public final class UtilidadesGUI {

    private static final String TITULO = "Tienda Virtual de Libros - UPTC";

    // Solo se usa desde el hilo de eventos (EDT)
    private static final DecimalFormat FORMATO_MONEDA = new DecimalFormat("$ #,##0.##",
            DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-CO")));

    private UtilidadesGUI() {
    }

    public static String formatearMoneda(double valor) {
        return FORMATO_MONEDA.format(valor);
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
