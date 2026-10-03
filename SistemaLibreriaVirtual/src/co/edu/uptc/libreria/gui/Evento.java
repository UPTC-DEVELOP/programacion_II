package co.edu.uptc.libreria.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Evento implements ActionListener {

	public final static String ELIMINAR_CLI = "Eliminar_CLI";
	public final static String VER_CLI = "Ver_CLI";
	public final static String ACTUALIZAR_CLI = "Actualizar_CLI";
	public final static String CREAR_CLI = "Nuevo_CLI";
	public final static String BUSCAR_CLI = "Buscar_CLI";
	public final static String LIMPIAR_CLI = "Limpiar_CLI";

	public final static String GUARDAR_CLI = "Guardar_CLI";
	public final static String EDITAR_CLI = "Editar_CLI";
	public final static String CANCELAR_CLI = "Cancelar_CLI";

	private VentanaPrincipal vent;

	public Evento(VentanaPrincipal v) {
		vent = v;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String evento = e.getActionCommand();

		if (evento.equals(CREAR_CLI)) {
			vent.lanzarDialogoCliente();
		} else if (evento.equals(CANCELAR_CLI)) {
			vent.cerrarDialogoCliente();
		} else if (evento.equals(GUARDAR_CLI)) {
			vent.registrarCliente();
		} else if (evento.equals(ACTUALIZAR_CLI)) {
			vent.actualizarClienteDialogo();
		} else if (evento.equals(EDITAR_CLI)) {
			vent.actualizarCliente();
		} else if (evento.equals(VER_CLI)) {
			vent.verCliente();
		} else if (evento.equals(ELIMINAR_CLI)) {
			vent.eliminarCliente();
		} else if (evento.equals(BUSCAR_CLI)) {
			vent.buscarCliente();
		} else if (evento.equals(LIMPIAR_CLI)) {
			vent.limpiarBusquedaCliente();
		}
	}
}
