package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import co.edu.uptc.tienda.libros.gui.DialogoLibro;
import co.edu.uptc.tienda.libros.gui.PanelLibro;
import co.edu.uptc.tienda.personas.gui.PanelCliente;

public class PanelCentral extends JPanel {

	private PanelCliente panelCliente;
	private PanelLibro panelLibro;

	public PanelCentral() {

		setLayout(new BorderLayout());

		// Asociaciones o Relaciones
		panelCliente = new PanelCliente();
		panelLibro = new PanelLibro();

		// Paneles
		JTabbedPane pPestana = new JTabbedPane();
		JPanel pCliente = new JPanel();
		JPanel pLibro = new JPanel();

		pCliente.add(panelCliente);
		pLibro.add(panelLibro);

		pPestana.addTab("Cliente", pCliente);
		pPestana.addTab("Libro", pLibro);

		add(pPestana);
	}
}