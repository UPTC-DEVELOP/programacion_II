package co.edu.uptc.caso.estudio;

import co.edu.uptc.caso.estudio.gui.VentanaPrincipal;
import co.edu.uptc.caso.estudio.negocio.TiendaConfig;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        TiendaConfig config = new TiendaConfig();
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(
                    config.getGestionLibros(),
                    config.getGestionUsuarios(),
                    config.getGestionCompras()
            );
            ventana.setVisible(true);
        });
    }
}