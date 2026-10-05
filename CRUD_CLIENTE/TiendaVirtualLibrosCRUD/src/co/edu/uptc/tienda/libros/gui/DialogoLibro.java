package co.edu.uptc.tienda.libros.gui;

import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.LibroDigital;
import co.edu.uptc.tienda.modelo.LibroFisico;
import co.edu.uptc.tienda.modelo.enums.TipoLibro;
import co.edu.uptc.tienda.negocio.ConfiguracionLibro;
import co.edu.uptc.tienda.negocio.GestionLibro;
import co.edu.uptc.tienda.gui.Evento;

public class DialogoLibro extends JDialog {

	private JTextField campoIsbn;
	private JTextField campoTitulo;
	private JTextField campoAutores;
	private JTextField campoFechaPublicacion;
	private JTextField campoGenero;
	private JTextField campoEditorial;
	private JTextField campoNumeroPaginas;
	private JTextField campoPrecioBase;
	private JTextField campoIva;
	private JTextField campoStockDisponible;
	private JList<String> listaAutores;
	private DefaultListModel<String> modeloListaAutores;
	private JComboBox<TipoLibro> comboTipoLibro;
	private JButton btnRegistrarAutor;
	private JButton btnRegistrarLibro;
	private JButton btnCancelarRegistro;

	private boolean guardadoExitoso = false;

	private GestionLibro gestionLibro;
	private Libro libro;
	private Evento evento;

	// Constructor por Defecto
	public DialogoLibro() {
		this(null);
	}

	// Constructor que Permite la edición de un Libro
	public DialogoLibro(Libro libroEditar) {
		this.libro = libroEditar;

		if (libroEditar == null) {
			setTitle("Registrar Libro");
		} else {
			setTitle("Actualizar Libro");
		}

		setSize(600, 500);
		setLocationRelativeTo(null);
		setModal(true);

		this.gestionLibro = ConfiguracionLibro.getGestionLibro();

		interfazGestionLibro();

		if (libroEditar != null) {
			cargarDatosLibro(libroEditar);
		}
	}

	// Interfaz que permite el registro de Libro
	private void interfazGestionLibro() {
		setLayout(new BorderLayout());

		JPanel panelCentralGeneral = new JPanel();
		JPanel panelFormulario = new JPanel();
		JPanel panelListaAutores = new JPanel();
		JPanel panelBotones = new JPanel();

		panelCentralGeneral.setLayout(new GridLayout(1, 2, 10, 10));
		panelFormulario.setLayout(new GridLayout(11, 2, 10, 10));
		panelListaAutores.setLayout(new BorderLayout());
		panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

		this.campoIsbn = new JTextField(10);
		this.campoTitulo = new JTextField(10);
		this.campoAutores = new JTextField(10);
		this.campoFechaPublicacion = new JTextField(10);
		this.campoGenero = new JTextField(10);
		this.campoEditorial = new JTextField(10);
		this.campoNumeroPaginas = new JTextField(10);
		this.campoPrecioBase = new JTextField(10);

		this.campoIva = new JTextField(10);
		this.campoIva.setText("19%");
		this.campoIva.setEditable(Boolean.FALSE);

		this.campoStockDisponible = new JTextField(10);
		this.modeloListaAutores = new DefaultListModel<>();
		this.listaAutores = new JList<>(modeloListaAutores);
		this.comboTipoLibro = new JComboBox<>(TipoLibro.values());

		this.btnRegistrarAutor = new JButton(Evento.REGISTRAR_AUTOR_LIBRO);
		this.btnRegistrarLibro = new JButton(libro == null ? Evento.GUARDAR_LIBRO : Evento.ACTUALIZAR_LIBRO);
		this.btnCancelarRegistro = new JButton(Evento.CANCELAR_REGISTRO_LIBRO);

		JScrollPane panelLista = new JScrollPane(listaAutores);

		panelFormulario.add(new JLabel("ISBN (*):"));
		panelFormulario.add(campoIsbn);

		panelFormulario.add(new JLabel("Titulo (*):"));
		panelFormulario.add(campoTitulo);

		panelFormulario.add(new JLabel("Autore(s) (*):"));
		panelFormulario.add(campoAutores);

		panelFormulario.add(new JLabel("Fecha Publicación:"));
		panelFormulario.add(campoFechaPublicacion);

		panelFormulario.add(new JLabel("Genero (*):"));
		panelFormulario.add(campoGenero);

		panelFormulario.add(new JLabel("Editorial:"));
		panelFormulario.add(campoEditorial);

		panelFormulario.add(new JLabel("Numero Páginas (*):"));
		panelFormulario.add(campoNumeroPaginas);

		panelFormulario.add(new JLabel("Precio Base (*):"));
		panelFormulario.add(campoPrecioBase);

		panelFormulario.add(new JLabel("IVA: "));
		panelFormulario.add(campoIva);

		panelFormulario.add(new JLabel("Stock (*):"));
		panelFormulario.add(campoStockDisponible);

		panelFormulario.add(new JLabel("Formato Libro:"));
		panelFormulario.add(comboTipoLibro);

		btnCancelarRegistro.addActionListener(evento);
		btnRegistrarAutor.addActionListener(e -> cargarAutores());
		btnRegistrarLibro.addActionListener(e -> procesarGuardado());

		panelListaAutores.add(panelLista, BorderLayout.CENTER);

		panelCentralGeneral.add(panelFormulario);
		panelCentralGeneral.add(panelListaAutores);

		panelBotones.add(btnCancelarRegistro);
		panelBotones.add(btnRegistrarAutor);
		panelBotones.add(btnRegistrarLibro);

		add(panelCentralGeneral, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
	}

	private void cargarDatosLibro(Libro libro) {
		campoIsbn.setText(libro.getIsbn());
		campoIsbn.setEditable(Boolean.FALSE);

		campoTitulo.setText(libro.getTitulo());
		campoFechaPublicacion.setText(libro.getFechaPublicacion());
		campoGenero.setText(libro.getGenero());
		campoEditorial.setText(libro.getEditorial());
		campoNumeroPaginas.setText(String.valueOf(libro.getNumeroPaginas()));
		campoPrecioBase.setText(String.valueOf(libro.getPrecioBase()));
		campoIva.setText(String.valueOf(libro.getIVA()));
		campoStockDisponible.setText(String.valueOf(libro.getStockDisponible()));

		modeloListaAutores.clear();

		if (libro.getAutores() != null) {
			for (String autor : libro.getAutores()) {
				modeloListaAutores.addElement(autor);
			}
		}

		if (libro.getTipoLibro() != null) {
			comboTipoLibro.setSelectedItem(libro.getTipoLibro());
		}

	}

	private void cargarAutores() {
		String autor = campoAutores.getText().trim();

		if (autor.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Tiene que Ingresar Autores", "Error autores",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			modeloListaAutores.addElement(autor);
			campoAutores.setText("");
		}

	}

	private void procesarGuardado() {
		// Paso 1. Abstracción de Valores y Validaciones
		String isbn = campoIsbn.getText().trim();
		String titulo = campoTitulo.getText().trim();

		List<String> autores = new ArrayList<>();

		for (int i = 0; i < modeloListaAutores.getSize(); i++) {
			autores.add(modeloListaAutores.getElementAt(i));

		}

		String genero = campoGenero.getText().trim();
		String editorial = campoEditorial.getText().trim();
		int numeroPaginas;
		String fechaPublicacion = campoFechaPublicacion.getText().trim();
		double precioBase;
		int stockDisponible;
		TipoLibro tipoLibro = (TipoLibro) comboTipoLibro.getSelectedItem();

		// Validaciones de ISBN
		if (!(isbn.length() >= 8 && isbn.length() <= 12)) {
			JOptionPane.showMessageDialog(this, "Longitud incorrecta (Debe contener de 8 a 12 caracteres)");
			return;
		}

		if (libro == null) {
			Libro existente = gestionLibro.buscarLibro(isbn);

			if (existente != null) {
				JOptionPane.showMessageDialog(this, "Ya existe un libro registrado con este ISBN");
				return;
			}

		}

		// Validación de Paginas
		if (!campoNumeroPaginas.getText().trim().isEmpty()) {
			numeroPaginas = Integer.parseInt(campoNumeroPaginas.getText().trim());

			if (numeroPaginas <= 0) {
				JOptionPane.showMessageDialog(this, "El numero de páginas no puede ser Negativo o 0");
				return;
			}
		} else {
			numeroPaginas = -1;
		}

		// Validación de Precio
		if (!campoPrecioBase.getText().trim().isEmpty()) {
			precioBase = Double.parseDouble(campoPrecioBase.getText().trim());

			if (precioBase <= 0.0) {
				JOptionPane.showMessageDialog(this, "El precio del libro no puede ser Negativo");
				return;
			}
		} else {
			precioBase = -1.0;
		}

		// Validación de Stock
		if (!campoStockDisponible.getText().trim().isEmpty()) {
			stockDisponible = Integer.parseInt(campoStockDisponible.getText().trim());

			if (stockDisponible < 0) {
				JOptionPane.showMessageDialog(this, "El Stock no puede ser menor a 0");
				return;
			}
		} else {
			stockDisponible = -1;
		}

		// Validación de Campos Obligatorios
		boolean campoObligatoriosLlenos = !titulo.isEmpty() && modeloListaAutores.getSize() > 0 && !genero.isEmpty()
				&& numeroPaginas != -1 && precioBase != -1.0 && stockDisponible != -1;

		if (!campoObligatoriosLlenos) {
			JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios (*).",
					"Campos Incompletos", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Paso 2. Construcción
		Libro libroProcesado;

		if (tipoLibro == TipoLibro.DIGITAL) {
			libroProcesado = new LibroDigital(libro != null ? libro.getIdLibro() : 0, isbn, titulo, autores,
					fechaPublicacion, genero, editorial, numeroPaginas, precioBase, stockDisponible, tipoLibro);
		} else {
			libroProcesado = new LibroFisico(libro != null ? libro.getIdLibro() : 0, isbn, titulo, autores,
					fechaPublicacion, genero, editorial, numeroPaginas, precioBase, stockDisponible, tipoLibro);
		}

		// Paso 3. La Persistencia de Datos
		if (libro == null) {
			gestionLibro.agregarLibro(libroProcesado);
			JOptionPane.showMessageDialog(this, "Libro registrado exitosamente.", "Registro Exitoso",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			boolean libroActualizado = gestionLibro.actualizarLibro(libroProcesado);

			if (libroActualizado) {
				JOptionPane.showMessageDialog(this, "Libro actualizado exitosamente.", "Actualización Exitosa",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "No se pudo actualizar la información del Libro.",
						"Error de Actualización", JOptionPane.ERROR_MESSAGE);
				return;
			}

		}
		guardadoExitoso = true;
		dispose();
	}

	public boolean isGuardadoExitoso() {
		return guardadoExitoso;
	}

}
