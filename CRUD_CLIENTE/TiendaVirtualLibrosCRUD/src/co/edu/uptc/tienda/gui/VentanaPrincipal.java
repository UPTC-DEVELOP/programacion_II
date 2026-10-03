package co.edu.uptc.tienda.gui;

import javax.swing.JFrame;

/**
 * Ventana principal de la aplicación Tienda Virtual de Libros (Módulo Clientes).
 */
public class VentanaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private PanelCentral panelCentral;

	/**
	 * Constructor principal que inicializa y centra la interfaz gráfica.
	 */
	public VentanaPrincipal() {
	    setTitle("Tienda Virtual de Libros - Gestión de Clientes");
	    setSize(950, 600);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null); // Centrar en pantalla
	    
	    panelCentral = new PanelCentral();
	    add(panelCentral);
	}
	    
    public static void main(String[] args) {
        // Iniciar la interfaz en el hilo de despacho de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}