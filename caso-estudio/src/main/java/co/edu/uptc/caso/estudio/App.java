package co.edu.uptc.caso.estudio;

import co.edu.uptc.caso.estudio.gui.VentanaPrincipal;
import co.edu.uptc.caso.estudio.negocio.TiendaConfig;
import javax.swing.SwingUtilities;

// Punto de entrada de la aplicación de escritorio Tienda Virtual de Libros
public class App {

    // Inicializa la configuración de dependencias y lanza la ventana principal en el EDT
    public static void main(String[] args) {
        final TiendaConfig config = new TiendaConfig();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal(
                        config.getGestionLibros(),
                        config.getGestionUsuarios(),
                        config.getGestionCompras());
                ventana.setVisible(true);
            }
        });
    }
}