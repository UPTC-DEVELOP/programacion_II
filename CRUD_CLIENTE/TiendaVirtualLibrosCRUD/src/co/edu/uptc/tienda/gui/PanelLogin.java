package co.edu.uptc.tienda.gui;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class PanelLogin extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtUsuario;
	private JPasswordField txtContrasena;
	private JButton btnIngresar;

	public PanelLogin(Evento evento) {
		setLayout(new BorderLayout(10, 10));
		JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));

		JLabel lblUsuario = new JLabel("Usuario:", SwingConstants.CENTER);
		txtUsuario = new JTextField(15);

		JLabel lblContrasena = new JLabel("Contraseña:", SwingConstants.CENTER);
		txtContrasena = new JPasswordField(15);

		panelFormulario.add(lblUsuario);
		panelFormulario.add(txtUsuario);
		panelFormulario.add(lblContrasena);
		panelFormulario.add(txtContrasena);

		btnIngresar = new JButton("Ingresar");

		JPanel panelBoton = new JPanel();
		panelBoton.add(btnIngresar);

		add(panelFormulario, BorderLayout.CENTER);
		add(panelBoton, BorderLayout.SOUTH);

		btnIngresar.addActionListener(evento);
		btnIngresar.setActionCommand(Evento.LOGIN);
	}

	public JTextField getTxtUsuario() {
		return txtUsuario;
	}

	public JPasswordField getTxtContrasena() {
		return txtContrasena;
	}

}