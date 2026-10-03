package co.edu.uptc.gui.cliente;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import co.edu.uptc.negocio.cliente.GestionCliente;

public class PanelCentral extends JPanel {

	private static final long serialVersionUID = 1L;

	private PanelCliente panelCliente;

	public PanelCentral(GestionCliente gestionCliente) {

        setLayout(new BorderLayout());

        panelCliente = new PanelCliente(gestionCliente);

        //Panel cleinte CENTRADO
        add(panelCliente, BorderLayout.CENTER);
	}
}
