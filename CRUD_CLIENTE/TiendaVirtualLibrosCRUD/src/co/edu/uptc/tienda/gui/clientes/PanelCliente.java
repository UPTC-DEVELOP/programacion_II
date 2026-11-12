package co.edu.uptc.tienda.gui.clientes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.gui.Evento;
import co.edu.uptc.tienda.gui.TablaFactory;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.negocio.IGestionCliente;
import co.edu.uptc.tienda.negocio.NegocioException;

public class PanelCliente extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int COLUMNA_IDENTIFICACION = 1;

	private JTextField txtIdentificacion;
	private JTable tablaClientes;
	private DefaultTableModel modeloTabla;

	private final IGestionCliente gestionCliente;

	public PanelCliente(IGestionCliente gestionCliente) {
		this.gestionCliente = gestionCliente;

		setLayout(new BorderLayout());

		crearPanelSuperior();
		crearTabla();

		cargarClientesEnTabla(gestionCliente.listarClientes());
	}

	private void crearPanelSuperior() {
		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

		txtIdentificacion = new JTextField(12);

		JButton btnBuscar = new JButton("Buscar");
		JButton btnListarTodos = new JButton("Ver Todos");
		JButton btnNuevo = new JButton("Nuevo");
		JButton btnActualizar = new JButton("Actualizar");
		JButton btnEliminar = new JButton("Eliminar");

		panelSuperior.add(new JLabel("Identificación:"));
		panelSuperior.add(txtIdentificacion);
		panelSuperior.add(btnBuscar);
		panelSuperior.add(btnListarTodos);
		panelSuperior.add(btnNuevo);
		panelSuperior.add(btnActualizar);
		panelSuperior.add(btnEliminar);

		add(panelSuperior, BorderLayout.NORTH);

		Evento evento = new Evento();
		evento.registrar(btnBuscar, Evento.BUSCAR_CLIENTE, this::buscarCliente);
		evento.registrar(btnListarTodos, Evento.VER_CLIENTES, this::verTodos);
		evento.registrar(btnNuevo, Evento.CREAR_CLIENTE, () -> abrirDialogoCliente(null));
		evento.registrar(btnActualizar, Evento.ACTUALIZAR_CLIENTE, this::abrirDialogoActualizarCliente);
		evento.registrar(btnEliminar, Evento.ELIMINAR_CLIENTE, this::eliminarClienteSeleccionado);
	}

	private void verTodos() {
		txtIdentificacion.setText("");
		cargarClientesEnTabla(gestionCliente.listarClientes());
	}

	private void crearTabla() {
		String[] columnas = { "ID", "Identificación", "Tipo Doc.", "Nombres", "Apellidos", "Correo Electrónico",
				"Celular", "Dirección", "Tipo Cliente" };

		modeloTabla = TablaFactory.crearModeloNoEditable(columnas);
		tablaClientes = new JTable(modeloTabla);

		add(TablaFactory.crearTabla(tablaClientes), BorderLayout.CENTER);
	}

	private void cargarClientesEnTabla(List<Cliente> clientes) {
		modeloTabla.setRowCount(0);

		for (Cliente c : clientes) {
			Object[] fila = { c.getIdCliente(), c.getIdentificacion(), c.getTipoIdentificacion(),
					unir(c.getPrimerNombre(), c.getOtrosNombres()), unir(c.getPrimerApellido(), c.getOtrosApellidos()),
					c.getCorreoElectronico(), c.getCelular(), c.getDireccion(), c.getTipoCliente().toString() };
			modeloTabla.addRow(fila);
		}
	}

	private String unir(String primero, String otros) {
		return primero + (otros != null && !otros.isEmpty() ? " " + otros : "");
	}

	private void buscarCliente() {
		String identificacion = txtIdentificacion.getText().trim();

		if (identificacion.isEmpty()) {
			cargarClientesEnTabla(gestionCliente.listarClientes());
			return;
		}

		Cliente encontrado = gestionCliente.buscarCliente(identificacion);
		if (encontrado != null) {
			cargarClientesEnTabla(Collections.singletonList(encontrado));
		} else {
			JOptionPane.showMessageDialog(this,
					"No se encontró ningún cliente con la identificación: " + identificacion, "Búsqueda sin resultados",
					JOptionPane.INFORMATION_MESSAGE);
			cargarClientesEnTabla(gestionCliente.listarClientes());
		}
	}

	private void abrirDialogoActualizarCliente() {
		Cliente clienteAEditar = obtenerClienteSeleccionadoOConsultado();

		if (clienteAEditar == null) {
			JOptionPane.showMessageDialog(this,
					"Por favor seleccione un cliente de la tabla o digite una identificación válida para actualizar.",
					"Seleccionar Cliente", JOptionPane.WARNING_MESSAGE);
			return;
		}

		abrirDialogoCliente(clienteAEditar);
	}

	public void abrirDialogoCliente(Cliente clienteAEditar) {
		DialogoCliente dialogo = new DialogoCliente(gestionCliente, clienteAEditar);
		dialogo.setVisible(true);
		if (dialogo.isGuardadoExitoso()) {
			cargarClientesEnTabla(gestionCliente.listarClientes());
		}
	}

	private void eliminarClienteSeleccionado() {
		Cliente clienteAEliminar = obtenerClienteSeleccionadoOConsultado();

		if (clienteAEliminar == null) {
			JOptionPane.showMessageDialog(this,
					"Por favor seleccione un cliente de la tabla o digite una identificación para eliminar.",
					"Seleccionar Cliente", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int confirmacion = JOptionPane.showConfirmDialog(this,
				"¿Está seguro de que desea eliminar al cliente " + clienteAEliminar.getPrimerNombre() + " "
						+ clienteAEliminar.getPrimerApellido() + " (" + clienteAEliminar.getIdentificacion() + ")?",
				"Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				gestionCliente.eliminarCliente(clienteAEliminar.getIdentificacion());
				JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.", "Eliminación Exitosa",
						JOptionPane.INFORMATION_MESSAGE);
				txtIdentificacion.setText("");
				cargarClientesEnTabla(gestionCliente.listarClientes());
			} catch (NegocioException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error al Eliminar", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private Cliente obtenerClienteSeleccionadoOConsultado() {
		int filaSeleccionada = tablaClientes.getSelectedRow();
		if (filaSeleccionada != -1) {
			String identificacion = (String) modeloTabla.getValueAt(filaSeleccionada, COLUMNA_IDENTIFICACION);
			return gestionCliente.buscarCliente(identificacion);
		}

		String identificacionTexto = txtIdentificacion.getText().trim();
		if (!identificacionTexto.isEmpty()) {
			return gestionCliente.buscarCliente(identificacionTexto);
		}

		return null;
	}
}
