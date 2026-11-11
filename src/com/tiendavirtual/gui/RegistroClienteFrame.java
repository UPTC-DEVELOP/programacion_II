package com.tiendavirtual.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.modelo.ClientePremium;
import com.tiendavirtual.negocio.GestionCliente;

public class RegistroClienteFrame extends JFrame {
	
	private GestionCliente gestionClientes;
	private JTextField txNombre;
	private JTextField txApellido;
	private JTextField txCorreo;
	private JPasswordField txContrasenia;
	private JTextField txTelefono;
	
	private JButton btRegistrar;
	private JButton btCancelar;
	
	public RegistroClienteFrame(GestionCliente gestionCliente) {
		
		this.gestionClientes=gestionCliente;
		
		setTitle("REGISTRO DE CLIENTE");
		setSize(400, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setLayout(new GridLayout(6, 2));
		
		txNombre=new JTextField();
		txApellido=new JTextField();
		txCorreo=new JTextField();
		txContrasenia=new JPasswordField();
		txTelefono=new JTextField();
		
		btRegistrar=new JButton("REGISTRAR");
		btRegistrar.addActionListener(e -> {
			String nombre=txNombre.getText();
			String apellido=txApellido.getText();
			String correo=txCorreo.getText();
			String contrasenia=new String(txContrasenia.getPassword());
			String telefono=txTelefono.getText();
		
		
		Cliente cliente=new ClientePremium(
				"",
				0,
				nombre,
				apellido,
				correo,
				telefono,
				contrasenia
				);
		
		boolean registrado=gestionCliente.registrarCliente(cliente);
		if (registrado) {
			javax.swing.JOptionPane.showMessageDialog(this,"CLIENTE REGISTRADO CORRECTAMENTE");
		} else {
				javax.swing.JOptionPane.showMessageDialog(this,"NO SE PUDO REGISTRAR EL CLIENTE");	
			
		}
		});
		
		btCancelar=new JButton("VOLVER AL LOGIN");
		btCancelar.addActionListener(e -> {
			dispose();
		});
		
		add(new JLabel("NOMBRE:"));
		add(txNombre);
		
		add(new JLabel("APELLIDO:"));
		add(txApellido);
		
		add(new JLabel("CORREO:"));
		add(txCorreo);
		
		add(new JLabel("CONTRASEÑA:"));
		add(txContrasenia);
		
		add(new JLabel("TELÉFONO:"));
		add(txTelefono);
		
		add(btRegistrar);
		add(btCancelar);
		
		
	}

}
