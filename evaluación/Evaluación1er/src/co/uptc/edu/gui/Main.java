package co.uptc.edu.gui;

import co.uptc.edu.negocio.ventanaInventario;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            configurarApariencia();

            ventanaInventario ventana =
                    new ventanaInventario();

            ventana.setVisible(true);
        });
    }

    private static void configurarApariencia() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception excepcion) {
            System.err.println(
                    "No fue posible cambiar la apariencia: "
                            + excepcion.getMessage()
            );
        }
    }
}
