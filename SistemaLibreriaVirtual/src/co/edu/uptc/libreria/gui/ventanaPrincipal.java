package co.edu.uptc.libreria.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import co.edu.uptc.libreria.controladores.*;

import co.edu.uptc.libreria.modelo.Cliente;
import co.edu.uptc.libreria.negocio.GestionCarrito;
import co.edu.uptc.libreria.negocio.TiendaConfig;
import co.edu.uptc.libreria.persistencia.CarritoPersistencia;
import co.edu.uptc.libreria.persistencia.LocalCarritoPersistencia;
import co.edu.uptc.libreria.clientes.gui.PanelClientes;
import co.edu.uptc.libreria.controladores.ControladorCarrito;
import co.edu.uptc.libreria.clientes.gui.DialogoCliente;
import co.edu.uptc.libreria.gui.ventanaPrincipal;

public class ventanaPrincipal extends JFrame {

	private Evento evento;
	private TiendaConfig config;
	private PanelClientes panClientes;
	private DialogoCliente dialogoCliente;
	
	
	
	

	public ventanaPrincipal() {
		setTitle("Tienda Virtual de Libros");
		setSize(820, 480);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Inicializar relaciones o asociaciones
		evento = new Evento(this);
		config = new TiendaConfig();
		panClientes = new PanelClientes(evento);
		add(panClientes, BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			ventanaPrincipal v = new ventanaPrincipal();
			v.setVisible(Boolean.TRUE);
		});
		
		ServicioAuditoria auditoria = new ServicioAuditoria();
		CarritoPersistencia persistencia = new LocalCarritoPersistencia();
		GestionCarrito gestionCarrito = new GestionCarrito(persistencia, auditoria);
		ControladorCarrito controlador = new ControladorCarrito(gestionCarrito);
		VistaCarrito panelVista = new VistaCarrito();
		EventosGui eventos = new EventosGui(panelVista, controlador);
	}

	// ======================= CLIENTE =======================

	public void lanzarDialogoCliente() {
		dialogoCliente = new DialogoCliente(this, evento, "Registrar cliente", true);
		dialogoCliente.setVisible(Boolean.TRUE);
	}

	public void cerrarDialogoCliente() {
		if (dialogoCliente != null) {
			dialogoCliente.dispose();
			dialogoCliente = null;
		}
	}

	public void registrarCliente() {
		try {
			config.getGestCliente().registrarCliente(dialogoCliente.capturarDatos());
			cerrarDialogoCliente();
			refrescarTabla();
		} catch (IllegalArgumentException e) {
			// el dialogo queda abierto para que el usuario corrija los datos
			JOptionPane.showMessageDialog(dialogoCliente, e.getMessage());
		}
	}

	public void actualizarClienteDialogo() {
		Cliente seleccionado = obtenerClienteSeleccionado();
		if (seleccionado != null) {
			lanzarActualizarDialogoCliente(seleccionado);
		}
	}

	public void lanzarActualizarDialogoCliente(Cliente actualizado) {
		dialogoCliente = new DialogoCliente(this, evento, "Editar cliente", false);
		dialogoCliente.actualizarCampos(actualizado);
		dialogoCliente.setVisible(Boolean.TRUE);
	}

	public void actualizarCliente() {
		try {
			config.getGestCliente().actualizarCliente(dialogoCliente.capturarDatos());
			cerrarDialogoCliente();
			refrescarTabla();
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(dialogoCliente, e.getMessage());
		}
	}

	public void verCliente() {
		Cliente seleccionado = obtenerClienteSeleccionado();
		if (seleccionado != null) {
			dialogoCliente = new DialogoCliente(this, evento, "Ver cliente", false);
			dialogoCliente.actualizarCampos(seleccionado);
			dialogoCliente.modoSoloLectura();
			dialogoCliente.setVisible(Boolean.TRUE);
		}
	}

	public void eliminarCliente() {
		Cliente seleccionado = obtenerClienteSeleccionado();
		if (seleccionado == null) {
			return;
		}
		int respuesta = JOptionPane.showConfirmDialog(this,
				"¿Desea eliminar al cliente " + seleccionado.getNombreCompleto() + "?", "Confirmar eliminación",
				JOptionPane.YES_NO_OPTION);
		if (respuesta == JOptionPane.YES_OPTION) {
			try {
				config.getGestCliente().eliminarCliente(seleccionado.getCorreo());
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			refrescarTabla();
		}
	}

	public void buscarCliente() {
		refrescarTabla();
	}

	public void limpiarBusquedaCliente() {
		panClientes.limpiarBusqueda();
		refrescarTabla();
	}

	/** Devuelve el cliente seleccionado en la tabla, o null (avisando al usuario) si no hay uno valido. */
	private Cliente obtenerClienteSeleccionado() {
		try {
			Cliente cliente = config.getGestCliente().buscarCliente(panClientes.getCorreoSeleccionado());
			if (cliente == null) {
				JOptionPane.showMessageDialog(this, "El cliente seleccionado ya no existe");
			}
			return cliente;
		} catch (IllegalStateException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			return null;
		}
	}

	/** Repuebla la tabla respetando el texto que haya en el campo de busqueda. */
	private void refrescarTabla() {
		panClientes.poblarTabla(config.getGestCliente().filtrarClientes(panClientes.getTextoBusqueda()));
	}
}