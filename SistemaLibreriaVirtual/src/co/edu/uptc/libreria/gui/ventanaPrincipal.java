package co.edu.uptc.libreria.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import co.edu.uptc.libreria.clientes.gui.DialogoCliente;
import co.edu.uptc.libreria.clientes.gui.PanelClientes;
import co.edu.uptc.libreria.controladores.ControladorCarrito;
import co.edu.uptc.libreria.modelo.Cliente;
import co.edu.uptc.libreria.modelo.ItemCarrito;
import co.edu.uptc.libreria.modelo.Libro;
import co.edu.uptc.libreria.modelo.enums.TipoCliente;
import co.edu.uptc.libreria.negocio.CalculadoraCarrito;
import co.edu.uptc.libreria.negocio.GestionCarrito;
import co.edu.uptc.libreria.negocio.GestionLibro;
import co.edu.uptc.libreria.negocio.LibroConfig;
import co.edu.uptc.libreria.negocio.TiendaConfig;
import co.edu.uptc.libreria.persistencia.CarritoPersistencia;
import co.edu.uptc.libreria.persistencia.LocalCarritoPersistencia;
import co.edu.uptc.libreria.persistencia.ServicioAuditoria;

/**
 * Ventana principal de la tienda. Reune en pestañas los tres modulos:
 * Clientes, Catalogo (detalle del libro y pedido) y Carrito. La gestion de
 * libros (registrar, buscar, actualizar, eliminar) se abre desde el menu "Libros".
 */
public class ventanaPrincipal extends JFrame {

	private static final int PESTANA_CATALOGO = 1;
	private static final int PESTANA_CARRITO = 2;

	// --- Clientes ---
	private Evento evento;
	private TiendaConfig config;
	private PanelClientes panClientes;
	private DialogoCliente dialogoCliente;

	// --- Libros y carrito ---
	private GestionLibro gestionLibro;
	private ControladorCarrito controlador;
	private EventosGui eventosCarrito;
	private VistaCarrito vistaCarrito;
	private CalculadoraCarrito calculadora;

	// --- Catalogo / pedido ---
	private JTabbedPane pestanas;
	private JComboBox<Cliente> cbClientes;
	private JComboBox<Libro> cbLibros;
	private JLabel lblTipoCliente, lblPrecioBase, lblStock, lblDescuento, lblIVA, lblPrecioFinal;
	private JTextField txtCantidad;
	private JButton btnRegistrar;
	/** Evita que los listeners reaccionen cuando los combos se repueblan por codigo. */
	private boolean actualizandoCombos = false;

	public ventanaPrincipal() {
		setTitle("Tienda Virtual de Libros");
		setSize(780, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));

		// Inicializar relaciones o asociaciones
		evento = new Evento(this);
		config = new TiendaConfig();
		gestionLibro = LibroConfig.getInstancia().getGestionLibro();
		calculadora = new CalculadoraCarrito();

		construirCarrito();
		panClientes = new PanelClientes(evento);

		pestanas = new JTabbedPane();
		pestanas.addTab("Clientes", panClientes);
		pestanas.addTab("Catálogo", construirPanelCatalogo());
		pestanas.addTab("Carrito", vistaCarrito);
		add(pestanas, BorderLayout.CENTER);

		setJMenuBar(construirMenuLibros());
		configurarEventos();

		refrescarTabla();
		refrescarCatalogo();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			ventanaPrincipal v = new ventanaPrincipal();
			v.setVisible(Boolean.TRUE);
		});
	}

	// ======================= CONSTRUCCION DE LA VISTA =======================

	/** Arma las capas del carrito (persistencia, negocio, controlador y vista). */
	private void construirCarrito() {
		ServicioAuditoria auditoria = new ServicioAuditoria();
		CarritoPersistencia persistencia = new LocalCarritoPersistencia(gestionLibro);
		GestionCarrito gestionCarrito = new GestionCarrito(persistencia, auditoria);
		controlador = new ControladorCarrito(gestionCarrito, auditoria);

		vistaCarrito = new VistaCarrito();
		eventosCarrito = new EventosGui(vistaCarrito, controlador);
		eventosCarrito.setAlSeguirComprando(() -> pestanas.setSelectedIndex(PESTANA_CATALOGO));
		vistaCarrito.conectarControlador(eventosCarrito);
	}

	// --- Panel Superior: Selección de Libros ---
	private JPanel construirPanelCatalogo() {
		JPanel panelSuperior = new JPanel(new GridLayout(8, 2, 5, 5));
		panelSuperior.setBorder(BorderFactory.createTitledBorder("Detalle del Libro"));

		cbClientes = new JComboBox<>();
		cbLibros = new JComboBox<>();

		lblTipoCliente = new JLabel();
		lblPrecioBase = new JLabel();
		lblStock = new JLabel();
		lblDescuento = new JLabel();
		lblIVA = new JLabel();
		lblPrecioFinal = new JLabel();

		panelSuperior.add(new JLabel("Cliente:"));
		panelSuperior.add(cbClientes);
		panelSuperior.add(new JLabel("Tipo de cliente:"));
		panelSuperior.add(lblTipoCliente);
		panelSuperior.add(new JLabel("Libro:"));
		panelSuperior.add(cbLibros);
		panelSuperior.add(new JLabel("Precio Base:"));
		panelSuperior.add(lblPrecioBase);
		panelSuperior.add(new JLabel("Stock Disponible:"));
		panelSuperior.add(lblStock);
		panelSuperior.add(new JLabel("Descuento (clientes Premium):"));
		panelSuperior.add(lblDescuento);
		panelSuperior.add(new JLabel("IVA (19%):"));
		panelSuperior.add(lblIVA);
		panelSuperior.add(new JLabel("Precio Final Unitario:"));
		panelSuperior.add(lblPrecioFinal);

		// --- Panel Central: Formulario de Pedido ---
		JPanel panelCentro = new JPanel(new FlowLayout());
		txtCantidad = new JTextField(5);
		btnRegistrar = new JButton("Agregar al carrito");

		panelCentro.add(new JLabel("Cantidad a comprar:"));
		panelCentro.add(txtCantidad);
		panelCentro.add(btnRegistrar);

		JPanel panelNorteYCentro = new JPanel(new BorderLayout());
		panelNorteYCentro.add(panelSuperior, BorderLayout.NORTH);
		panelNorteYCentro.add(panelCentro, BorderLayout.SOUTH);

		JPanel contenedor = new JPanel(new BorderLayout());
		contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contenedor.add(panelNorteYCentro, BorderLayout.NORTH);
		return contenedor;
	}

	/** Menu desde el que se abren las ventanas de gestion de libros. */
	private JMenuBar construirMenuLibros() {
		JMenuItem registrar = new JMenuItem("Registrar libro");
		JMenuItem buscar = new JMenuItem("Buscar libro");
		JMenuItem actualizar = new JMenuItem("Actualizar libro");
		JMenuItem eliminar = new JMenuItem("Eliminar libro");

		registrar.addActionListener(e -> new PanelRegistrarLibro().setVisible(true));
		buscar.addActionListener(e -> new PanelBuscarLibro().setVisible(true));
		actualizar.addActionListener(e -> new PanelActualizarLibro().setVisible(true));
		eliminar.addActionListener(e -> new PanelEliminarLibro().setVisible(true));

		JMenu menu = new JMenu("Libros");
		menu.add(registrar);
		menu.add(buscar);
		menu.add(actualizar);
		menu.add(eliminar);

		JMenuBar barra = new JMenuBar();
		barra.add(menu);
		return barra;
	}

	// --- Asignación de Eventos ---
	private void configurarEventos() {
		// Evento 1: cambiar el libro o el cliente recalcula el detalle (precio, descuento, IVA)
		cbLibros.addActionListener(e -> {
			if (!actualizandoCombos) {
				actualizarCamposLibro();
			}
		});
		cbClientes.addActionListener(e -> {
			if (!actualizandoCombos) {
				controlador.setClienteActual((Cliente) cbClientes.getSelectedItem());
				actualizarCamposLibro();
			}
		});

		// Evento 2: registrar el pedido (agregar al carrito)
		btnRegistrar.addActionListener(e -> registrarPedido());

		// Evento 3: al entrar a una pestaña se refresca con los datos mas recientes
		pestanas.addChangeListener(e -> {
			if (pestanas.getSelectedIndex() == PESTANA_CATALOGO) {
				refrescarCatalogo();
			} else if (pestanas.getSelectedIndex() == PESTANA_CARRITO) {
				eventosCarrito.refrescarVista();
			}
		});

		// Evento 4: al volver de las ventanas de libros se recarga el catalogo
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				if (pestanas.getSelectedIndex() == PESTANA_CATALOGO) {
					refrescarCatalogo();
				}
			}
		});
	}

	// ======================= CATALOGO / PEDIDO =======================

	/** Repuebla los combos de clientes y libros conservando la seleccion cuando sigue existiendo. */
	private void refrescarCatalogo() {
		actualizandoCombos = true;
		try {
			Cliente clienteSel = (Cliente) cbClientes.getSelectedItem();
			String correoSel = clienteSel == null ? null : clienteSel.getCorreo();
			Libro libroSel = (Libro) cbLibros.getSelectedItem();
			String codigoSel = libroSel == null ? null : libroSel.getCodigo();

			cbClientes.removeAllItems();
			Cliente clienteNuevo = null;
			for (Cliente c : config.getGestCliente().listarClientes()) {
				cbClientes.addItem(c);
				if (c.getCorreo().equals(correoSel)) {
					clienteNuevo = c;
				}
			}
			if (clienteNuevo != null) {
				cbClientes.setSelectedItem(clienteNuevo);
			}

			cbLibros.removeAllItems();
			Libro libroNuevo = null;
			for (Libro l : gestionLibro.listarLibros()) {
				cbLibros.addItem(l);
				if (l.getCodigo().equals(codigoSel)) {
					libroNuevo = l;
				}
			}
			if (libroNuevo != null) {
				cbLibros.setSelectedItem(libroNuevo);
			}
		} finally {
			actualizandoCombos = false;
		}
		controlador.setClienteActual((Cliente) cbClientes.getSelectedItem());
		actualizarCamposLibro();
	}

	private void actualizarCamposLibro() {
		Libro libro = (Libro) cbLibros.getSelectedItem();
		Cliente cliente = (Cliente) cbClientes.getSelectedItem();
		boolean esPremium = cliente != null && cliente.getTipo() == TipoCliente.PREMIUM;

		lblTipoCliente.setText(cliente == null ? "-" : String.valueOf(cliente.getTipo()));

		if (libro == null) {
			mostrarDetalleVacio("-");
			lblStock.setText("-");
			return;
		}

		lblStock.setText(String.valueOf(libro.getStock()));
		Double precio = leerPrecio(libro);
		if (precio == null) {
			mostrarDetalleVacio("Precio no válido");
			return;
		}

		double descuento = calculadora.calcularDescuento(precio, esPremium);
		double iva = calculadora.calcularIva(precio, descuento);
		double precioFinal = calculadora.calcularTotalconIva(precio, descuento, iva);

		lblPrecioBase.setText(String.format("$%.2f", precio));
		lblDescuento.setText(String.format("-$%.2f", descuento));
		lblIVA.setText(String.format("$%.2f", iva));
		lblPrecioFinal.setText(String.format("$%.2f", precioFinal));
	}

	private void mostrarDetalleVacio(String textoPrecio) {
		lblPrecioBase.setText(textoPrecio);
		lblDescuento.setText("-");
		lblIVA.setText("-");
		lblPrecioFinal.setText("-");
	}

	/** Valida y agrega el libro seleccionado al carrito. */
	private void registrarPedido() {
		Cliente cliente = (Cliente) cbClientes.getSelectedItem();
		if (cliente == null) {
			JOptionPane.showMessageDialog(this, "Registre y seleccione un cliente antes de comprar.", "Falta el cliente",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Libro libro = (Libro) cbLibros.getSelectedItem();
		if (libro == null) {
			JOptionPane.showMessageDialog(this, "No hay libros en el catálogo. Regístrelos desde el menú Libros.",
					"Catálogo vacío", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Double precio = leerPrecio(libro);
		int stock = leerStock(libro);
		if (precio == null || stock < 0) {
			JOptionPane.showMessageDialog(this,
					"El libro tiene un precio o un stock que no es numérico. Corríjalo desde Libros > Actualizar libro.",
					"Datos del libro no válidos", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			int cantidad = Integer.parseInt(txtCantidad.getText().trim());

			// Validación de cantidad y stock (se descuenta lo que ya esta en el carrito)
			if (cantidad <= 0) {
				JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error de entrada",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			int disponible = stock - cantidadEnCarrito(libro);
			if (cantidad > disponible) {
				JOptionPane.showMessageDialog(this,
						"Stock insuficiente. Disponible para agregar: " + Math.max(0, disponible), "Error de Stock",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			controlador.setClienteActual(cliente);
			controlador.agregarAlCarrito(libro, precio, cantidad);

			// Limpiar y actualizar UI
			txtCantidad.setText("");
			eventosCarrito.refrescarVista();
			JOptionPane.showMessageDialog(this, "Libro agregado al carrito con éxito.");

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Por favor ingrese un número entero válido para la cantidad.",
					"Entrada No Válida", JOptionPane.ERROR_MESSAGE);
		}
	}

	private int cantidadEnCarrito(Libro libro) {
		for (ItemCarrito item : controlador.listarCarrito()) {
			if (item.getLibro().getCodigo().equals(libro.getCodigo())) {
				return item.getCantidad();
			}
		}
		return 0;
	}

	/** Precio del libro como numero, o null si el texto registrado no es valido. */
	private Double leerPrecio(Libro libro) {
		try {
			double precio = Double.parseDouble(libro.getPrecio().trim());
			return precio >= 0 ? precio : null;
		} catch (NumberFormatException | NullPointerException e) {
			return null;
		}
	}

	/** Stock del libro como entero, o -1 si el texto registrado no es valido. */
	private int leerStock(Libro libro) {
		try {
			return Math.max(0, Integer.parseInt(libro.getStock().trim()));
		} catch (NumberFormatException | NullPointerException e) {
			return -1;
		}
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