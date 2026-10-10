package co.edu.uptc.tienda.libros.gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.gui.TablaFactory;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.negocio.ConfiguracionLibro;
import co.edu.uptc.tienda.negocio.GestionLibro;

public class PanelLibro extends JPanel {

	private JPanel panelSuperior;
	private JLabel lblTitulo;
	private JTextField txtTitulo;
	private JButton btnBuscar;
	private JButton btnNuevo;
	private JButton btnActualizar;
	private JButton btnEliminar;
	private JButton btnListarTodos;

	private JTable tablaLibros;
	private DefaultTableModel modeloTabla;

	private GestionLibro gestionLibro;

	public PanelLibro() {
		gestionLibro = ConfiguracionLibro.getGestionLibro();

		setLayout(new BorderLayout(5, 5));

		// Creación del Panel Superior
		panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

		lblTitulo = new JLabel("Titulo Libro: ");
		txtTitulo = new JTextField(12);

		btnBuscar = new JButton("Buscar");
		btnListarTodos = new JButton("Ver Todos");
		btnNuevo = new JButton("Nuevo");
		btnActualizar = new JButton("Actualizar");
		btnEliminar = new JButton("Eliminar");

		btnBuscar.addActionListener(e -> buscarLibro());
		btnNuevo.addActionListener(e -> abrirDialogoNuevoLibro());
		btnActualizar.addActionListener(e -> abrirDialogoActualizarLibro());
		btnEliminar.addActionListener(e -> eliminarLibroSeleccionado());
		btnListarTodos.addActionListener(e -> {
			txtTitulo.setText("");
			cargarLibrosEnTabla(gestionLibro.listarLibros());
		});

		// Agregar controles en el orden correcto
		panelSuperior.add(lblTitulo);
		panelSuperior.add(txtTitulo);
		panelSuperior.add(btnBuscar);
		panelSuperior.add(btnListarTodos);
		panelSuperior.add(btnNuevo);
		panelSuperior.add(btnActualizar);
		panelSuperior.add(btnEliminar);

		String[] columnas = { "ID", "ISBN", "Título", "Autores", "Fecha Publicacion", "Genero", "Editorial", "Paginas",
				"Formato", "Stock", "Precio" };

		// 1. Inicializamos la tabla del panel para que deje de ser null
		tablaLibros = new JTable();

		// 2. Creamos el contenedor para capturar el modelo
		DefaultTableModel[] contenedorModelo = new DefaultTableModel[1];

		// 3. Invocamos la fábrica compartida
		JScrollPane scrollPane = TablaFactory.configurarTabla(columnas, tablaLibros, contenedorModelo);

		// 4. Asignamos el modelo a la variable de tu clase de libros
		this.modeloTabla = contenedorModelo[0];

		add(panelSuperior, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

	}

	public void cargarLibrosEnTabla(List<Libro> libros) {
		modeloTabla.setRowCount(0);

		if (libros != null) {
			for (Libro lb : libros) {
				Object[] fila = { lb.getIdLibro(), lb.getIsbn(), lb.getTitulo(), lb.getAutores(),
						lb.getFechaPublicacion(), lb.getGenero(), lb.getEditorial(), lb.getNumeroPaginas(),
						lb.getTipoLibro().toString(), lb.getStockDisponible(), lb.getPrecioFinal() };
				modeloTabla.addRow(fila);
			}
		}

	}

	private void buscarLibro() {
		String titulo = txtTitulo.getText().trim();

		if (titulo.isEmpty()) {
			cargarLibrosEnTabla(gestionLibro.listarLibros());
			return;
		}

		Libro lbEncontrado = gestionLibro.buscarLibro(titulo);

		if (lbEncontrado != null) {
			cargarLibrosEnTabla(Collections.singletonList(lbEncontrado));
		} else {
			JOptionPane.showMessageDialog(this, "No se encontró ningún libro con el Titulo: " + titulo,
					"Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	private void abrirDialogoNuevoLibro() {
		DialogoLibro dialogoLibro = new DialogoLibro();
		dialogoLibro.setVisible(Boolean.TRUE);

		if (dialogoLibro.isGuardadoExitoso()) {
			cargarLibrosEnTabla(gestionLibro.listarLibros());
		}
	}

	private void abrirDialogoActualizarLibro() {
		Libro libroEditar = obtenerLibroSeleccionadoOConsultado();

		if (libroEditar == null) {
			JOptionPane.showMessageDialog(this,
					"Por favor seleccione un Libro de la tabla o digite un titulo valido para actualizar.",
					"Seleccionar Libro", JOptionPane.WARNING_MESSAGE);
			return;
		}

		DialogoLibro dialogoLibro = new DialogoLibro(libroEditar);
		dialogoLibro.setVisible(Boolean.TRUE);

		if (dialogoLibro.isGuardadoExitoso()) {
			cargarLibrosEnTabla(gestionLibro.listarLibros());
		}
	}

	private void eliminarLibroSeleccionado() {
		Libro libroEliminar = obtenerLibroSeleccionadoOConsultado();

		if (libroEliminar == null) {
			JOptionPane.showMessageDialog(this,
					"Por favor seleccione un Libro de la tabla o digite un Titulo para eliminar.",
					"Seleccionar Cliente", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(this,
				"¿Está seguro de eliminar el Libro? " + libroEliminar.getTitulo() + " ", "Confirmar Eliminación",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmacion == JOptionPane.YES_OPTION) {
			boolean eliminado = gestionLibro.eliminarLibro(libroEliminar.getTitulo());

			if (eliminado) {
				JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente.", "Eliminación Exitosa",
						JOptionPane.INFORMATION_MESSAGE);
				txtTitulo.setText("");
				cargarLibrosEnTabla(gestionLibro.listarLibros());
			} else {
				JOptionPane.showMessageDialog(this, "No se pudo eliminar el libro seleccionado.", "Error al Eliminar",
						JOptionPane.ERROR_MESSAGE);
			}

		}

	}

	private Libro obtenerLibroSeleccionadoOConsultado() {
		int filaSeleccionada = tablaLibros.getSelectedRow();

		if (filaSeleccionada != -1) {
			String titulo = (String) modeloTabla.getValueAt(filaSeleccionada, 2);
			return gestionLibro.buscarLibro(titulo);
		}

		String textoTitulo = txtTitulo.getText().trim();
		if (!textoTitulo.isEmpty()) {
			return gestionLibro.buscarLibro(textoTitulo);
		}

		return null;

	}

	public void ejecutarEvento(String evento) {
		if ("ACTUALIZAR_TABLA".equalsIgnoreCase(evento)) {
			cargarLibrosEnTabla(gestionLibro.listarLibros());
		}
	}
}