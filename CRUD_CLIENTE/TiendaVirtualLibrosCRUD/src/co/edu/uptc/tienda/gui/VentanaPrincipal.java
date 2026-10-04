package co.edu.uptc.tienda.gui;

import javax.swing.*;

/**
 * Ventana principal de la aplicación Tienda Virtual de Libros (Módulo
 * Clientes).
 */
public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private PanelCentral panelCentral;
	private PanelLogin panelLogin;
	private Evento evento;

	/**
	 * Constructor principal que inicializa y centra la interfaz gráfica.
	 */
	public VentanaPrincipal() {
		setTitle("Tienda Virtual de Libros - Gestión de Clientes");
		setSize(300, 180);
		setResizable(Boolean.FALSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centrar en pantalla

		// Relaciones o Asociaciones
		evento = new Evento(this);
		panelLogin = new PanelLogin(evento);
		this.panelCentral = new PanelCentral();

		add(panelLogin);

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			VentanaPrincipal ventana = new VentanaPrincipal();
			ventana.setVisible(Boolean.TRUE);
		});
	}

	public void loguear() {
		String usuario = panelLogin.getTxtUsuario().getText();
		char[] password = panelLogin.getTxtContrasena().getText().toCharArray();

		if (usuario.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ingrese el Usuario.", "Validación", JOptionPane.WARNING_MESSAGE);
			return;
		} else if (password.length < 8) {
			JOptionPane.showMessageDialog(this, "La contraseña debe tener mínimo 8 caracteres.", "Validación",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.");

		this.panelLogin.setVisible(Boolean.FALSE);
		add(this.panelCentral);
		setSize(1100, 570);
		setLocationRelativeTo(null);
		this.panelCentral.setVisible(Boolean.TRUE);

	}

}