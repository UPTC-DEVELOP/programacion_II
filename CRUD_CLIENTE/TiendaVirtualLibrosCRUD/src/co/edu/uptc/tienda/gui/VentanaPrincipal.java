package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;

import javax.swing.*;

/**
 * Ventana principal de la aplicación Tienda Virtual de Libros (Módulo
 * Clientes).
 */
public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private PanelCentral panelCentral;

	/**
	 * Constructor principal que inicializa y centra la interfaz gráfica.
	 */
	public VentanaPrincipal() {
		setTitle("Tienda Virtual de Libros - Gestión de Clientes");
		setSize(1100, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centrar en pantalla

		// Relaciones o AsociacionesF
		panelCentral = new PanelCentral();

		// Panel de Pestañas
		setLayout(new BorderLayout());

		JTabbedPane pestanas = new JTabbedPane();
		JPanel panelCliente = new JPanel();
		JPanel panelLibro = new JPanel();

		panelCliente.add(panelCentral);

		pestanas.addTab("Cliente", panelCliente);
		pestanas.addTab("Libro", panelLibro);

		add(pestanas, BorderLayout.CENTER);

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			VentanaPrincipal ventana = new VentanaPrincipal();
			ventana.setVisible(true);
		});
	}
}