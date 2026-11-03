package co.edu.uptc.gui;

import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.enums.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para la gestión (CRUD) de libros por parte del Administrador.
 *
 * PRINCIPIO SOLID:
 * - SRP: Solo se encarga de mostrar la interfaz y capturar eventos.
 * - DIP: Depende de ActionListener (abstracción), no de implementación concreta.
 *
 * @author Grupo 7 - UPTC
 * @version 1.0
 */
public class PanelGestionLibros extends JPanel {

    private static final long serialVersionUID = 1L;

    // Componentes de la interfaz
    private JTextField txtBusqueda;
    private JComboBox<Categoria> comboCategoria;
    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnVolver;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    // Copia de los DTOs que se están mostrando (permite mapear la fila -> DTO)
    private List<LibroDto> librosMostrados = new ArrayList<>();

    // Listener para manejar los eventos
    private final ActionListener manejadorEventos;

    // Comandos de acción
    public static final String CMD_BUSCAR = "BUSCAR_LIBRO";
    public static final String CMD_NUEVO = "NUEVO_LIBRO";
    public static final String CMD_EDITAR = "EDITAR_LIBRO";
    public static final String CMD_ELIMINAR = "ELIMINAR_LIBRO";
    public static final String CMD_VOLVER = "DASHBOARD";

    /**
     * Constructor del panel.
     * @param manejadorEventos Objeto que escuchará los eventos (clase Evento)
     */
    public PanelGestionLibros(ActionListener manejadorEventos) {
        this.manejadorEventos = manejadorEventos;
        inicializarComponentes();
        configurarLayout();
        registrarEventos();
    }

    private void inicializarComponentes() {
        // Campos de búsqueda
        txtBusqueda = new CampoConHint("Título del libro", 20);
        comboCategoria = new JComboBox<>(Categoria.values());
        comboCategoria.insertItemAt(null, 0); // Opción "Todas"
        comboCategoria.setSelectedIndex(0);

        btnBuscar = new JButton("Buscar");
        btnNuevo = new JButton("Nuevo Libro");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnVolver = new JButton("Volver al panel");

        // Configuración de la tabla
        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Categoría", "Precio", "Stock", "Formato"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel superior (búsqueda y acciones)
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Buscar:"));
        panelSuperior.add(txtBusqueda);
        panelSuperior.add(new JLabel("Categoría:"));
        panelSuperior.add(comboCategoria);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(Box.createHorizontalStrut(20));
        panelSuperior.add(btnNuevo);
        panelSuperior.add(btnEditar);
        panelSuperior.add(btnEliminar);
        panelSuperior.add(Box.createHorizontalStrut(20));
        panelSuperior.add(btnVolver);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel central (tabla)
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void registrarEventos() {
        btnBuscar.setActionCommand(CMD_BUSCAR);
        btnBuscar.addActionListener(manejadorEventos);

        btnNuevo.setActionCommand(CMD_NUEVO);
        btnNuevo.addActionListener(manejadorEventos);

        btnEditar.setActionCommand(CMD_EDITAR);
        btnEditar.addActionListener(manejadorEventos);

        btnEliminar.setActionCommand(CMD_ELIMINAR);
        btnEliminar.addActionListener(manejadorEventos);

        btnVolver.setActionCommand(CMD_VOLVER);
        btnVolver.addActionListener(manejadorEventos);
    }

    // Métodos públicos para obtener datos

    public String getTextoBusqueda() {
        return txtBusqueda.getText().trim();
    }

    public Categoria getCategoriaSeleccionada() {
        return (Categoria) comboCategoria.getSelectedItem();
    }

    /**
     * Construye el filtro de búsqueda a partir de los campos del panel.
     */
    public FiltroLibroDto getFiltro() {
        return new FiltroLibroDto(txtBusqueda.getText().trim(), null, getCategoriaSeleccionada());
    }

    /**
     * Devuelve el DTO completo del libro seleccionado en la tabla.
     * @return DTO del libro o null si no hay selección.
     */
    public LibroDto getLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila == -1 || fila >= librosMostrados.size()) {
            return null;
        }
        return librosMostrados.get(fila);
    }

    public void actualizarTabla(List<LibroDto> libros) {
        modeloTabla.setRowCount(0);
        librosMostrados = (libros == null) ? new ArrayList<>() : new ArrayList<>(libros);

        for (LibroDto libro : librosMostrados) {
            String autores = (libro.getAutores() == null)
                    ? "" : String.join(", ", libro.getAutores());
            modeloTabla.addRow(new Object[]{
                    libro.getIsbn(),
                    libro.getTitulo(),
                    autores,
                    libro.getAnioPublicacion(),
                    libro.getCategoria() == null ? "" : libro.getCategoria().name(),
                    libro.getPrecioVenta(),
                    libro.getStock(),
                    libro.getFormato() == null ? "" : libro.getFormato().name()
            });
        }
    }
}
