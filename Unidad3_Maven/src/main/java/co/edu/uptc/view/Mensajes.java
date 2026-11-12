package co.edu.uptc.view;

import javax.swing.JOptionPane;
import java.awt.Component;

// Mensajes concentra los diálogos para que los controladores no repitan código visual.
public final class Mensajes {
    private Mensajes() { }
    public static void error(Component padre, String mensaje) { JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE); }
    public static void advertencia(Component padre, String mensaje) { JOptionPane.showMessageDialog(padre, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE); }
    public static void informacion(Component padre, String mensaje) { JOptionPane.showMessageDialog(padre, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE); }
    public static boolean confirmar(Component padre, String mensaje) { return JOptionPane.showConfirmDialog(padre, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; }
}
