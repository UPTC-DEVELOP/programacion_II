package co.edu.uptc.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

public class VentanaPedido extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private PanelProducto panelProducto;
    private PanelHistorial panelHistorial;

    public VentanaPedido() {
        super("Registrar Pedidos", true, true, true, true);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(950, 560);

        panelProducto = new PanelProducto();
        panelHistorial = new PanelHistorial();

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setLayout(new BorderLayout(8, 8));
        add(panelProducto, BorderLayout.NORTH);
        add(panelHistorial, BorderLayout.CENTER);
    }

    public PanelProducto getPanelProducto() {
        return panelProducto;
    }

    public PanelHistorial getPanelHistorial() {
        return panelHistorial;
    }
}
