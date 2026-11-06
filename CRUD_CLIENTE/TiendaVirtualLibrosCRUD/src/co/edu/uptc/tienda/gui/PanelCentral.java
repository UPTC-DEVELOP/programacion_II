package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import co.edu.uptc.tienda.gui.clientes.PanelCliente;
import co.edu.uptc.tienda.gui.libros.PanelLibro;

public class PanelCentral extends JPanel {

	private static final long serialVersionUID = 1L;

	public PanelCentral(PanelCliente panelCliente, PanelLibro panelLibro) {

		setLayout(new BorderLayout());

		JTabbedPane pPestana = new JTabbedPane();
		pPestana.addTab("Cliente", panelCliente);
		pPestana.addTab("Libro", panelLibro);

		add(pPestana);
	}
}
