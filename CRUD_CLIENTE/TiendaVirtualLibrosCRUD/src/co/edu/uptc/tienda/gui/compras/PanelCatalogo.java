package co.edu.uptc.tienda.gui.compras;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.gui.Evento;
import co.edu.uptc.tienda.gui.PanelLogin;
import co.edu.uptc.tienda.gui.TablaFactory;
import co.edu.uptc.tienda.gui.VentanaPrincipal;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.negocio.IGestionLibro;

public class PanelCatalogo extends JPanel {

	private JTextField txtTitulo;
	private JButton btnBuscar;
	private JButton btnVerTodos;
	private JButton btnCerrarSesion;

	private JTable tablaLibros;
	private DefaultTableModel modeloTabla;

	private final IGestionLibro gestionLibro;

	public PanelCatalogo(IGestionLibro gestionLibro) {
		this.gestionLibro = gestionLibro;

		setLayout(new BorderLayout(10, 10));

		crearPanelSuperior();
		crearTabla();

		cargarLibrosEnTabla(this.gestionLibro.listarLibros());

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
		evento.registrar(this.btnCerrarSesion, Evento.CERRAR_SESION, this::cerrarSesion);

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

	private void cargarLibrosEnTabla(List<Libro> libros) {
		this.modeloTabla.setRowCount(0);

		for (Libro libro : libros) {
			Object filas[] = { libro.getIsbn(), libro.getTitulo(), String.join(", ", libro.getAutores()),
					libro.getFechaPublicacion(), libro.getGenero(), libro.getEditorial(), libro.getNumeroPaginas(),
					libro.getStockDisponible(), libro.getTipoLibro().name(), Libro.IVA, libro.getPrecioFinal() };
			this.modeloTabla.addRow(filas);
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

		if (libroEncontrado != null) {
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

	private void cerrarSesion() {
	}

}
