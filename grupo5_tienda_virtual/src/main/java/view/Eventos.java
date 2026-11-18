package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class Eventos {

    protected final JFrame ventana;

    protected Eventos(JFrame ventana) {
        this.ventana = ventana;
    }

    protected void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(
                ventana, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                ventana, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(
                ventana, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    protected void volver(JFrame padre) {
        ventana.dispose();
        if (padre != null) {
            padre.setVisible(true);
        }
    }
}
