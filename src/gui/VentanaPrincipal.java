package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Ventana principal de la aplicacion.
 */
public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaPrincipal() {
        setTitle("Gestion de Productos - Grupo 5");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Gestion de Productos - Grupo 5", SwingConstants.CENTER));
    }
}
