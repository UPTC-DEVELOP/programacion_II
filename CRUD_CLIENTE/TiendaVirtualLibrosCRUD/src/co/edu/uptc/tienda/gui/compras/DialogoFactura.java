package co.edu.uptc.tienda.gui.compras;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.gui.TablaFactory;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Factura;
import co.edu.uptc.tienda.modelo.ItemCarrito;


public class DialogoFactura extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private final Factura factura;

	public DialogoFactura(Factura factura) {
		this.factura = factura;

		setTitle("Factura de Compra N° " + factura.getNumero());
		setSize(700, 520);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new BorderLayout(10, 10));

		crearEncabezado();
		crearDetalle();
		crearTotales();
	}

	private void crearEncabezado() {
		Cliente cliente = factura.getCliente();

		JPanel panelEncabezado = new JPanel(new GridLayout(0, 2, 10, 4));
		panelEncabezado.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

		JLabel lblTienda = new JLabel("Tienda Virtual de Libros");
		lblTienda.setFont(lblTienda.getFont().deriveFont(Font.BOLD, 16f));
		panelEncabezado.add(lblTienda);
		panelEncabezado.add(new JLabel("Factura N° " + factura.getNumero(), SwingConstants.RIGHT));

		panelEncabezado.add(new JLabel("Fecha: " + factura.getFecha().format(FORMATO_FECHA)));
		panelEncabezado.add(new JLabel("Tipo de cliente: " + cliente.getTipoCliente(), SwingConstants.RIGHT));

		panelEncabezado.add(new JLabel("Cliente: " + cliente.getPrimerNombre() + " " + cliente.getPrimerApellido()));
		panelEncabezado.add(new JLabel(
				cliente.getTipoIdentificacion() + ": " + cliente.getIdentificacion(), SwingConstants.RIGHT));

		panelEncabezado.add(new JLabel("Correo: " + cliente.getCorreoElectronico()));
		panelEncabezado.add(new JLabel(""));

		// faltan datos de la factura

		add(panelEncabezado, BorderLayout.NORTH);
	}

	private void crearDetalle() {
		String[] columnas = { "ISBN", "Título", "Formato", "Precio (IVA incl.)", "Cantidad", "Subtotal" };

		DefaultTableModel modeloTabla = TablaFactory.crearModeloNoEditable(columnas);
		for (ItemCarrito item : factura.getItems()) {
			Object[] fila = { item.getLibro().getIsbn(), item.getLibro().getTitulo(),
					item.getLibro().getTipoLibro().toString(), item.getLibro().getPrecioFinal(), item.getCantidad(),
					item.getSubtotal() };
			modeloTabla.addRow(fila);
		}

		JPanel panelDetalle = new JPanel(new BorderLayout());
		panelDetalle.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		panelDetalle.add(TablaFactory.crearTabla(new JTable(modeloTabla)), BorderLayout.CENTER);

		add(panelDetalle, BorderLayout.CENTER);
	}

	private void crearTotales() {
		JPanel panelTotales = new JPanel(new GridLayout(3, 1, 0, 4));

		JLabel lblTotal = new JLabel("Total pagado: $ " + factura.getTotal(), SwingConstants.RIGHT);
		lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD));

		panelTotales.add(new JLabel("Subtotal (IVA incluido): $ " + factura.getSubtotal(), SwingConstants.RIGHT));
		panelTotales.add(new JLabel("Descuento: - $ " + factura.getDescuento(), SwingConstants.RIGHT));
		panelTotales.add(lblTotal);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(e -> dispose());
		JPanel panelBoton = new JPanel();
		panelBoton.add(btnCerrar);

		JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
		panelInferior.add(panelTotales, BorderLayout.EAST);
		panelInferior.add(panelBoton, BorderLayout.SOUTH);

		add(panelInferior, BorderLayout.SOUTH);
	}
}
