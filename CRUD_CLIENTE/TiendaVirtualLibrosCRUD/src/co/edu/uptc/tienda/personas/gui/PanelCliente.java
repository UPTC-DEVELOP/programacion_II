package co.edu.uptc.tienda.personas.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.negocio.Configuracion;
import co.edu.uptc.tienda.negocio.GestionCliente;

/**
 * Panel de presentación para la administración completa (CRUD) de clientes.
 * Permite buscar por identificación, registrar nuevos clientes, actualizar
 * existentes y eliminarlos.
 */
public class PanelCliente extends JPanel {

	private static final long serialVersionUID = 1L;

	// Componentes visuales de la barra superior
	private JPanel panelSuperior;
	private JLabel lblIdentificacion;
	private JTextField txtIdentificacion;
	private JButton btnBuscar;
	private JButton btnNuevo;
	private JButton btnActualizar;
	private JButton btnEliminar;
	private JButton btnListarTodos;

	// Componentes de la tabla de visualización
	private JTable tablaClientes;
	private DefaultTableModel modeloTabla;

	// Capa de negocio
	private GestionCliente gestionCliente;

	/**
	 * Constructor principal del panel de clientes.
	 */
	public PanelCliente() {
		// Inicializar servicio de negocio
		gestionCliente = Configuracion.getGestionCliente();

		setLayout(new BorderLayout());

		// Inicializar componentes gráficos
		crearPanelSuperior();
		crearTabla();

		// Cargar clientes existentes en la tabla al iniciar
		cargarClientesEnTabla(gestionCliente.listarClientes());
	}

	/**
	 * Construye la barra superior con los controles de búsqueda y botones de acción
	 * CRUD.
	 */
	public void crearPanelSuperior() {
		panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

		lblIdentificacion = new JLabel("Identificación:");
		txtIdentificacion = new JTextField(12);

		btnBuscar = new JButton("Buscar");
		btnListarTodos = new JButton("Ver Todos");
		btnNuevo = new JButton("Nuevo");
		btnActualizar = new JButton("Actualizar");
		btnEliminar = new JButton("Eliminar");

		// Agregar controles en el orden correcto
		panelSuperior.add(lblIdentificacion);
		panelSuperior.add(txtIdentificacion);
		panelSuperior.add(btnBuscar);
		panelSuperior.add(btnListarTodos);
		panelSuperior.add(btnNuevo);
		panelSuperior.add(btnActualizar);
		panelSuperior.add(btnEliminar);

		add(panelSuperior, BorderLayout.NORTH);

		// Asignación de listeners a los botones
		btnBuscar.addActionListener(e -> buscarCliente());
		btnListarTodos.addActionListener(e -> {
			txtIdentificacion.setText("");
			cargarClientesEnTabla(gestionCliente.listarClientes());
		});
		btnNuevo.addActionListener(e -> abrirDialogoNuevoCliente());
		btnActualizar.addActionListener(e -> abrirDialogoActualizarCliente());
		btnEliminar.addActionListener(e -> eliminarClienteSeleccionado());
	}

	/**
	 * Configura la tabla y su modelo para presentar la información de los clientes.
	 */
	private void crearTabla() {
		String[] columnas = { "ID", "Identificación", "Tipo Doc.", "Nombres", "Apellidos", "Correo Electrónico",
				"Celular", "Dirección", "Tipo Cliente" };

		// Modelo no editable directamente desde las celdas
		modeloTabla = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaClientes = new JTable(modeloTabla);
		tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaClientes.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollPane = new JScrollPane(tablaClientes);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Poblar las filas de la tabla con la lista de clientes proporcionada.
	 * 
	 * @param clientes Lista de clientes a desplegar.
	 */
	public void cargarClientesEnTabla(List<Cliente> clientes) {
		// Limpiar filas existentes
		modeloTabla.setRowCount(0);

		if (clientes != null) {
			for (Cliente c : clientes) {
				String nombres = c.getPrimerNombre()
						+ (c.getOtrosNombres() != null && !c.getOtrosNombres().isEmpty() ? " " + c.getOtrosNombres()
								: "");
				String apellidos = c.getPrimerApellido()
						+ (c.getOtrosApellidos() != null && !c.getOtrosApellidos().isEmpty()
								? " " + c.getOtrosApellidos()
								: "");

				Object[] fila = { c.getIdCliente(), c.getIdentificacion(), c.getTipoIdentificacion(), nombres,
						apellidos, c.getCorreoElectronico(), c.getCelular(), c.getDireccion(),
						c.getTipoCliente() != null ? c.getTipoCliente().toString() : "" };
				modeloTabla.addRow(fila);
			}
		}
	}

	/**
	 * Busca un cliente por su número de identificación y actualiza la tabla con el
	 * resultado.
	 */
	private void buscarCliente() {
		String identificacion = txtIdentificacion.getText().trim();

		if (identificacion.isEmpty()) {
			// Si el campo está vacío, se muestran todos los registros
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

	/**
	 * Despliega el diálogo modal para crear un nuevo cliente y refresca la tabla si
	 * se completó.
	 */
	private void abrirDialogoNuevoCliente() {
		DialogoCliente dialogo = new DialogoCliente();
		dialogo.setVisible(true);

		// Al ser modal, la ejecución continúa al cerrarse el diálogo
		if (dialogo.isGuardadoExitoso()) {
			cargarClientesEnTabla(gestionCliente.listarClientes());
		}
	}

	/**
	 * Abre el diálogo de edición con los datos del cliente seleccionado en la tabla
	 * o buscado por ID.
	 */
	private void abrirDialogoActualizarCliente() {
		Cliente clienteAEditar = obtenerClienteSeleccionadoOConsultado();

		if (clienteAEditar == null) {
			JOptionPane.showMessageDialog(this,
					"Por favor seleccione un cliente de la tabla o digite una identificación válida para actualizar.",
					"Seleccionar Cliente", JOptionPane.WARNING_MESSAGE);
			return;
		}

		DialogoCliente dialogo = new DialogoCliente(clienteAEditar);
		dialogo.setVisible(true);

		if (dialogo.isGuardadoExitoso()) {
			cargarClientesEnTabla(gestionCliente.listarClientes());
		}
	}

	/**
	 * Elimina el cliente seleccionado previa confirmación del usuario.
	 */
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
			boolean eliminado = gestionCliente.eliminarCliente(clienteAEliminar.getIdentificacion());
			if (eliminado) {
				JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.", "Eliminación Exitosa",
						JOptionPane.INFORMATION_MESSAGE);
				txtIdentificacion.setText("");
				cargarClientesEnTabla(gestionCliente.listarClientes());
			} else {
				JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente seleccionado.", "Error al Eliminar",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Identifica el cliente a partir de la fila seleccionada en la tabla o del
	 * campo de identificación.
	 * 
	 * @return Instancia del Cliente encontrado, o null si no hay selección válida.
	 */
	private Cliente obtenerClienteSeleccionadoOConsultado() {
		int filaSeleccionada = tablaClientes.getSelectedRow();
		if (filaSeleccionada != -1) {
			String identificacion = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
			return gestionCliente.buscarCliente(identificacion);
		}

		String identificacionTexto = txtIdentificacion.getText().trim();
		if (!identificacionTexto.isEmpty()) {
			return gestionCliente.buscarCliente(identificacionTexto);
		}

		return null;
	}

	public void ejecutarEvento(String evento) {
		// Refresca la tabla al recibir notificaciones de eventos externos
		if ("ACTUALIZAR_TABLA".equalsIgnoreCase(evento)) {
			cargarClientesEnTabla(gestionCliente.listarClientes());
		}
	}
}
