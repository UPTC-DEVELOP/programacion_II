package co.edu.uptc.gui;

import co.edu.uptc.negocio.Compra;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class PanelHistorialCompras extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {
            "N°", "Fecha", "Tipo", "Método de pago", "Subtotal", "Impuestos", "Desc. Premium", "Total"
    };

    private final DefaultTableModel modelo;
    private final JTable tabla;

    public PanelHistorialCompras() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Historial de compras del cliente"));
        modelo = new DefaultTableModel(COLUMNAS, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
            @Override public Class<?> getColumnClass(int columna) {
                return columna == 0 ? Long.class : Object.class;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    public void mostrarCompras(List<Compra> compras) {
        modelo.setRowCount(0);
        for (Compra compra : compras) {
            modelo.addRow(new Object[] {
                    compra.getId(), UtilidadesGUI.formatearFecha(compra.getFecha()),
                    compra.getTipoCliente().getEtiqueta(), compra.getMetodoPago().getEtiqueta(),
                    UtilidadesGUI.formatearMoneda(compra.getSubtotal()),
                    UtilidadesGUI.formatearMoneda(compra.getImpuestos()),
                    UtilidadesGUI.formatearMoneda(compra.getDescuentoPremium()),
                    UtilidadesGUI.formatearMoneda(compra.getTotal())
            });
        }
    }
}
