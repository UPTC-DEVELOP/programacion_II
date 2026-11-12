package co.edu.uptc.tienda.gui;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PanelLogin extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtUsuario;
	private JPasswordField txtContrasena;
	private JButton btnIngresar;
	private JButton btnRegistrarse;

	public PanelLogin(Evento evento) {
		setLayout(new BorderLayout(10, 10));
		JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));

		JLabel lblUsuario = new JLabel("Correo electrónico:", SwingConstants.CENTER);
		txtUsuario = new JTextField(15);
		txtUsuario.setToolTipText("Correo electrónico registrado");

		JLabel lblContrasena = new JLabel("Contraseña:", SwingConstants.CENTER);
		txtContrasena = new JPasswordField(15);

		panelFormulario.add(lblUsuario);
		panelFormulario.add(txtUsuario);
		panelFormulario.add(lblContrasena);
		panelFormulario.add(txtContrasena);

		btnIngresar = new JButton("Ingresar");
		btnRegistrarse = new JButton("Registrar");

		JPanel panelBoton = new JPanel();
		panelBoton.add(btnIngresar);
		panelBoton.add(btnRegistrarse);

		add(panelFormulario, BorderLayout.CENTER);
		add(panelBoton, BorderLayout.SOUTH);

		evento.escuchar(btnIngresar, Evento.LOGIN);
		evento.escuchar(btnRegistrarse, Evento.REGISTRARSE);
	}

	public String getUsuario() {
		return txtUsuario.getText();
	}

	public char[] getContrasena() {
		return txtContrasena.getPassword();
	}

	// Se usa tras un intento fallido: borra la contraseña 
	public void limpiarContrasena() {
		txtContrasena.setText("");
		txtContrasena.requestFocusInWindow();
	}

	// Permite iniciar sesión presionando Enter
	public JButton getBotonIngresar() {
		return btnIngresar;
	}
}
