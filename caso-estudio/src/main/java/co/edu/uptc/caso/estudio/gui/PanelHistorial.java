package co.edu.uptc.caso.estudio.gui;

import co.edu.uptc.caso.estudio.model.Compra;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// PanelHistorial: muestra el historial de compras del usuario autenticado.
public class PanelHistorial extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final String ACTUALIZAR_HISTORIAL = "ACTUALIZAR_HISTORIAL";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final DefaultTableModel modeloTabla;
    private final JTable tablaHistorial;
    private final JButton btnActualizar;

    public PanelHistorial(Evento evento) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID Compra", "Fecha", "Método de Pago", "Libros", "Total Pagado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setAutoCreateRowSorter(true);
        add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnActualizar = new JButton("Actualizar Historial");
        btnActualizar.setActionCommand(ACTUALIZAR_HISTORIAL);
        if (evento != null) {
            btnActualizar.addActionListener(evento);
        }
        panelBotones.add(btnActualizar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void actualizarHistorial(List<Compra> compras) {
        modeloTabla.setRowCount(0);
        if (compras == null) {
            return;
        }
        for (int i = 0; i < compras.size(); i++) {
            Compra compra = compras.get(i);
            if (compra != null) {
                Object[] fila = {
                    compra.getIdCompra(),
                    formatearFecha(compra),
                    compra.getMetodoPago(),
                    resumirLibros(compra),
                    String.format("$%.2f", compra.getTotalPagado())
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    private String formatearFecha(Compra compra) {
        if (compra.getFecha() == null) {
            return "-";
        }
        return compra.getFecha().format(FORMATO_FECHA);
    }

    private String resumirLibros(Compra compra) {
        List<ItemCarrito> items = compra.getItems();
        if (items == null || items.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            ItemCarrito item = items.get(i);
            if (item != null && item.getLibro() != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(item.getLibro().getTitulo())
                        .append(" (x")
                        .append(item.getCantidad())
                        .append(")");
            }
        }
        return sb.length() > 0 ? sb.toString() : "-";
    }
}