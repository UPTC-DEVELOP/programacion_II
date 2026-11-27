package co.edu.uptc.caso.estudio.gui;

import co.edu.uptc.caso.estudio.model.Libro;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelLibros extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int COLUMNA_ISBN = 0;
    private static final int COLUMNA_STOCK = 8;
    private static final int UMBRAL_STOCK_BAJO = 3;
    private static final Color COLOR_FONDO_STOCK_BAJO = new Color(255, 230, 230);
    private static final String[] COLUMNAS = {
        "ISBN", "Título", "Autor", "Año", "Categoría", "Editorial",
        "Páginas", "Precio", "Stock", "Formato", "IVA %"
    };

    private DefaultTableModel modeloTablaLibros;
    private JTable tablaLibros;
    private JTextField txtBuscarLibro;

    public PanelLibros(Evento evento) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentes(evento);
    }

    private void initComponentes(Evento evento) {
        add(crearPanelBusqueda(evento), BorderLayout.NORTH);
        add(new JScrollPane(crearTablaLibros()), BorderLayout.CENTER);
        add(crearPanelBotones(evento), BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda(Evento evento) {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar (Título, Autor, ISBN, Categoría):"));

        txtBuscarLibro = new JTextField(20);
        txtBuscarLibro.setActionCommand(Evento.BUSCAR_LIBRO);
        txtBuscarLibro.addActionListener(evento);

        panelBusqueda.add(txtBuscarLibro);
        panelBusqueda.add(crearBoton("Buscar", Evento.BUSCAR_LIBRO, evento));
        panelBusqueda.add(crearBoton("Mostrar Todos", Evento.MOSTRAR_TODOS_LIBROS, evento));
        return panelBusqueda;
    }

    private JTable crearTablaLibros() {
        modeloTablaLibros = new DefaultTableModel(COLUMNAS, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTablaLibros);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLibros.setAutoCreateRowSorter(true);
        tablaLibros.setDefaultRenderer(Object.class, crearRendererStock());
        return tablaLibros;
    }

    private DefaultTableCellRenderer crearRendererStock() {
        return new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                int modelRow = table.convertRowIndexToModel(row);
                Object stockObj = table.getModel().getValueAt(modelRow, COLUMNA_STOCK);
                int stock = (stockObj instanceof Number) ? ((Number) stockObj).intValue() : 0;

                if (!isSelected) {
                    boolean stockBajo = stock <= UMBRAL_STOCK_BAJO;
                    c.setBackground(stockBajo ? COLOR_FONDO_STOCK_BAJO : Color.WHITE);
                    c.setForeground(stockBajo ? Color.RED : Color.BLACK);
                }
                return c;
            }
        };
    }

    private JPanel crearPanelBotones(Evento evento) {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(crearBoton("Agregar Libro", Evento.AGREGAR_LIBRO, evento));
        panelBotones.add(crearBoton("Editar Libro", Evento.EDITAR_LIBRO, evento));
        panelBotones.add(crearBoton("Eliminar Seleccionado", Evento.ELIMINAR_LIBRO, evento));
        panelBotones.add(crearBoton("Agregar al Carrito", Evento.AGREGAR_AL_CARRITO, evento));
        return panelBotones;
    }

    private JButton crearBoton(String texto, String comando, Evento evento) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(comando);
        btn.addActionListener(evento);
        return btn;
    }

    public void actualizarTabla(List<Libro> lista) {
        modeloTablaLibros.setRowCount(0);
        if (lista == null) {
            return;
        }
        for (Libro l : lista) {
            if (l == null) {
                continue;
            }
            modeloTablaLibros.addRow(new Object[]{
                l.getIsbn(),
                l.getTitulo(),
                l.getAutor(),
                l.getAnio(),
                l.getCategoria(),
                l.getEditorial(),
                l.getPaginas(),
                l.getPrecioVenta(),
                l.getStock(),
                l.getFormato(),
                l.getPorcentajeIva()
            });
        }
    }

    public String getTextoBusqueda() {
        return txtBuscarLibro.getText().trim().toLowerCase();
    }

    public void limpiarBusqueda() {
        txtBuscarLibro.setText("");
    }

    public String getIsbnSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        int filaModelo = tablaLibros.convertRowIndexToModel(fila);
        Object valor = modeloTablaLibros.getValueAt(filaModelo, COLUMNA_ISBN);
        return valor != null ? valor.toString() : null;
    }
}