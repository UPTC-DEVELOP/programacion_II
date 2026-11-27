package co.edu.uptc.tienda.gui.facturas;

import co.edu.uptc.tienda.gui.TablaFactory;
import co.edu.uptc.tienda.modelo.Factura;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.negocio.IGestionFactura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PanelFactura extends JPanel {

	private final JButton btnBuscar;
	private final JComboBox<String> filtros;
	private final DefaultTableModel modeloTabla;
	private final JTable tabla;
	private final IGestionFactura gestionFactura;

	public PanelFactura(IGestionFactura gestionFactura) {
		this.gestionFactura = gestionFactura;

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
		filtros = new JComboBox<>(new String[] { "Todas" });
		btnBuscar = new JButton("Actualizar");
		panelSuperior.add(new JLabel("Filtro:"));
		panelSuperior.add(filtros);
		panelSuperior.add(btnBuscar);

		String[] columnas = { "Número", "Fecha", "Cliente", "Correo", "Libros comprados", "Total" };
		modeloTabla = TablaFactory.crearModeloNoEditable(columnas);
		tabla = new JTable(modeloTabla);
		JScrollPane scrollTabla = TablaFactory.crearTabla(tabla);

		add(panelSuperior, BorderLayout.NORTH);
		add(scrollTabla, BorderLayout.CENTER);

		btnBuscar.addActionListener(e -> cargarFacturas());

		cargarFacturas();
	}

	public void cargarFacturas() {
		modeloTabla.setRowCount(0);
		if (gestionFactura == null) {
			return;
		}

		List<Factura> facturas = gestionFactura.listarFacturas();
		if (facturas == null) {
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		for (Factura f : facturas) {
			String fechaTexto = f.getFecha() != null ? f.getFecha().format(formatter) : "";
			String nombreCliente = f.getCliente() != null ? f.getCliente().getPrimerNombre() : "";
			String correoCliente = f.getCliente() != null ? f.getCliente().getCorreoElectronico() : "";

			String librosTexto = "";
			if (f.getItems() != null) {
				librosTexto = f.getItems().stream().map(this::formatearItem).collect(Collectors.joining(", "));
			}

			modeloTabla.addRow(new Object[] { f.getNumero(), fechaTexto, nombreCliente, correoCliente, librosTexto,
					String.format("$ %.2f", f.getTotal()) });
		}
	}

	private String formatearItem(ItemCarrito item) {
		String titulo = (item.getLibro() != null && item.getLibro().getTitulo() != null) ? item.getLibro().getTitulo()
				: "Libro";
		return titulo + " (x" + item.getCantidad() + ")";
	}
}