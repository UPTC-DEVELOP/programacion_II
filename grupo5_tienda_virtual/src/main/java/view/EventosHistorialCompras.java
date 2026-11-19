package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class EventosHistorialCompras extends Eventos {

    private final VentanaHistorialCompras vista;

    public EventosHistorialCompras(VentanaHistorialCompras vista) {
        super(vista);
        this.vista = vista;
    }

    public void registrarEventos(JButton btnAtras, JFrame padre) {
        btnAtras.addActionListener(e -> volver(padre));
        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (padre != null) {
                    padre.setVisible(true);
                }
            }
        });
    }
}
