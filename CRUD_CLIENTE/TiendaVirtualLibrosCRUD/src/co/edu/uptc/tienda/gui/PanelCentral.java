package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import co.edu.uptc.tienda.personas.gui.PanelCliente;

public class PanelCentral extends JPanel {

	private PanelCliente panelCliente;
	// private PanelLibro panelLibro

	public PanelCentral() {

		
		setLayout(new BorderLayout());

		// Asociaciones o Relaciones
		panelCliente = new PanelCliente();
		// panelLibro = new PanelLibro();

		// Paneles
		JTabbedPane pPestana = new JTabbedPane();
		JPanel pCliente = new JPanel();
		JPanel pLibro = new JPanel();

		pCliente.add(panelCliente);

		pPestana.addTab("Cliente", pCliente);
		pPestana.addTab("Libro", pLibro);

		add(pPestana);
	}
}

//setLayout(new BorderLayout());
//JTabbedPane pestanas = new JTabbedPane();
//JPanel panelCliente = new JPanel();
//JPanel panelLibro = new JPanel();
//panelCliente.add(panelCentral);
//pestanas.addTab("Cliente", panelCliente);
//pestanas.addTab("Libro", panelLibro);
//add(pestanas, BorderLayout.CENTER);