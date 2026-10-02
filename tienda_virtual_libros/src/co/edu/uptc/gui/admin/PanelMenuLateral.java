package co.edu.uptc.gui.admin;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import co.edu.uptc.gui.eventos.admin.EventoAdmin;



/**
 * CLASE PanelMenuLateral  (paquete: gui)  extends JPanel
 * ---------------------------------------------------------------------------
 * Menú izquierdo del prototipo con 4 botones, en este orden:
 *   Dashboard | Manage Books | Reportes | Registro libros
 * Layout: BoxLayout vertical (apila los botones de arriba hacia abajo, cada uno
 * con la misma altura fija y separados por un espacio) dentro de un panel con
 * borde y relleno, como en el prototipo. Cada botón dispara un EventoAdmin.
 * Es un panel reutilizable: la ventana principal solo lo coloca en el WEST.
 */
class PanelMenuLateral extends JPanel {

    private static final long serialVersionUID = 1L;
    private final JButton[] botones;

    PanelMenuLateral() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Estilos.GRIS_FONDO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.GRIS_BORDE),
                BorderFactory.createEmptyBorder(18, 10, 18, 10)));
        setPreferredSize(Estilos.tamano(160, 0));

        botones = new JButton[] {
            Estilos.boton("Dashboard",      EventoAdmin.IR_DASHBOARD.name(), null),
            Estilos.boton("Manage Books",   EventoAdmin.IR_GESTION_LIBROS.name(), null),
            Estilos.boton("Reportes",       EventoAdmin.IR_REPORTES.name(), null),
            Estilos.boton("Registro libros", EventoAdmin.REGISTRAR_LIBRO.name(), null)
        };
        for (JButton b : botones) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));  // ancho completo, alto fijo
            b.setMargin(new java.awt.Insets(2, 2, 2, 2));            // evita que el texto se corte
            add(b);
            add(Box.createVerticalStrut(14));                        // separación entre botones
        }
        add(Box.createVerticalGlue());                               // empuja todo hacia arriba
    }

    /** El controlador se conecta aquí; el panel no sabe quién escucha (bajo acoplamiento). */
    void registrarEscuchador(ActionListener escuchador) {
        for (JButton b : botones) {
            b.addActionListener(escuchador);
        }
    }
}
