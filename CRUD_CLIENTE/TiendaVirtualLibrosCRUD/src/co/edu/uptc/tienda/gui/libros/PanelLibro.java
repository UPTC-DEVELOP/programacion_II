package co.edu.uptc.tienda.gui.libros;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.gui.TablaFactory;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.negocio.IGestionLibro;
import co.edu.uptc.tienda.negocio.NegocioException;

public class PanelLibro extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int COLUMNA_ISBN = 1;

	private JTextField txtTitulo;
	private JTable tablaLibros;
	private DefaultTableModel modeloTabla;

	private final IGestionLibro gestionLibro;

	public PanelLibro(IGestionLibro gestionLibro) {
		this.gestionLibro = gestionLibro;

		setLayout(new BorderLayout(5, 5));

		crearPanelSuperior();
		crearTabla();

		cargarLibrosEnTabla(gestionLibro.listarLibros());
	}

	private void crearPanelSuperior() {
		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

		txtTitulo = new JTextField(12);

		JButton btnBuscar = new JButton("Buscar");
		JButton btnListarTodos = new JButton("Ver Todos");
		JButton btnNuevo = new JButton("Nuevo");
		JButton btnActualizar = new JButton("Actualizar");
		JButton btnEliminar = new JButton("Eliminar");

		btnBuscar.addActionListener(e -> buscarLibro());
		btnNuevo.addActionListener(e -> abrirDialogoLibro(null));
		btnActualizar.addActionListener(e -> abrirDialogoActualizarLibro());
		btnEliminar.addActionListener(e -> eliminarLibroSeleccionado());
		btnListarTodos.addActionListener(e -> {
			txtTitulo.setText("");
			cargarLibrosEnTabla(gestionLibro.listarLibros());
		});

		panelSuperior.add(new JLabel("Titulo Libro: "));
		panelSuperior.add(txtTitulo);
		panelSuperior.add(btnBuscar);
		panelSuperior.add(btnListarTodos);
		panelSuperior.add(btnNuevo);
		panelSuperior.add(btnActualizar);
		panelSuperior.add(btnEliminar);

		add(panelSuperior, BorderLayout.NORTH);
	}

	private void crearTabla() {
		String[] columnas = { "ID", "ISBN", "Título", "Autores", "Fecha Publicacion", "Genero", "Editorial", "Paginas",
				"Formato", "Stock", "Precio" };

		modeloTabla = TablaFactory.crearModeloNoEditable(columnas);
		tablaLibros = new JTable(modeloTabla);

		add(TablaFactory.crearTabla(tablaLibros), BorderLayout.CENTER);
	}

	private void cargarLibrosEnTabla(List<Libro> libros) {
		modeloTabla.setRowCount(0);

		for (Libro lb : libros) {
			Object[] fila = { lb.getIdLibro(), lb.getIsbn(), lb.getTitulo(), String.join(", ", lb.getAutores()),
					lb.getFechaPublicacion(), lb.getGenero(), lb.getEditorial(), lb.getNumeroPaginas(),
					lb.getTipoLibro().toString(), lb.getStockDisponible(), lb.getPrecioFinal() };
			modeloTabla.addRow(fila);
		}
	}

	private void buscarLibro() {
		String titulo = txtTitulo.getText().trim();

		if (titulo.isEmpty()) {
			cargarLibrosEnTabla(gestionLibro.listarLibros());
			return;
		}

		Libro lbEncontrado = gestionLibro.buscarLibroPorTitulo(titulo);

		if (lbEncontrado != null) {
			cargarLibrosEnTabla(Collections.singletonList(lbEncontrado));
		} else {
			JOptionPane.showMessageDialog(this, "No se encontró ningún libro con el Titulo: " + titulo,
					"Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
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

		abrirDialogoLibro(libroEditar);
	}

	private void abrirDialogoLibro(Libro libroEditar) {
		DialogoLibro dialogoLibro = new DialogoLibro(gestionLibro, libroEditar);
		dialogoLibro.setVisible(Boolean.TRUE);

		if (dialogoLibro.isGuardadoExitoso()) {
			cargarLibrosEnTabla(gestionLibro.listarLibros());
		}
	}

	private void eliminarLibroSeleccionado() {
		Libro libroEliminar = obtenerLibroSeleccionadoOConsultado();

		if (libroEliminar == null) {
			JOptionPane.showMessageDialog(this,
					"Por favor seleccione un Libro de la tabla o digite un Titulo para eliminar.", "Seleccionar Libro",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(this,
				"¿Está seguro de eliminar el Libro? " + libroEliminar.getTitulo() + " ", "Confirmar Eliminación",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				gestionLibro.eliminarLibro(libroEliminar.getIsbn());
				JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente.", "Eliminación Exitosa",
						JOptionPane.INFORMATION_MESSAGE);
				txtTitulo.setText("");
				cargarLibrosEnTabla(gestionLibro.listarLibros());
			} catch (NegocioException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error al Eliminar", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private Libro obtenerLibroSeleccionadoOConsultado() {
		int filaSeleccionada = tablaLibros.getSelectedRow();

		if (filaSeleccionada != -1) {
			String isbn = (String) modeloTabla.getValueAt(filaSeleccionada, COLUMNA_ISBN);
			return gestionLibro.buscarLibroPorIsbn(isbn);
		}

		String textoTitulo = txtTitulo.getText().trim();
		if (!textoTitulo.isEmpty()) {
			return gestionLibro.buscarLibroPorTitulo(textoTitulo);
		}

		return null;
	}
}
