package co.edu.uptc.gui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.BorderFactory;
import javax.swing.WindowConstants;

public class VentanaHistorialCompras extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private final PanelHistorialCompras panelHistorial;

    public VentanaHistorialCompras() {
        super("Historial de compras", true, true, true, true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(900, 360);
        panelHistorial = new PanelHistorialCompras();
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());
        add(panelHistorial, BorderLayout.CENTER);
    }

    public PanelHistorialCompras getPanelHistorial() {
        return panelHistorial;
    }
}
