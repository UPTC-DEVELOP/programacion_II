package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import co.edu.uptc.tienda.libros.gui.DialogoLibro;
import co.edu.uptc.tienda.personas.gui.PanelCliente;

public class PanelCentral extends JPanel {

	private PanelCliente panelCliente;
	private DialogoLibro prueba;

	public PanelCentral() {

		setLayout(new BorderLayout());

		// Asociaciones o Relaciones
		panelCliente = new PanelCliente();
		prueba = new DialogoLibro();

		// Paneles
		JTabbedPane pPestana = new JTabbedPane();
		JPanel pCliente = new JPanel();
		JPanel pLibro = new JPanel();

		pCliente.add(panelCliente);
		pLibro.add(prueba);

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