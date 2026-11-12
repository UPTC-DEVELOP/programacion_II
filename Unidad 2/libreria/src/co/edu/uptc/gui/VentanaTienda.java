package co.edu.uptc.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

public class VentanaTienda extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private PanelTienda panelTienda;

    public VentanaTienda() {
        super("Tienda Virtual - Carrito y Compra", true, true, true, true);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(1100, 700);
        panelTienda = new PanelTienda();
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(panelTienda, BorderLayout.CENTER);
    }

    public PanelTienda getPanelTienda() {
        return panelTienda;
    }
}
