package co.edu.uptc.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

public class VentanaLibro extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private PanelDetalleLibro panelDetalle;
    private PanelCatalogo panelCatalogo;

    public VentanaLibro() {
        super("Gestionar Libros", true, true, true, true);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE); // se puede reabrir desde el menú
        setSize(950, 560);

        panelDetalle = new PanelDetalleLibro();
        panelCatalogo = new PanelCatalogo();

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setLayout(new BorderLayout(8, 8));
        add(panelDetalle, BorderLayout.WEST);
        add(panelCatalogo, BorderLayout.CENTER);
    }

    public PanelDetalleLibro getPanelDetalle() {
        return panelDetalle;
    }

    public PanelCatalogo getPanelCatalogo() {
        return panelCatalogo;
    }
}

