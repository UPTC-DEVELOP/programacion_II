package co.edu.uptc.tienda.gui.compras;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.gui.Evento;
import co.edu.uptc.tienda.gui.TablaFactory;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Factura;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.negocio.GestorFactura;
import co.edu.uptc.tienda.negocio.IGestionCarrito;
import co.edu.uptc.tienda.negocio.IGestionCompra;
import co.edu.uptc.tienda.negocio.NegocioException;

public class PanelCarritoCompras extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int COLUMNA_ISBN = 0;

	private JTable tablaCarrito;
	private DefaultTableModel modeloTabla;
	private JLabel lblSubtotal;
	private JLabel lblDescuento;
	private JLabel lblTotal;

	private final IGestionCarrito gestionCarrito;
	private final IGestionCompra gestionCompra;
	private Cliente cliente;
	private Runnable accionVerCatalogo;

	public PanelCarritoCompras(IGestionCarrito gestionCarrito, IGestionCompra gestionCompra) {
		this.gestionCarrito = gestionCarrito;
		this.gestionCompra = gestionCompra;

		setLayout(new BorderLayout(10, 10));

		crearTabla();
		crearPanelInferior();

		actualizar();
	}

	private void crearTabla() {
		String[] columnas = { "ISBN", "Título", "Formato", "Precio (IVA incl.)", "Cantidad", "Subtotal" };

		modeloTabla = TablaFactory.crearModeloNoEditable(columnas);
		tablaCarrito = new JTable(modeloTabla);

		add(TablaFactory.crearTabla(tablaCarrito), BorderLayout.CENTER);
	}

	private void crearPanelInferior() {
		JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		JButton btnVerCatalogo = new JButton("Agregar libros del catálogo");
		JButton btnQuitar = new JButton("Quitar seleccionado");
		JButton btnVaciar = new JButton("Vaciar carrito");

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panelBotones.add(btnVerCatalogo);
		panelBotones.add(btnQuitar);
		panelBotones.add(btnVaciar);

		lblSubtotal = new JLabel("", SwingConstants.RIGHT);
		lblDescuento = new JLabel("", SwingConstants.RIGHT);
		lblTotal = new JLabel("", SwingConstants.RIGHT);
		lblTotal.setFont(lblTotal.getFont().deriveFont(java.awt.Font.BOLD));

		JPanel panelTotales = new JPanel(new GridLayout(3, 1, 0, 4));
		panelTotales.add(lblSubtotal);
		panelTotales.add(lblDescuento);
		panelTotales.add(lblTotal);

		JButton btnFinalizarCompra = new JButton("Finalizar compra");
		btnFinalizarCompra.setFont(btnFinalizarCompra.getFont().deriveFont(java.awt.Font.BOLD));
		JPanel panelDerecho = new JPanel(new BorderLayout(10, 6));
		panelDerecho.add(panelTotales, BorderLayout.CENTER);
		panelDerecho.add(btnFinalizarCompra, BorderLayout.SOUTH);

		panelInferior.add(panelBotones, BorderLayout.WEST);
		panelInferior.add(panelDerecho, BorderLayout.EAST);

		add(panelInferior, BorderLayout.SOUTH);

		Evento evento = new Evento();
		evento.registrar(btnVerCatalogo, Evento.VER_CATALOGO, this::verCatalogo);
		evento.registrar(btnQuitar, Evento.QUITAR_CARRITO, this::quitarSeleccionado);
		evento.registrar(btnVaciar, Evento.VACIAR_CARRITO, this::vaciarCarrito);
		evento.registrar(btnFinalizarCompra, Evento.FINALIZAR_COMPRA, this::finalizarCompra);
	}

	public void setAccionVerCatalogo(Runnable accionVerCatalogo) {
		this.accionVerCatalogo = accionVerCatalogo;
	}

	private void verCatalogo() {
		if (accionVerCatalogo != null) {
			accionVerCatalogo.run();
		}
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		actualizar();
	}

	public void actualizar() {
		modeloTabla.setRowCount(0);

		for (ItemCarrito item : gestionCarrito.listarItems()) {
			Object[] fila = { item.getLibro().getIsbn(), item.getLibro().getTitulo(),
					item.getLibro().getTipoLibro().toString(), item.getLibro().getPrecioFinal(), item.getCantidad(),
					item.getSubtotal() };
			modeloTabla.addRow(fila);
		}

		lblSubtotal.setText("Subtotal: $ " + gestionCarrito.calcularSubtotal());
		lblDescuento.setText(textoDescuento());
		lblTotal.setText("Total a pagar: $ " + gestionCarrito.calcularTotal(cliente));
	}

	private String textoDescuento() {
		String tipo = cliente != null ? " (" + cliente.getTipoCliente() + ")" : "";
		return "Descuento" + tipo + ": - $ " + gestionCarrito.calcularDescuento(cliente);
	}

	private void quitarSeleccionado() {
		int filaSeleccionada = tablaCarrito.getSelectedRow();

		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un libro del carrito para quitarlo.", "Seleccionar Libro",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String isbn = (String) modeloTabla.getValueAt(filaSeleccionada, COLUMNA_ISBN);
		try {
			gestionCarrito.quitarLibro(isbn);
		} catch (NegocioException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Carrito", JOptionPane.WARNING_MESSAGE);
		}
		actualizar();
	}

	private void finalizarCompra() {
		if (gestionCarrito.estaVacio()) {
			JOptionPane.showMessageDialog(this, "El carrito está vacío. Agregue libros del catálogo para comprar.",
					"Carrito", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(this,
				"¿Desea finalizar la compra por $ " + gestionCarrito.calcularTotal(cliente) + "?", "Finalizar Compra",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		Factura factura;
		try {
			factura = gestionCompra.finalizarCompra(cliente);
		} catch (NegocioException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Compra", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Guardar automáticamente la factura en la Base de Datos
		GestorFactura gestorFactura = new GestorFactura();
		boolean guardadoExitoso = gestorFactura.procesarYGuardarFactura(factura);

		if (!guardadoExitoso) {
			JOptionPane.showMessageDialog(this,
					"La compra se procesó, pero hubo un error al guardar la factura en la base de datos.",
					"Advertencia de Base de Datos", JOptionPane.WARNING_MESSAGE);
		}

		actualizar();
		new DialogoFactura(factura).setVisible(true);
	}

	private void vaciarCarrito() {
		if (gestionCarrito.estaVacio()) {
			JOptionPane.showMessageDialog(this, "El carrito ya está vacío.", "Carrito",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea quitar todos los libros del carrito?",
				"Vaciar Carrito", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmacion == JOptionPane.YES_OPTION) {
			gestionCarrito.vaciar();
			actualizar();
		}
	}
}
