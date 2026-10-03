package co.edu.uptc.tienda.gui;

import javax.swing.JFrame;


public class VentanaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private PanelCentral panelCentral;

	
	
	public VentanaPrincipal() {
	    setTitle("Tienda Virtual de Libros - Gestión de Clientes");
	    setSize(950, 600);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null); // Centrar en pantalla
	    
	    panelCentral = new PanelCentral();
	    add(panelCentral);
	}
	    
    public static void main(String[] args) {
      
        javax.swing.SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}