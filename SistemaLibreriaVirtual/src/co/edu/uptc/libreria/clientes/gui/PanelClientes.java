package co.edu.uptc.libreria.clientes.gui;

import java.util.List;

import co.edu.uptc.libreria.modelo.Cliente;
import co.edu.uptc.libreria.gui.Evento;

public class PanelClientes extends PanelCentral {

	private static final int COL_CORREO = 1;

	public PanelClientes(Evento evento) {
		super(evento);
	}

	@Override
	public void agregarTituloPanel() {
		tituloPanel = "Clientes";
	}

	@Override
	public void agregarIdentificadorComandoBoton() {
		btnEliminar.setActionCommand(Evento.ELIMINAR_CLI);
		btnVer.setActionCommand(Evento.VER_CLI);
		btnActualizar.setActionCommand(Evento.ACTUALIZAR_CLI);
		btnCrear.setActionCommand(Evento.CREAR_CLI);
		btnLimpiar.setActionCommand(Evento.LIMPIAR_CLI);
		btnBuscar.setActionCommand(Evento.BUSCAR_CLI);
	}

	@Override
	public void agregarCabeceraTabla() {
		modelo.addColumn("Nombre completo");
		modelo.addColumn("Correo");
		modelo.addColumn("Dirección de envío");
		modelo.addColumn("Teléfono");
		modelo.addColumn("Tipo");
		tblDatos.setModel(modelo);
	}

	@Override
	public void poblarTabla(List<?> lista) {
		modelo.setRowCount(0);
		for (Object obj : lista) {
			Cliente c = (Cliente) obj;
			Object[] fila = { c.getNombreCompleto(), c.getCorreo(), c.getDireccionEnvio(), c.getTelefono(),
					c.getTipo() };
			modelo.addRow(fila);
		}
	}

	/** Correo (llave) del cliente seleccionado en la tabla. */
	public String getCorreoSeleccionado() {
		return getValorSeleccionado(COL_CORREO).toString();
	}

}
