package co.edu.uptc.gui.cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.util.Collections;
import java.util.List;

import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.gui.eventos.cliente.EventoCliente;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.excepciones.ReglaNegocioException;


/**
 * CRUD de clientes: Buscar (por identificación), Nuevo, Actualizar y Eliminar.
 * Actualizar y Eliminar trabajan sobre la fila seleccionada en la tabla.
 * Todos los botones (del panel y del diálogo) los escucha EventoCliente, que
 * llama a los métodos públicos de este panel. El panel usa la capa de negocio
 * solo a través de la interfaz IGestionCliente.
 */
public class PanelCliente extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int COLUMNA_IDENTIFICACION = 1;

	private JPanel panelSuperior;
	private JLabel lblIdentificacion;
	private JTextField txtIdentificacion;
	private JButton btnBuscar;
	private JButton btnNuevo;
	private JButton btnActualizar;
	private JButton btnEliminar;
	private DefaultTableModel modeloTabla;
	private final IGestionCliente gestionCliente;
	private JTable tablaClientes;
	private EventoCliente evento;
	private DialogoCliente dialogoCliente;

	//Constructor: la gestion de clientes llega desde AppLibros (inyeccion de dependencias)
    public PanelCliente(IGestionCliente gestionCliente) {
        setLayout(new BorderLayout());
        this.gestionCliente = gestionCliente;
        this.evento = new EventoCliente(this);

        crearPanelSuperior();
        crearTabla();
        listarClientes();
    }


	public void crearPanelSuperior() {

	    panelSuperior = new JPanel(new FlowLayout());
	    lblIdentificacion = new JLabel("Identificación:");
	    txtIdentificacion = new JTextField(15);


	    btnBuscar = new JButton("Buscar");
	    btnNuevo = new JButton("Nuevo");
	    btnActualizar = new JButton("Actualizar");
	    btnEliminar = new JButton("Eliminar");


	    panelSuperior.add(lblIdentificacion);
	    panelSuperior.add(txtIdentificacion);

	    panelSuperior.add(btnBuscar);
	    panelSuperior.add(btnNuevo);
	    panelSuperior.add(btnActualizar);
	    panelSuperior.add(btnEliminar);


	    add(panelSuperior, BorderLayout.NORTH);

	    btnBuscar.setActionCommand(EventoCliente.BUSCAR);
	    txtIdentificacion.setActionCommand(EventoCliente.BUSCAR);   // Enter = buscar
	    btnNuevo.setActionCommand(EventoCliente.CREAR);
	    btnActualizar.setActionCommand(EventoCliente.ACTUALIZAR);
	    btnEliminar.setActionCommand(EventoCliente.ELIMINAR);

	    btnBuscar.addActionListener(evento);
	    txtIdentificacion.addActionListener(evento);
	    btnNuevo.addActionListener(evento);
	    btnActualizar.addActionListener(evento);
	    btnEliminar.addActionListener(evento);
	}

		//Busqueda vacia = mostrar todos
		public void buscarCliente() {
			String identificacion = txtIdentificacion.getText().trim();
			if (identificacion.isEmpty()) {
				listarClientes();
				return;
			}
			Cliente cliente = gestionCliente.buscarCliente(identificacion);
			if (cliente == null) {
				mostrarEnTabla(Collections.emptyList());
				JOptionPane.showMessageDialog(this, "No se encontró un cliente con identificación " + identificacion);
				return;
			}
			mostrarEnTabla(Collections.singletonList(cliente));
		}

		//Boton Nuevo: formulario vacio
		public void lanzarDialogoCliente() {
			abrirDialogoCliente(null);
		}

		//Boton Actualizar: formulario con los datos de la fila seleccionada
		public void actualizarClienteDialogo() {
			Cliente cliente = clienteSeleccionado();
			if (cliente != null) {
				abrirDialogoCliente(cliente);
			}
		}

		//Boton Guardar del dialogo en modo Nuevo
		public void crearCliente() {
			try {
				gestionCliente.agregarCliente(dialogoCliente.getCliente());
			} catch (ReglaNegocioException ex) {
				//Se deja el formulario abierto para que el usuario corrija
				dialogoCliente.mostrarError(ex.getMessage());
				return;
			}
			cerrarDialogoCliente();
			txtIdentificacion.setText("");
			listarClientes();
		}

		//Boton Guardar del dialogo en modo Actualizar
		public void actualizarCliente() {
			try {
				gestionCliente.actualizarCliente(dialogoCliente.getCliente());
			} catch (ReglaNegocioException ex) {
				dialogoCliente.mostrarError(ex.getMessage());
				return;
			}
			cerrarDialogoCliente();
			txtIdentificacion.setText("");
			listarClientes();
		}

		public void cerrarDialogoCliente() {
			if (dialogoCliente != null) {
				dialogoCliente.dispose();
				dialogoCliente = null;
			}
		}

		public void eliminarCliente() {
			Cliente cliente = clienteSeleccionado();
			if (cliente == null) {
				return;
			}
			int respuesta = JOptionPane.showConfirmDialog(this,
					"¿Está seguro de eliminar al cliente " + cliente.getPrimerNombre() + " "
					+ cliente.getPrimerApellido() + " (" + cliente.getIdentificacion() + ")?",
					"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (respuesta != JOptionPane.YES_OPTION) {
				return;
			}
			try {
				gestionCliente.eliminarCliente(cliente.getIdentificacion());
			} catch (ReglaNegocioException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				listarClientes();
				return;
			}
			JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
			listarClientes();
		}

		//Cliente de la fila seleccionada (o null con mensaje si no hay seleccion)
		private Cliente clienteSeleccionado() {
			int fila = tablaClientes.getSelectedRow();
			if (fila < 0) {
				JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			String identificacion = (String) modeloTabla.getValueAt(fila, COLUMNA_IDENTIFICACION);
			return gestionCliente.buscarCliente(identificacion);
		}

	    //Tabla
	    private void crearTabla() {

	        String[] columnas = {
	                "ID",
	                "Identificación",
	                "Nombre",
	                "Apellido",
	                "Correo",
	                "Celular",
	                "Tipo Cliente" };

	        //Celdas de solo lectura: se edita con el boton Actualizar
	        modeloTabla = new DefaultTableModel(columnas, 0) {
	            private static final long serialVersionUID = 1L;

	            @Override
	            public boolean isCellEditable(int fila, int columna) {
	                return false;
	            }
	        };
	        tablaClientes = new JTable(modeloTabla);
	        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	        JScrollPane scrollPane = new JScrollPane(tablaClientes);

	        add(scrollPane, BorderLayout.CENTER);
	    }

	    private void abrirDialogoCliente(Cliente clienteEditar) {
	        dialogoCliente = new DialogoCliente(evento, clienteEditar);
	        dialogoCliente.setVisible(true);
	    }

	    private void listarClientes() {
	    	mostrarEnTabla(gestionCliente.listarClientes());
	    }

	    private void mostrarEnTabla(List<Cliente> listaClientes) {

	    	//cargar de uevo
	    	modeloTabla.setRowCount(0);

	    	for (Cliente cliente : listaClientes) {
	    		Object[] fila = {
	    		        cliente.getIdCliente(),
	    		        cliente.getIdentificacion(),
	    		        cliente.getPrimerNombre(),
	    		        cliente.getPrimerApellido(),
	    		        cliente.getCorreoElectronico(),
	    		        cliente.getCelular(),
	    		        cliente.getTipoCliente() };

	    		modeloTabla.addRow(fila);

	    	}
	    }
}
