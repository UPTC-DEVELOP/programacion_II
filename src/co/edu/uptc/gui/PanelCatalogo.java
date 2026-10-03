package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PanelCatalogo extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {"ISBN", "Título", "Autor", "Año", "Categoría",
            "Editorial", "Págs.", "Precio base", "Precio c/IVA", "Stock", "Formato"};
    private static final int[] ANCHOS = {105, 190, 140, 45, 85, 110, 45, 85, 90, 45, 65};

    private JLabel lblBuscar;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnMostrarTodos;
    private JTable tblLibros;
    private DefaultTableModel modeloTabla;
    private JLabel lblResumen;

    public PanelCatalogo() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createTitledBorder("Catálogo de Libros"));

        // ---- Barra de búsqueda (FlowLayout) ----
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        lblBuscar = new JLabel("Buscar (título, autor o categoría):");
        txtBuscar = new JTextField(20);
        txtBuscar.setActionCommand(Comandos.BUSCAR); // Enter dentro del campo también busca
        btnBuscar = new JButton("Buscar");
        btnBuscar.setActionCommand(Comandos.BUSCAR);
        btnMostrarTodos = new JButton("Mostrar todos");
        btnMostrarTodos.setActionCommand(Comandos.MOSTRAR_TODOS);
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnMostrarTodos);

        // ---- Tabla de solo lectura ----
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblLibros = new JTable(modeloTabla);
        tblLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblLibros.setAutoCreateRowSorter(true);
        tblLibros.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < ANCHOS.length; i++) {
            tblLibros.getColumnModel().getColumn(i).setPreferredWidth(ANCHOS[i]);
        }

        lblResumen = new JLabel(" ");
        lblResumen.setBorder(BorderFactory.createEmptyBorder(0, 6, 4, 0));

        add(panelBusqueda, BorderLayout.NORTH);
        add(new JScrollPane(tblLibros), BorderLayout.CENTER);
        add(lblResumen, BorderLayout.SOUTH);
    }

    // ===================== Eventos =====================

    public void agregarListener(ActionListener listener) {
        txtBuscar.addActionListener(listener);
        btnBuscar.addActionListener(listener);
        btnMostrarTodos.addActionListener(listener);
    }

    public void agregarSeleccionListener(ListSelectionListener listener) {
        tblLibros.getSelectionModel().addListSelectionListener(listener);
    }

    // ===================== Lectura / escritura visual =====================

    public String getCriterioBusqueda() {
        return txtBuscar.getText().trim();
    }

    public void limpiarBusqueda() {
        txtBuscar.setText("");
    }

    public String getIsbnSeleccionado() {
        int fila = tblLibros.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        int filaModelo = tblLibros.convertRowIndexToModel(fila);
        return String.valueOf(modeloTabla.getValueAt(filaModelo, 0));
    }

    public void limpiarSeleccion() {
        tblLibros.clearSelection();
    }

    public void mostrarLibros(List<Libro> libros) {
        modeloTabla.setRowCount(0);
        for (Libro l : libros) {
            modeloTabla.addRow(new Object[] {
                    l.getIsbn(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getAnioPublicacion(),
                    l.getCategoria(),
                    l.getEditorial(),
                    l.getNumeroPaginas() > 0 ? String.valueOf(l.getNumeroPaginas()) : "-",
                    UtilidadesGUI.formatearMoneda(l.getPrecioBase()),
                    UtilidadesGUI.formatearMoneda(l.getPrecioVenta()),
                    l.getCantidadDisponible(),
                    l.getFormato().getEtiqueta()
            });
        }
        lblResumen.setText(libros.isEmpty()
                ? "No se encontraron libros para el criterio indicado."
                : libros.size() + " libro(s) en el listado.");
    }
}
