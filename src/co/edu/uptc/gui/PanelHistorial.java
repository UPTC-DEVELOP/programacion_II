package co.edu.uptc.gui;

import co.edu.uptc.negocio.Pedido;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


public class PanelHistorial extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {"N°", "Fecha", "ISBN", "Producto", "Cant.",
            "Precio unit. final", "Total"};
    private static final int COLUMNA_ID = 0;

    private JTable tblHistorial;
    private DefaultTableModel modeloTabla;
    private JButton btnCancelar;

    public PanelHistorial() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createTitledBorder("Historial de Transacciones"));

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int fila, int columna) {
                return false;
            }

            public Class<?> getColumnClass(int columna) {
                
                return columna == COLUMNA_ID ? Long.class : Object.class;
            }
        };
        tblHistorial = new JTable(modeloTabla);
        tblHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHistorial.setAutoCreateRowSorter(true);
        tblHistorial.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblHistorial.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblHistorial.getColumnModel().getColumn(2).setPreferredWidth(110);
        tblHistorial.getColumnModel().getColumn(3).setPreferredWidth(220);
        tblHistorial.getColumnModel().getColumn(4).setPreferredWidth(50);
        tblHistorial.getColumnModel().getColumn(5).setPreferredWidth(120);
        tblHistorial.getColumnModel().getColumn(6).setPreferredWidth(110);

        btnCancelar = new JButton("Cancelar Pedido Seleccionado");
        btnCancelar.setActionCommand(Comandos.CANCELAR_PEDIDO);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        panelBoton.add(btnCancelar);

        add(new JScrollPane(tblHistorial), BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    public void agregarListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public void mostrarPedidos(List<Pedido> pedidos) {
        modeloTabla.setRowCount(0);
        for (Pedido pedido : pedidos) {
            agregarPedido(pedido);
        }
    }

    public void agregarPedido(Pedido pedido) {
        modeloTabla.addRow(new Object[] {
                pedido.getId(),
                UtilidadesGUI.formatearFecha(pedido.getFecha()),
                pedido.getIsbn(),
                pedido.getTitulo(),
                pedido.getCantidad(),
                UtilidadesGUI.formatearMoneda(pedido.getPrecioUnitarioFinal()),
                UtilidadesGUI.formatearMoneda(pedido.getTotal())
        });
    }

    public long getIdPedidoSeleccionado() {
        int fila = tblHistorial.getSelectedRow();
        if (fila < 0) {
            return -1;
        }
        int filaModelo = tblHistorial.convertRowIndexToModel(fila);
        return (Long) modeloTabla.getValueAt(filaModelo, COLUMNA_ID);
    }

    public void eliminarPedido(long idPedido) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if ((Long) modeloTabla.getValueAt(i, COLUMNA_ID) == idPedido) {
                modeloTabla.removeRow(i);
                return;
            }
        }
    }
}
