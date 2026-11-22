package co.edu.uptc.caso.estudio;

import javax.swing.SwingUtilities;
import co.edu.uptc.gui.VentanaPrincipal;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
