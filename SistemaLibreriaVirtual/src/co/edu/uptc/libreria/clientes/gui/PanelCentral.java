package co.edu.uptc.libreria.clientes.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.GridLayout;

import javax.swing.JFrame;


import co.edu.uptc.libreria.gui.Evento;

/**
 * Panel base con botones CRUD, buscador y tabla. Cada entidad (clientes,
 * libros...) lo extiende y define titulo, comandos, columnas y filas.
 */
public abstract class PanelCentral extends JPanel {

	protected String tituloPanel;
	protected JButton btnEliminar;
	protected JButton btnVer;
	protected JButton btnActualizar;
	protected JButton btnCrear;
	protected JButton btnBuscar;
	protected JButton btnLimpiar;
	protected JTextField txBuscar;
	protected JTable tblDatos;
	protected DefaultTableModel modelo;
	
	private JButton botonRegistrarCliente;
    private JButton botonActualizarCliente;
    private JButton botonBuscarCliente;
    private JButton botonEliminarCliente;

    private JButton botonRegistrarLibro;
    private JButton botonActualizarLibro;
    private JButton botonBuscarLibro;
    private JButton botonEliminarLibro;

    private JButton botonSalir;


	public PanelCentral(Evento evento) {
		agregarTituloPanel();
		setLayout(new BorderLayout());

		JPanel pTitulo = new JPanel();
		pTitulo.add(new JLabel(tituloPanel));

		JPanel pBtnFuncion = new JPanel();
		btnEliminar = new JButton("Eliminar");
		btnVer = new JButton("Ver");
		btnActualizar = new JButton("Actualizar");
		btnCrear = new JButton("Nuevo");
		btnEliminar.addActionListener(evento);
		btnVer.addActionListener(evento);
		btnActualizar.addActionListener(evento);
		btnCrear.addActionListener(evento);
		pBtnFuncion.add(btnEliminar);
		pBtnFuncion.add(btnVer);
		pBtnFuncion.add(btnActualizar);
		pBtnFuncion.add(btnCrear);

		JPanel pFiltros = new JPanel();
		txBuscar = new JTextField();
		txBuscar.setPreferredSize(new Dimension(180, 30));
		btnBuscar = new JButton("Buscar");
		btnLimpiar = new JButton("Limpiar");
		btnBuscar.addActionListener(evento);
		btnLimpiar.addActionListener(evento);
		pFiltros.add(txBuscar);
		pFiltros.add(btnBuscar);
		pFiltros.add(btnLimpiar);

		JPanel pSuperior = new JPanel(new BorderLayout());
		pSuperior.add(pTitulo, BorderLayout.NORTH);
		pSuperior.add(pBtnFuncion, BorderLayout.CENTER);
		pSuperior.add(pFiltros, BorderLayout.SOUTH);

		// la tabla es solo de consulta: las celdas no se editan directamente
		modelo = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		tblDatos = new JTable();
		agregarCabeceraTabla();

		add(pSuperior, BorderLayout.NORTH);
		add(new JScrollPane(tblDatos), BorderLayout.CENTER);
		agregarIdentificadorComandoBoton();
	}

	//Valor de la fila seleccionada en la columna indicada
	protected Object getValorSeleccionado(int columna) {
		int fila = tblDatos.getSelectedRow();
		if (fila < 0) {
			throw new IllegalStateException("Debe seleccionar un registro de la tabla");
		}
		return tblDatos.getModel().getValueAt(fila, columna);
	}

	public String getTextoBusqueda() {
		return txBuscar.getText();
	}

	public void limpiarBusqueda() {
		txBuscar.setText("");
	}

	public abstract void agregarTituloPanel();

	public abstract void agregarIdentificadorComandoBoton();

	public abstract void agregarCabeceraTabla();

	public abstract void poblarTabla(List<?> lista);
	setTitle("Tienda Virtual de Libros");
    setSize(550, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    JPanel panelBotones = new JPanel(
            new GridLayout(9, 1, 10, 10)
    );

    botonRegistrarCliente = new JButton("Registrar Cliente");
    botonActualizarCliente = new JButton("Actualizar Cliente");
    botonBuscarCliente = new JButton("Buscar Cliente");
    botonEliminarCliente = new JButton("Eliminar Cliente");

    botonRegistrarLibro = new JButton("Registrar Libro");
    botonActualizarLibro = new JButton("Actualizar Libro");
    botonBuscarLibro = new JButton("Buscar Libro");
    botonEliminarLibro = new JButton("Eliminar Libro");

    botonSalir = new JButton("Salir");

    panelBotones.add(botonRegistrarCliente);
    panelBotones.add(botonActualizarCliente);
    panelBotones.add(botonBuscarCliente);
    panelBotones.add(botonEliminarCliente);

    panelBotones.add(botonRegistrarLibro);
    panelBotones.add(botonActualizarLibro);
    panelBotones.add(botonBuscarLibro);
    panelBotones.add(botonEliminarLibro);

    panelBotones.add(botonSalir);

    add(panelBotones);

    botonRegistrarCliente.addActionListener(e -> {
        new PanelRegistrarCliente().setVisible(true);
        dispose();
    });

    botonActualizarCliente.addActionListener(e -> {
        new PanelActualizarCliente().setVisible(true);
        dispose();
    });

    botonBuscarCliente.addActionListener(e -> {
        new PanelBuscarCliente().setVisible(true);
        dispose();
    });

    botonEliminarCliente.addActionListener(e -> {
        new PanelEliminarCliente().setVisible(true);
        dispose();
    });

    botonRegistrarLibro.addActionListener(e -> {
        new PanelRegistrarLibro().setVisible(true);
        dispose();
    });

    botonActualizarLibro.addActionListener(e -> {
        new PanelActualizarLibro().setVisible(true);
        dispose();
    });

    botonBuscarLibro.addActionListener(e -> {
        new PanelBuscarLibro().setVisible(true);
        dispose();
    });

    botonEliminarLibro.addActionListener(e -> {
        new PanelEliminarLibro().setVisible(true);
        dispose();
    });

    botonSalir.addActionListener(e -> {
        System.exit(0);
    });
}

        