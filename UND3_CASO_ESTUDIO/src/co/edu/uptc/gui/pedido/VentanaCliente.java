package co.edu.uptc.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

public class VentanaCliente extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private PanelCliente panelCliente;

    public VentanaCliente() {
        super("Clientes", true, true, true, true);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(500, 470);
        panelCliente = new PanelCliente();
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setLayout(new BorderLayout());
        add(panelCliente, BorderLayout.CENTER);
    }

    public PanelCliente getPanelCliente() {
        return panelCliente;
    }
}
