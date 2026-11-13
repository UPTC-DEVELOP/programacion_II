package co.edu.uptc.tienda.gui.compras;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.gui.Evento;
import co.edu.uptc.tienda.gui.PanelLogin;
import co.edu.uptc.tienda.gui.TablaFactory;
import co.edu.uptc.tienda.gui.VentanaPrincipal;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.negocio.IGestionCarrito;
import co.edu.uptc.tienda.negocio.IGestionLibro;
import co.edu.uptc.tienda.negocio.NegocioException;

public class PanelCatalogo extends JPanel {

	private JTextField txtTitulo;
	private JButton btnBuscar;
	private JButton btnVerTodos;
	private JButton btnCerrarSesion;

	private JTable tablaLibros;
	private DefaultTableModel modeloTabla;

	private JSpinner spnCantidad;
	private JButton btnAgregarCarrito;

	private final IGestionLibro gestionLibro;
	private final IGestionCarrito gestionCarrito;

	private final List<Libro> librosMostrados = new ArrayList<>();

	public PanelCatalogo(IGestionLibro gestionLibro, IGestionCarrito gestionCarrito, Evento evento) {
		this.gestionLibro = gestionLibro;
		this.gestionCarrito = gestionCarrito;

		setLayout(new BorderLayout(10, 10));

		crearPanelSuperior();
		crearTabla();
		crearPanelInferior();

		cargarLibrosEnTabla(this.gestionLibro.listarLibros());

		evento.escuchar(this.btnCerrarSesion, Evento.CERRAR_SESION);
	}

	private void crearPanelSuperior() {
		JPanel panelContenedor = new JPanel(new BorderLayout(10, 10));
		JPanel panelSuperiorDer = new JPanel();
		JPanel panelSuperiorIzq = new JPanel();

		this.txtTitulo = new JTextField(15);
		this.btnBuscar = new JButton("Buscar");
		this.btnVerTodos = new JButton("Ver Todos");
		this.btnCerrarSesion = new JButton("Cerrar Sesión");

		Evento evento = new Evento();
		evento.registrar(this.btnBuscar, Evento.BUSCAR_LIBRO, this::buscarPorTitulo);
		evento.registrar(this.btnVerTodos, Evento.VER_LIBROS, this::verTodos);

		panelSuperiorIzq.add(new JLabel("Titulo Libro: ", SwingConstants.CENTER));
		panelSuperiorIzq.add(this.txtTitulo);
		panelSuperiorIzq.add(this.btnBuscar);
		panelSuperiorIzq.add(this.btnVerTodos);

		panelSuperiorDer.add(this.btnCerrarSesion);

		panelContenedor.add(panelSuperiorIzq, BorderLayout.CENTER);
		panelContenedor.add(panelSuperiorDer, BorderLayout.EAST);

		add(panelContenedor, BorderLayout.NORTH);
	}

	private void crearTabla() {
		String[] columnas = { "isbn", "titulo", "autores", "fecha_publicación", "genero", "editorial", "numero_paginas",
				"stock_disponible", "tipo_libro", "iva", "precio_final", "Comprar" };

		this.modeloTabla = TablaFactory.crearModeloNoEditable(columnas);
		this.tablaLibros = new JTable(this.modeloTabla);

		add(TablaFactory.crearTabla(this.tablaLibros), BorderLayout.CENTER);

	}

	private void crearPanelInferior() {
		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

		// Entre 1 y 999 unidades; el negocio valida el stock real
		this.spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
		this.btnAgregarCarrito = new JButton("Agregar al carrito");

		Evento evento = new Evento();
		evento.registrar(this.btnAgregarCarrito, Evento.AGREGAR_CARRITO, this::agregarAlCarrito);

		panelInferior.add(new JLabel("Cantidad: "));
		panelInferior.add(this.spnCantidad);
		panelInferior.add(this.btnAgregarCarrito);

		add(panelInferior, BorderLayout.SOUTH);
	}

	private void cargarLibrosEnTabla(List<Libro> libros) {
		this.modeloTabla.setRowCount(0);
		this.librosMostrados.clear();

		for (Libro libro : libros) {
			// En el catálogo solo se ofrecen los libros que se pueden vender
			if (libro.getStockDisponible() <= 0) {
				continue;
			}
			Object filas[] = { libro.getIsbn(), libro.getTitulo(), String.join(", ", libro.getAutores()),
					libro.getFechaPublicacion(), libro.getGenero(), libro.getEditorial(), libro.getNumeroPaginas(),
					libro.getStockDisponible(), libro.getTipoLibro().name(), Libro.IVA, libro.getPrecioFinal() };
			this.modeloTabla.addRow(filas);
			this.librosMostrados.add(libro);
		}

	}

	private void agregarAlCarrito() {
		int filaSeleccionada = this.tablaLibros.getSelectedRow();

		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un libro del catálogo.", "Seleccionar Libro",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Libro libro = this.librosMostrados.get(this.tablaLibros.convertRowIndexToModel(filaSeleccionada));
		int cantidad = (Integer) this.spnCantidad.getValue();

		try {
			this.gestionCarrito.agregarLibro(libro, cantidad);
			JOptionPane.showMessageDialog(this,
					cantidad + " unidad(es) de '" + libro.getTitulo() + "' agregada(s) al carrito.", "Carrito",
					JOptionPane.INFORMATION_MESSAGE);
			this.spnCantidad.setValue(1);
		} catch (NegocioException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Carrito", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void buscarPorTitulo() {
		String tituloLibro = this.txtTitulo.getText().trim();

		if (tituloLibro.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Error el campo de busqueda esta vacío", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Libro libroEncontrado = this.gestionLibro.buscarLibroPorTitulo(tituloLibro);

		if (libroEncontrado != null && libroEncontrado.getStockDisponible() <= 0) {
			JOptionPane.showMessageDialog(null, "El libro '" + tituloLibro + "' no tiene unidades disponibles",
					"Sin stock", JOptionPane.INFORMATION_MESSAGE);
		} else if (libroEncontrado != null) {
			cargarLibrosEnTabla(Collections.singletonList(libroEncontrado));
		} else {
			JOptionPane.showMessageDialog(null, "No existe el libro '" + tituloLibro + "'", "Libro no encontrador",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private void verTodos() {
		this.txtTitulo.setText("");
		cargarLibrosEnTabla(this.gestionLibro.listarLibros());
	}

}
