package co.edu.uptc.tienda.gui.libros;

import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

import co.edu.uptc.tienda.gui.Evento;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.enums.TipoLibro;
import co.edu.uptc.tienda.negocio.IGestionLibro;
import co.edu.uptc.tienda.negocio.NegocioException;

public class DialogoLibro extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField campoIsbn;
	private JTextField campoTitulo;
	private JTextField campoAutores;
	private JTextField campoFechaPublicacion;
	private JTextField campoGenero;
	private JTextField campoEditorial;
	private JTextField campoNumeroPaginas;
	private JTextField campoPrecioBase;
	private JTextField campoStockDisponible;
	private DefaultListModel<String> modeloListaAutores;
	private JComboBox<TipoLibro> comboTipoLibro;

	private boolean guardadoExitoso = false;

	private final IGestionLibro gestionLibro;

	// null cuando se registra un libro nuevo
	private final Libro libro;

	public DialogoLibro(IGestionLibro gestionLibro, Libro libroEditar) {
		this.gestionLibro = gestionLibro;
		this.libro = libroEditar;

		if (libroEditar == null) {
			setTitle("Registrar Libro");
		} else {
			setTitle("Actualizar Libro");
		}

		setSize(600, 500);
		setLocationRelativeTo(null);
		setModal(true);

		interfazGestionLibro();

		if (libroEditar != null) {
			cargarDatosLibro(libroEditar);
		}
	}

	private void interfazGestionLibro() {
		setLayout(new BorderLayout());

		JPanel panelCentralGeneral = new JPanel(new GridLayout(1, 2, 10, 10));
		JPanel panelFormulario = new JPanel(new GridLayout(11, 2, 10, 10));
		JPanel panelListaAutores = new JPanel(new BorderLayout());
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

		this.campoIsbn = new JTextField(10);
		this.campoTitulo = new JTextField(10);
		this.campoAutores = new JTextField(10);
		this.campoFechaPublicacion = new JTextField(10);
		this.campoGenero = new JTextField(10);
		this.campoEditorial = new JTextField(10);
		this.campoNumeroPaginas = new JTextField(10);
		this.campoPrecioBase = new JTextField(10);
		this.campoStockDisponible = new JTextField(10);

		JTextField campoIva = new JTextField(10);
		campoIva.setText(Math.round(Libro.IVA * 100) + "%");
		campoIva.setEditable(Boolean.FALSE);

		this.modeloListaAutores = new DefaultListModel<>();
		JList<String> listaAutores = new JList<>(modeloListaAutores);
		this.comboTipoLibro = new JComboBox<>(TipoLibro.values());

		JButton btnRegistrarAutor = new JButton(Evento.REGISTRAR_AUTOR_LIBRO);
		JButton btnRegistrarLibro = new JButton(libro == null ? Evento.GUARDAR_LIBRO : Evento.ACTUALIZAR_LIBRO);
		JButton btnCancelarRegistro = new JButton(Evento.CANCELAR_REGISTRO_LIBRO);

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

		Evento evento = new Evento();
		evento.registrar(btnCancelarRegistro, Evento.CANCELAR_REGISTRO_LIBRO, this::dispose);
		evento.registrar(btnRegistrarAutor, Evento.REGISTRAR_AUTOR_LIBRO, this::cargarAutores);
		evento.registrar(btnRegistrarLibro, Evento.GUARDAR_LIBRO, this::procesarGuardado);

		panelListaAutores.add(new JScrollPane(listaAutores), BorderLayout.CENTER);

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
		// El ISBN es la clave del libro, no se puede modificar
		campoIsbn.setEditable(Boolean.FALSE);

		campoTitulo.setText(libro.getTitulo());
		campoFechaPublicacion.setText(libro.getFechaPublicacion());
		campoGenero.setText(libro.getGenero());
		campoEditorial.setText(libro.getEditorial());
		campoNumeroPaginas.setText(String.valueOf(libro.getNumeroPaginas()));
		campoPrecioBase.setText(String.valueOf(libro.getPrecioBase()));
		campoStockDisponible.setText(String.valueOf(libro.getStockDisponible()));

		modeloListaAutores.clear();
		for (String autor : libro.getAutores()) {
			modeloListaAutores.addElement(autor);
		}

		comboTipoLibro.setSelectedItem(libro.getTipoLibro());
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

		List<String> autores = new ArrayList<>();
		for (int i = 0; i < modeloListaAutores.getSize(); i++) {
			autores.add(modeloListaAutores.getElementAt(i));
		}

		Integer numeroPaginas = leerEntero(campoNumeroPaginas, "Numero Páginas");
		if (numeroPaginas == null) {
			return;
		}

		Double precioBase = leerDecimal(campoPrecioBase, "Precio Base");
		if (precioBase == null) {
			return;
		}

		Integer stockDisponible = leerEntero(campoStockDisponible, "Stock");
		if (stockDisponible == null) {
			return;
		}

		TipoLibro tipoLibro = (TipoLibro) comboTipoLibro.getSelectedItem();
		Libro libroProcesado = tipoLibro.crear(campoIsbn.getText().trim(), campoTitulo.getText().trim(), autores,
				campoFechaPublicacion.getText().trim(), campoGenero.getText().trim(), campoEditorial.getText().trim(),
				numeroPaginas, precioBase, stockDisponible);

		try {
			if (libro == null) {
				gestionLibro.agregarLibro(libroProcesado);
				JOptionPane.showMessageDialog(this, "Libro registrado exitosamente.", "Registro Exitoso",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				gestionLibro.actualizarLibro(libroProcesado);
				JOptionPane.showMessageDialog(this, "Libro actualizado exitosamente.", "Actualización Exitosa",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (NegocioException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);
			return;
		}

		guardadoExitoso = true;
		dispose();
	}

	private Integer leerEntero(JTextField campo, String nombreCampo) {
		try {
			return Integer.parseInt(campo.getText().trim());
		} catch (NumberFormatException e) {
			mostrarErrorNumerico(nombreCampo, "un número entero");
			return null;
		}
	}

	private Double leerDecimal(JTextField campo, String nombreCampo) {
		try {
			return Double.parseDouble(campo.getText().trim());
		} catch (NumberFormatException e) {
			mostrarErrorNumerico(nombreCampo, "un número");
			return null;
		}
	}

	private void mostrarErrorNumerico(String nombreCampo, String tipoEsperado) {
		JOptionPane.showMessageDialog(this, "El campo " + nombreCampo + " es obligatorio y debe ser " + tipoEsperado
				+ ".", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
	}

	public boolean isGuardadoExitoso() {
		return guardadoExitoso;
	}

}
