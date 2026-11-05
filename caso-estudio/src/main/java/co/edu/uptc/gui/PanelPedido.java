package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelPedido extends JPanel {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTablaPedidos;
    private JButton btnVerDetalle, btnCambiarEstado, btnActualizar;

    public PanelPedido() {
        setLayout(new BorderLayout(10, 10));

        // Configuración de la tabla de pedidos
        String[] columnas = {"ID Pedido", "Cliente", "Fecha", "Total", "Estado"};
        modeloTablaPedidos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTablaPedidos);
        add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        // Panel de botones de acción inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVerDetalle = new JButton("Ver Detalle");
        btnCambiarEstado = new JButton("Cambiar Estado");
        btnActualizar = new JButton("Actualizar Lista");

        btnVerDetalle.addActionListener(e -> verDetallePedido());
        btnCambiarEstado.addActionListener(e -> cambiarEstadoPedido());
        btnActualizar.addActionListener(e -> cargarPedidos());

        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnCambiarEstado);
        panelBotones.add(btnActualizar);

        add(panelBotones, BorderLayout.SOUTH);
        
        cargarPedidos();
    }

    private void verDetallePedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila >= 0) {
            String idPedido = modeloTablaPedidos.getValueAt(fila, 0).toString();
            JOptionPane.showMessageDialog(this, "Mostrando detalles del pedido ID: " + idPedido);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para ver el detalle.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cambiarEstadoPedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila >= 0) {
            JOptionPane.showMessageDialog(this, "Estado del pedido actualizado.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para cambiar su estado.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void cargarPedidos() {
        modeloTablaPedidos.setRowCount(0);
        // ejemplo de fila de prueba:
        // modeloTablaPedidos.addRow(new Object[]{"P-001", "Julian Peaz", "2026-10-09", "$45.000", "Pendiente"});
    }

    // Método para recibir y añadir nuevos pedidos desde el carrito
    public void agregarPedido(String idPedido, String cliente, String fecha, String total, String estado) {
        modeloTablaPedidos.addRow(new Object[]{idPedido, cliente, fecha, total, estado});
    }
}