package co.edu.uptc.libreria.gui;

import co.edu.uptc.libreria.negocio.GestionCarrito;
import co.edu.uptc.libreria.persistencia.CarritoPersistencia;
import co.edu.uptc.libreria.persistencia.LocalCarritoPersistencia;

import javax.swing.JFrame;

import co.edu.uptc.libreria.controladores.*;

public class VistaPrincipal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ServicioAuditoria auditoria = new ServicioAuditoria();
		CarritoPersistencia persistencia = new LocalCarritoPersistencia();
		GestionCarrito gestionCarrito = new GestionCarrito(persistencia, auditoria);
		ControladorCarrito controlador = new ControladorCarrito(gestionCarrito);
		VistaCarrito panelVista = new VistaCarrito();
		EventosGui eventos = new EventosGui(panelVista, controlador);
		
		panelVista.conectarControlador(eventos);
		
		JFrame frame = new JFrame("Libreria");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(720, 520);
		frame.add(panelVista);
		frame.setVisible(true);

	}

}
