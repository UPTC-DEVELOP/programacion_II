package co.edu.uptc.libreria.gui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import co.edu.uptc.libreria.controladores.*;

public class EventosGui implements ActionListener {
	
	public final static String CANCELAR = "Cancelar";
	public final static String LOGIN = "Login";
	public final static String ELIMINAR = "Eliminar";
	public final static String VER = "Ver";
	public final static String ACTUALIZAR = "Actualizar";
	public final static String CREAR = "Nuevo";
	public final static String BUSCAR = "Buscar";
	public final static String LIMPIAR = "Limpiar";
	public final static String FINALIZAR_COMPRA = "Finalizar compra";
	
	public final static String GUARDAR = "Guardar";
	public final static String EDITAR = "Editar";
	
	private VistaCarrito vista;
	private ControladorCarrito controlador;
	
	
	public EventosGui(VistaCarrito vista, ControladorCarrito controlador) {
		this.vista = vista;
		this.controlador = controlador;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String evento = e.getActionCommand();
		
		if (evento.equals(ELIMINAR)) {
			
			String isbn = vista.obtenerCodigoSeleccionado();
			
			if (isbn != null && !isbn.isEmpty()) {
				controlador.eliminarElemento(isbn);
				vista.actualizarTabla(controlador.listarCarrito());
				JOptionPane.showMessageDialog(vista,"Libro eliminado del carrito" );
			} else {
				JOptionPane.showMessageDialog(vista, "Selecciona un libro Primero");
			}
			
		} else if (evento.equals(ACTUALIZAR)) {
			String isbn = vista.obtenerCodigoSeleccionado();
			int nuevaCantidad = vista.obtenerNuevaCantidad();
			
			if (isbn != null && nuevaCantidad > 0) {
				controlador.modificarCantidad(isbn, nuevaCantidad);
				vista.actualizarTabla(controlador.listarCarrito());
				JOptionPane.showMessageDialog(vista, "cantidad actualizada");
			}
		} else if (evento.equals(FINALIZAR_COMPRA)) {
			if (controlador.listarCarrito().isEmpty()) {
				JOptionPane.showMessageDialog(vista, "El carrito esta vacio, Agrega libros primero");
				return;
			}
			
			//double total = controlador.obtenerTotal();
			String usuario = controlador.getUsuarioActual();
			controlador.finalizarCompra(usuario);
			
			controlador.vaciarCarrito();
			vista.actualizarTabla(controlador.listarCarrito());
			vista.mostrarTotales(0, 0, 0, 0);
			
			JOptionPane.showMessageDialog(vista, "Compra finalizada con exito gracias por tu compra");
		}
		
		
		
	}
	
	
	
	

}
