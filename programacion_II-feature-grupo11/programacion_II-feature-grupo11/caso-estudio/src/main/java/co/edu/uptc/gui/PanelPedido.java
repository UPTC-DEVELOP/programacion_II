package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelPedido extends JPanel {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTablaPedidos;
    private JButton btnRealizarCompra, btnVerDetalle, btnCambiarEstado, btnActualizar;
    private int contadorPedido = 1;

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
        btnRealizarCompra = new JButton("Realizar Compra");
        btnVerDetalle = new JButton("Ver Detalle");
        btnCambiarEstado = new JButton("Cambiar Estado");
        btnActualizar = new JButton("Actualizar Lista");

        // ActionCommands para el Controlador central (MVC)
        btnRealizarCompra.setActionCommand("REALIZAR_COMPRA");
        btnVerDetalle.setActionCommand("VER_DETALLE_PEDIDO");
        btnCambiarEstado.setActionCommand("CAMBIAR_ESTADO_PEDIDO");
        btnActualizar.setActionCommand("ACTUALIZAR_PEDIDOS");

        // Eventos locales
        btnRealizarCompra.addActionListener(e -> formularioRealizarCompra());
        btnVerDetalle.addActionListener(e -> verDetallePedido());
        btnCambiarEstado.addActionListener(e -> cambiarEstadoPedido());
        btnActualizar.addActionListener(e -> cargarPedidos());

        // Adición de botones al panel inferior
        panelBotones.add(btnRealizarCompra);
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnCambiarEstado);
        panelBotones.add(btnActualizar);

        add(panelBotones, BorderLayout.SOUTH);
        
        cargarPedidos();
    }

    // Proceso para registrar una compra directamente desde este panel
    private void formularioRealizarCompra() {
        String cliente = JOptionPane.showInputDialog(this, "Ingrese el nombre del cliente:", "Realizar Compra", JOptionPane.QUESTION_MESSAGE);
        
        if (cliente != null && !cliente.trim().isEmpty()) {
            String totalStr = JOptionPane.showInputDialog(this, "Ingrese el valor total de la compra ($):", "Realizar Compra", JOptionPane.QUESTION_MESSAGE);
            
            if (totalStr != null && !totalStr.trim().isEmpty()) {
                String idPedido = String.format("PED-%03d", contadorPedido++);
                String fechaActual = LocalDate.now().toString();
                String totalFormateado = "$" + totalStr.trim();
                
                agregarPedido(idPedido, cliente.trim(), fechaActual, totalFormateado, "Pendiente");
                JOptionPane.showMessageDialog(this, "¡Compra registrada con éxito!\nID de Pedido: " + idPedido, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void verDetallePedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila >= 0) {
            String idPedido = modeloTablaPedidos.getValueAt(fila, 0).toString();
            String cliente = modeloTablaPedidos.getValueAt(fila, 1).toString();
            String fecha = modeloTablaPedidos.getValueAt(fila, 2).toString();
            String total = modeloTablaPedidos.getValueAt(fila, 3).toString();
            String estado = modeloTablaPedidos.getValueAt(fila, 4).toString();

            String detalle = String.format("ID Pedido: %s\nCliente: %s\nFecha: %s\nTotal: %s\nEstado: %s", 
                                           idPedido, cliente, fecha, total, estado);
            JOptionPane.showMessageDialog(this, detalle, "Detalle del Pedido", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para ver el detalle.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cambiarEstadoPedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila >= 0) {
            String[] opciones = {"Pendiente", "Enviado", "Entregado", "Cancelado"};
            String estadoActual = modeloTablaPedidos.getValueAt(fila, 4).toString();
            
            String nuevoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el nuevo estado del pedido:",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                estadoActual
            );

            if (nuevoEstado != null) {
                modeloTablaPedidos.setValueAt(nuevoEstado, fila, 4);
                JOptionPane.showMessageDialog(this, "Estado actualizado a: " + nuevoEstado);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para cambiar su estado.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void cargarPedidos() {
        // Mantiene la lista limpia o cargará desde data/pedidos.json
    }

    // Método invocado manualmente o desde PanelCarrito.realizarCompra()
    public void agregarPedido(String idPedido, String cliente, String fecha, String total, String estado) {
        modeloTablaPedidos.addRow(new Object[]{idPedido, cliente, fecha, total, estado});
    }

    // Asignación de oyentes externos para el Controlador principal (nombre pluralizado)
    public void agregarOyentes(ActionListener listener) {
        btnRealizarCompra.addActionListener(listener);
        btnVerDetalle.addActionListener(listener);
        btnCambiarEstado.addActionListener(listener);
        btnActualizar.addActionListener(listener);
    }

    public String getSelectedIdPedido() {
        int fila = tablaPedidos.getSelectedRow();
        return (fila >= 0) ? modeloTablaPedidos.getValueAt(fila, 0).toString() : null;
    }
}