package co.edu.uptc.tienda.libros.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.enums.TipoLibro;
import co.edu.uptc.tienda.negocio.GestionLibro;
import co.edu.uptc.tienda.gui.Evento;

public class DialogoLibro extends JPanel {

	private JTextField campoIsbn;
	private JTextField campoTitulo;
	private JTextField campoAutores;
	private JTextField campoFechaPublicacion;
	private JTextField campoGenero;
	private JTextField campoNumeroPaginas;
	private JTextField campoPrecioBase;
	private JTextField campoStockDisponible;
	private JList<String> listaAutores;
	private DefaultListModel<String> modeloListaAutores;
	private JComboBox<TipoLibro> comboTipoLibro;
	private JButton btnRegistrarAutor;
	private JButton btnRegistrarLibro;
	private JButton btnCancelarRegistro;

	// Relaciones o Asociaciones
	private GestionLibro gestionLibro;
	private Libro libro;
	private Evento evento;

	// Constructor por Defecto
	public DialogoLibro() {
		interfazGestionLibro();
	}

	// Constructor que Permite la edición de un Libro
	public DialogoLibro(Libro libroEditar) {
		// TODO
	}

	// Interfaz que permite el registro de Libro
	private void interfazGestionLibro() {
		setLayout(new BorderLayout());
		JPanel panelInterfaz = new JPanel();
		JPanel panelBotones = new JPanel();

		panelInterfaz.setLayout(new GridLayout(11, 2, 10, 10)); // 10 campos - 3 Botones - 1 lista
		panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		this.campoIsbn = new JTextField(10);
		this.campoTitulo = new JTextField(10);
		this.campoAutores = new JTextField(10);
		this.campoFechaPublicacion = new JTextField(10);
		this.campoGenero = new JTextField(10);
		this.campoNumeroPaginas = new JTextField(10);
		this.campoPrecioBase = new JTextField(10);
		this.campoStockDisponible = new JTextField(10);
		this.modeloListaAutores = new DefaultListModel<String>();
		this.listaAutores = new JList<String>(modeloListaAutores);
		this.comboTipoLibro = new JComboBox<>(TipoLibro.values());
		this.btnRegistrarAutor = new JButton(Evento.REGISTRAR_AUTOR_LIBRO);
		this.btnRegistrarLibro = new JButton(libro == null ? Evento.GUARDAR_LIBRO : Evento.ACTUALIZAR_LIBRO);
		this.btnCancelarRegistro = new JButton(Evento.CANCELAR_REGISTRO_LIBRO);

		JScrollPane panelLista = new JScrollPane(listaAutores);

		panelInterfaz.add(new JLabel("ISBN (*):"), SwingConstants.CENTER);
		panelInterfaz.add(campoIsbn);

		panelInterfaz.add(new JLabel("Titulo (*):"), SwingConstants.CENTER);
		panelInterfaz.add(campoTitulo);

		panelInterfaz.add(new JLabel("Autore(es) (*):"), SwingConstants.CENTER);
		panelInterfaz.add(campoAutores);

		panelInterfaz.add(new JLabel("Fecha Publicación:"), SwingConstants.CENTER);
		panelInterfaz.add(campoFechaPublicacion);

		panelInterfaz.add(new JLabel("Genero (*):"), SwingConstants.CENTER);
		panelInterfaz.add(campoGenero);

		panelInterfaz.add(new JLabel("Numero Páginas (*):"), SwingConstants.CENTER);
		panelInterfaz.add(campoNumeroPaginas);

		panelInterfaz.add(new JLabel("Precio Base (*):"), SwingConstants.CENTER);
		panelInterfaz.add(campoPrecioBase);

		panelInterfaz.add(new JLabel("Stock (*):"), SwingConstants.CENTER);
		panelInterfaz.add(campoStockDisponible);

		panelInterfaz.add(new JLabel("Formato Libro:"), SwingConstants.CENTER);
		panelInterfaz.add(comboTipoLibro);

		btnCancelarRegistro.addActionListener(evento);
		btnRegistrarAutor.addActionListener(evento);
		btnRegistrarLibro.addActionListener(evento);

		btnCancelarRegistro.setActionCommand(Evento.CANCELAR_REGISTRO_LIBRO);
		btnRegistrarAutor.setActionCommand(Evento.REGISTRAR_AUTOR_LIBRO);
		btnRegistrarLibro.setActionCommand(libro == null ? Evento.GUARDAR_LIBRO : Evento.ACTUALIZAR_LIBRO);

		panelBotones.add(btnCancelarRegistro);
		panelBotones.add(btnRegistrarAutor);
		panelBotones.add(btnRegistrarLibro);

		add(panelInterfaz, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		add(panelLista, BorderLayout.EAST);

	}

}
