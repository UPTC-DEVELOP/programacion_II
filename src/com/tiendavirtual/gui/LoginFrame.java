package com.tiendavirtual.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.negocio.GestionCliente;

public class LoginFrame extends JFrame {
	
		private JTextField txUsuario;
		private JPasswordField pfContrasenia;
		private JButton btIniciarSesion;
		private JButton btRegistrarse;
		private GestionCliente gestionCliente;
		private int intentos=0;
		
	public LoginFrame() {
		// TODO Auto-generated constructor stub
		gestionCliente=new GestionCliente();
		
		setTitle("INICIO DE SESIÓN");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(4, 2));
		
		txUsuario=new JTextField();
		pfContrasenia=new JPasswordField();
		btIniciarSesion=new JButton("INICIAR SESIÓN");
		btIniciarSesion.addActionListener(e -> {
			String correo=txUsuario.getText();
			String contrasenia=new String(pfContrasenia.getPassword());
			
			Cliente cliente=gestionCliente.iniciarSesion(correo, contrasenia);
			
			if (cliente != null) {
			    javax.swing.JOptionPane.showMessageDialog(this, "INICIO DE SESIÓN CORRECTO");
			    intentos = 0;

			    MenuPrincipal ventana = new MenuPrincipal(gestionCliente);
			    ventana.setVisible(true);
			    dispose();

			} else {
			    intentos++;

			    if (intentos < 5) {
			        javax.swing.JOptionPane.showMessageDialog(this,
			            "CORREO O CONTRASEÑA INCORRECTOS INTENTO " + intentos + " DE 5");
			    } else {
			        javax.swing.JOptionPane.showMessageDialog(this,
			            "SE ALCANZÓ EL MÁXIMO DE 5 INTENTOS");
			        btIniciarSesion.setEnabled(false);
			    }
			}
		});
		
		
		btRegistrarse=new JButton("REGISTRARSE");
		btRegistrarse.addActionListener(e -> {
			RegistroClienteFrame ventana=new RegistroClienteFrame(gestionCliente);
			ventana.setVisible(true);
		});
		
		add(new JLabel("USUARIO:"));
		add(txUsuario);
		
		add(new JLabel("CONTRASEÑA:"));
		add(pfContrasenia);
		
		add(btIniciarSesion);
		add(btRegistrarse);
		
		
	}

}
