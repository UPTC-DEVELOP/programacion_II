package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import co.edu.uptc.tienda.personas.gui.PanelCliente;

public class PanelCentral extends JPanel {
	
	private PanelCliente panelCliente;
	
	public PanelCentral() {

        setLayout(new BorderLayout());
        
        panelCliente = new PanelCliente();
        
        //Panel cliente CENTRADO
        add(panelCliente, BorderLayout.CENTER);
	}
}