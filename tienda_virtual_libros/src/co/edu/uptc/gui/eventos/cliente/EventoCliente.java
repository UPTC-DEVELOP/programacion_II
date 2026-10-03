package co.edu.uptc.gui.eventos.cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import co.edu.uptc.gui.cliente.PanelCliente;

/**
 * Escuchador de TODOS los botones del CRUD de clientes (panel y diálogo).
 * Cada botón se identifica con una de estas constantes (setActionCommand)
 * y aquí solo se decide qué método del panel ejecutar: el evento no valida
 * ni guarda nada, eso lo hace el panel a través de la capa de negocio.
 */
public class EventoCliente implements ActionListener {

	// Panel CRUD
	public final static String BUSCAR = "Buscar";
	public final static String CREAR = "Nuevo";
	public final static String ACTUALIZAR = "Actualizar";
	public final static String ELIMINAR = "Eliminar";

	// Diálogo de cliente
	public final static String GUARDAR = "Guardar";
	public final static String EDITAR = "Editar";
	public final static String CANCELAR = "Cancelar";

	private PanelCliente panel;

	public EventoCliente(PanelCliente p) {
		panel = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String evento = e.getActionCommand();

		if (evento.equals(BUSCAR)) {
			panel.buscarCliente();
		} else if (evento.equals(CREAR)) {
			panel.lanzarDialogoCliente();
		} else if (evento.equals(ACTUALIZAR)) {
			panel.actualizarClienteDialogo();
		} else if (evento.equals(ELIMINAR)) {
			panel.eliminarCliente();
		} else if (evento.equals(GUARDAR)) {
			panel.crearCliente();
		} else if (evento.equals(EDITAR)) {
			panel.actualizarCliente();
		} else if (evento.equals(CANCELAR)) {
			panel.cerrarDialogoCliente();
		}
	}
}
