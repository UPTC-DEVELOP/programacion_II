package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import co.edu.uptc.model.Libro;

public class PanelLibros extends JPanel {
    private static final long serialVersionUID = 1L;

    private DefaultTableModel modeloTablaLibros;
    private JTable tablaLibros;
    private JTextField txtBuscarLibro;

    public PanelLibros(Evento evento) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentes(evento);
    }

    private void initComponentes(Evento evento) {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar (Título, Autor, ISBN, Categoría):"));
        txtBuscarLibro = new JTextField(20);

        JButton btnBuscar = crearBoton("Buscar", Evento.BUSCAR_LIBRO, evento);
        JButton btnLimpiar = crearBoton("Mostrar Todos", Evento.MOSTRAR_TODOS_LIBROS, evento);

        panelBusqueda.add(txtBuscarLibro);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnLimpiar);
        add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Categoría", "Editorial", "Páginas", "Precio", "Stock", "Formato", "IVA %"};
        modeloTablaLibros = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaLibros = new JTable(modeloTablaLibros);
        tablaLibros.setAutoCreateRowSorter(true);
        tablaLibros.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int modelRow = table.convertRowIndexToModel(row);
                int stock = (int) table.getModel().getValueAt(modelRow, 8);
                if (!isSelected) {
                    c.setBackground(stock <= 3 ? new Color(255, 230, 230) : Color.WHITE);
                    c.setForeground(stock <= 3 ? Color.RED : Color.BLACK);
                }
                return c;
            }
        });

        add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(crearBoton("Agregar Libro", Evento.AGREGAR_LIBRO, evento));
        panelBotones.add(crearBoton("Editar Libro", Evento.EDITAR_LIBRO, evento));
        panelBotones.add(crearBoton("Eliminar Seleccionado", Evento.ELIMINAR_LIBRO, evento));
        panelBotones.add(crearBoton("Agregar al Carrito", Evento.AGREGAR_AL_CARRITO, evento));
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, String comando, Evento evento) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(comando);
        btn.addActionListener(evento);
        return btn;
    }

    public void actualizarTabla(List<Libro> lista) {
        modeloTablaLibros.setRowCount(0);
        if (lista != null) {
            for (Libro l : lista) {
                modeloTablaLibros.addRow(new Object[]{
                    l.getIsbn(), l.getTitulo(), l.getAutor(), l.getAnio(),
                    l.getCategoria(), l.getEditorial(), l.getPaginas(),
                    l.getPrecioVenta(), l.getStock(), l.getFormato(), l.getPorcentajeIva()
                });
            }
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
        if (fila < 0) return null;
        int filaModelo = tablaLibros.convertRowIndexToModel(fila);
        return (String) modeloTablaLibros.getValueAt(filaModelo, 0);
    }
}
