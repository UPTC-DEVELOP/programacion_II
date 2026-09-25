package co.edu.uptc.gui;

import co.edu.uptc.negocio.GestorLibros;
import co.edu.uptc.negocio.modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de Administración de Libros (Manage Books).
 * 
 * Según prototipo:
 * - Barra de búsqueda por ISBN o Título en la parte superior
 * - Tabla con columnas: ISBN, Título, Autor, Precio, Stock, Acciones
 * - Botones Editar y Eliminar en cada fila
 * - Botón "Registrar Libro" que abre ventana independiente
 * 
 * NO contiene formulario de registro (eso va en VentanaRegistroLibros)
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */
@SuppressWarnings("serial")
public class PanelManageBooks extends JPanel {

    private GestorLibros gestorLibros;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JButton btnRegistrar;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private VentanaPrincipal ventanaPrincipal; // Referencia para abrir diálogos

    public PanelManageBooks(GestorLibros gestorLibros, VentanaPrincipal ventanaPrincipal) {
        this.gestorLibros = gestorLibros;
        this.ventanaPrincipal = ventanaPrincipal;
        inicializarComponentes();
        cargarLibrosEnTabla();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Panel superior: Búsqueda + Botón Registrar ───────────────────
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 0));
        
        // Búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBusqueda = new JLabel("Búsqueda por ISBN o Título:");
        txtBusqueda = new JTextField(30);
        btnBuscar = new JButton("Buscar");
        
        btnBuscar.addActionListener(e -> buscarLibros());
        txtBusqueda.addActionListener(e -> buscarLibros()); // Enter también busca
        
        panelBusqueda.add(lblBusqueda);
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);
        
        // Botón Registrar
        btnRegistrar = new JButton("Registrar Libro");
        btnRegistrar.addActionListener(e -> abrirVentanaRegistro());
        panelSuperior.add(btnRegistrar, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);

        // ── Panel central: Tabla de libros ───────────────────────────────
        String[] columnas = {"ISBN", "Título", "Autor", "Precio", "Stock", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo los botones de acciones son interactivos
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLibros.setRowHeight(30); // Filas más altas para los botones
        
        // Ajustar ancho de columnas
        tablaLibros.getColumnModel().getColumn(0).setPreferredWidth(120); // ISBN
        tablaLibros.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tablaLibros.getColumnModel().getColumn(2).setPreferredWidth(150); // Autor
        tablaLibros.getColumnModel().getColumn(3).setPreferredWidth(100); // Precio
        tablaLibros.getColumnModel().getColumn(4).setPreferredWidth(80);  // Stock
        tablaLibros.getColumnModel().getColumn(5).setPreferredWidth(150); // Acciones

        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Carga todos los libros en la tabla.
     */
    private void cargarLibrosEnTabla() {
        List<Libro> libros = gestorLibros.listarTodosLosLibros();
        actualizarTabla(libros);
    }

    /**
     * Actualiza la tabla con la lista de libros.
     * Agrega botones Editar y Eliminar en la columna Acciones.
     */
    private void actualizarTabla(List<Libro> libros) {
        modeloTabla.setRowCount(0);

        for (Libro libro : libros) {
            String autores = String.join(", ", libro.getAutores());
            // Truncar si es muy largo
            if (autores.length() > 30) {
                autores = autores.substring(0, 27) + "...";
            }
            
            modeloTabla.addRow(new Object[]{
                libro.getIsbn(),
                libro.getTitulo(),
                autores,
                String.format("$%.2f", libro.getPrecioVenta()),
                libro.getStock(),
                "" // Placeholder para botones
            });
        }

        // Agregar botones Editar y Eliminar usando cell renderer
        tablaLibros.getColumnModel().getColumn(5).setCellRenderer(new BotonesAccionesRenderer());
        tablaLibros.getColumnModel().getColumn(5).setCellEditor(new BotonesAccionesEditor(gestorLibros, this));
    }

    /**
     * Busca libros según el texto ingresado.
     */
    private void buscarLibros() {
        String texto = txtBusqueda.getText().trim();
        
        if (texto.isEmpty()) {
            cargarLibrosEnTabla();
            return;
        }

        List<Libro> resultados = gestorLibros.listarLibrosConFiltros(
            null, null, texto, null, null
        );

        actualizarTabla(resultados);
        
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron libros con: " + texto,
                "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Abre la ventana de registro de libros.
     */
    private void abrirVentanaRegistro() {
        VentanaRegistroLibros dialogo = new VentanaRegistroLibros(
            ventanaPrincipal, gestorLibros, this
        );
        dialogo.setVisible(true);
    }

    /**
     * Método público para refrescar la tabla después de registrar/editar/eliminar.
     */
    public void refrescarTabla() {
        cargarLibrosEnTabla();
    }

    /**
     * Renderer personalizado para mostrar botones en la columna Acciones.
     */
    private class BotonesAccionesRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton btnEditar = new JButton("Editar");
        private JButton btnEliminar = new JButton("Eliminar");

        public BotonesAccionesRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            add(btnEditar);
            add(btnEliminar);
            btnEditar.setPreferredSize(new Dimension(70, 25));
            btnEliminar.setPreferredSize(new Dimension(70, 25));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Editor personalizado para manejar clics en los botones Editar/Eliminar.
     */
    private class BotonesAccionesEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        private JButton btnEditar = new JButton("Editar");
        private JButton btnEliminar = new JButton("Eliminar");
        private GestorLibros gestor;
        private PanelManageBooks panelPadre;
        private int filaActual;

        public BotonesAccionesEditor(GestorLibros gestor, PanelManageBooks panelPadre) {
            this.gestor = gestor;
            this.panelPadre = panelPadre;
            
            panel.add(btnEditar);
            panel.add(btnEliminar);
            btnEditar.setPreferredSize(new Dimension(70, 25));
            btnEliminar.setPreferredSize(new Dimension(70, 25));

            btnEditar.addActionListener(e -> {
                fireEditingStopped();
                editarLibro(filaActual);
            });

            btnEliminar.addActionListener(e -> {
                fireEditingStopped();
                eliminarLibro(filaActual);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.filaActual = row;
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        private void editarLibro(int fila) {
            String isbn = (String) modeloTabla.getValueAt(fila, 0);
            Libro libro = gestor.buscarPorISBN(isbn);
            
            if (libro != null) {
                VentanaRegistroLibros dialogo = new VentanaRegistroLibros(
                    ventanaPrincipal, gestor, panelPadre, libro
                );
                dialogo.setVisible(true);
            }
        }

        private void eliminarLibro(int fila) {
            String isbn = (String) modeloTabla.getValueAt(fila, 0);
            String titulo = (String) modeloTabla.getValueAt(fila, 1);
            
            int confirmacion = JOptionPane.showConfirmDialog(panelPadre,
                "¿Está seguro de eliminar el libro: " + titulo + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                String resultado = gestor.eliminarLibro(isbn);
                JOptionPane.showMessageDialog(panelPadre, resultado);
                panelPadre.refrescarTabla();
            }
        }
    }
}