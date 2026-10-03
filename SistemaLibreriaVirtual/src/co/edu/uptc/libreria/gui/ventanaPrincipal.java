package co.edu.uptc.libreria.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

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
	private JComboBox<Libro>cbLibros;
	private JLabel lblPrecioBase, lblStock, lblDescuento, lblIVA, lblPrecioFinal;
	private JTextField txtCantidad;
    private JButton btnRegistrar, btnCancelarPedido;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

	public ventanaPrincipal() {
		setTitle("Tienda Virtual de Libros");
		setSize(820, 480);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Control de Inventario y Ventas ");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

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
	
	// --- Panel Superior: Selección de Productos ---
    JPanel panelSuperior = new JPanel(new GridLayout(6, 2, 5, 5));
    panelSuperior.setBorder(BorderFactory.createTitledBorder("Detalle del Libro"));

    cbProductos = new JComboBox<>();
    // Datos de prueba
    cbLibros.addItem(new Libro("Laptop", 2500000, 10, 10, 19));
    cbLibros.addItem(new Libro("Mouse Optico", 50000, 25, 5, 19));
    cbLibros.addItem(new Libro("Teclado Mecanico", 180000, 15, 0, 19));

    lblPrecioBase = new JLabel();
    lblStock = new JLabel();
    lblDescuento = new JLabel();
    lblIVA = new JLabel();
    lblPrecioFinal = new JLabel();

    panelSuperior.add(new JLabel("Libro:"));
    panelSuperior.add(cbLibros);
    panelSuperior.add(new JLabel("Precio Base:"));
    panelSuperior.add(lblPrecioBase);
    panelSuperior.add(new JLabel("Stock Disponible:"));
    panelSuperior.add(lblStock);
    panelSuperior.add(new JLabel("Descuento (%):"));
    panelSuperior.add(lblDescuento);
    panelSuperior.add(new JLabel("IVA (%):"));
    panelSuperior.add(lblIVA);
    panelSuperior.add(new JLabel("Precio Final Unitario:"));
    panelSuperior.add(lblPrecioFinal);
    
 // --- Panel Central: Formulario de Pedido ---
    JPanel panelCentro = new JPanel(new FlowLayout());
    txtCantidad = new JTextField(5);
    btnRegistrar = new JButton("Registrar Pedido");

    panelCentro.add(new JLabel("Cantidad a comprar:"));
    panelCentro.add(txtCantidad);
    panelCentro.add(btnRegistrar);

    JPanel panelNorteYCentro = new JPanel(new BorderLayout());
    panelNorteYCentro.add(panelSuperior, BorderLayout.NORTH);
    panelNorteYCentro.add(panelCentro, BorderLayout.SOUTH);
    add(panelNorteYCentro, BorderLayout.NORTH);
    
    // --- Panel Inferior: Tabla de Historial y Reversión ---
    modeloTabla = new DefaultTableModel(new String[]{"ISBN", "Nombre", "Cantidad", "Total Pado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Hacer tabla no editable
        }
    };
    tablaHistorial = new JTable(modeloTabla);
    // Ocultar columna del objeto Producto (columna 0)
    tablaHistorial.removeColumn(tablaHistorial.getColumnModel().getColumn(0));

    JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
    btnCancelarPedido = new JButton("Cancelar Pedido Seleccionado");

    JPanel panelInferior = new JPanel(new BorderLayout());
    panelInferior.setBorder(BorderFactory.createTitledBorder("Historial de Transacciones"));
    panelInferior.add(scrollTabla, BorderLayout.CENTER);
    panelInferior.add(btnCancelarPedido, BorderLayout.SOUTH);

    add(panelInferior, BorderLayout.CENTER);
    
 // --- Asignación de Eventos ---
    	configurarEventos();
    	actualizarCamposProducto();
	}

	private void actualizarCamposLibro() {
		Libro p = (Libro) cbLibros.getSelectedItem();
		if (p != null) {
			lblPrecioBase.setText(String.format("$%.2f", p.getPrecioBase()));
			lblStock.setText(String.valueOf(p.getStock()));
			lblDescuento.setText(p.getPorcentajeDescuento() + "%");
			lblIVA.setText(p.getImpuestoIVA() + "%");
			lblPrecioFinal.setText(String.format("$%.2f", p.calcularPrecioFinal()));
		}
	}

	private void configurarEventos() {
		// Evento 1: Cambiar selector de productos (Módulo B.1)
		cbProductos.addActionListener(e -> actualizarCamposProducto());

		// Evento 2: Registrar Pedido (Módulo B.2, B.3 y C.1, C.2)
		btnRegistrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Libro prodSeleccionado = (Libro) cbLibros.getSelectedItem();
				if (LibSeleccionado == null) return;

				try {
					int cantidad = Integer.parseInt(txtCantidad.getText().trim());

					// Validación de cantidad y stock
					if (cantidad <= 0) {
						JOptionPane.showMessageDialog(ventanaPrincipal.this, 
								"La cantidad debe ser mayor a 0.", "Error de entrada", JOptionPane.WARNING_MESSAGE);
						return;
					}

					if (cantidad > prodSeleccionado.getStock()) {
						JOptionPane.showMessageDialog(ventanaPrincipal.this, 
								"Stock insuficiente. Stock actual: " + prodSeleccionado.getStock(), 
								"Error de Stock", JOptionPane.ERROR_MESSAGE);
						return;
					}

					// Decrementar stock
					prodSeleccionado.setStock(prodSeleccionado.getStock() - cantidad);

					// Calcular total
					double total = prodSeleccionado.calcularPrecioFinal() * cantidad;

					// Agregar a la tabla de historial
					modeloTabla.addRow(new Object[]{prodSeleccionado, prodSeleccionado.getNombre(), cantidad, String.format("$%.2f", total)});

					// Limpiar y actualizar UI
					txtCantidad.setText("");
					actualizarCamposProducto();
					JOptionPane.showMessageDialog(ventanaPrincipal.this, "Pedido registrado con éxito.");

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(ventanaPrincipal.this, 
							"Por favor ingrese un número entero válido para la cantidad.", 
							"Entrada No Válida", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Evento 3: Cancelar Pedido Seleccionado (Módulo C.3)
		btnCancelarPedido.addActionListener(e -> {
			int filaSeleccionada = tablaHistorial.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(VentanaPrincipal.this, 
						"Seleccione una transacción de la tabla para cancelar.", 
						"Aviso", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
}