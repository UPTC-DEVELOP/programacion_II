package view;

import javax.swing.JButton;
import javax.swing.JFrame;

public class EventosRecibo extends Eventos {

    public EventosRecibo(VentanaRecibo vista) {
        super(vista);
    }

    public void registrarEventos(JButton btnCerrar, JFrame padre) {
        btnCerrar.addActionListener(e -> volver(padre));
    }
}
