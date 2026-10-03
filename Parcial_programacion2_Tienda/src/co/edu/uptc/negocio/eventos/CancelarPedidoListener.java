package co.edu.uptc.negocio.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.modelo.Producto;

public class CancelarPedidoListener implements ActionListener {
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private JComboBox<Producto> comboProductos;

    public CancelarPedidoListener(JTable tablaHistorial, DefaultTableModel modeloTabla, JComboBox<Producto> comboProductos) {
        this.tablaHistorial = tablaHistorial;
        this.modeloTabla = modeloTabla;
        this.comboProductos = comboProductos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int filaSeleccionada = tablaHistorial.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un pedido de la tabla para cancelar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        int cantidad = (int) modeloTabla.getValueAt(filaSeleccionada, 1);

        // Restablecer stock
        for (int i = 0; i < comboProductos.getItemCount(); i++) {
            Producto prod = comboProductos.getItemAt(i);
            if (prod.getNombre().equals(nombreProducto)) {
                prod.aumentarStock(cantidad);
                break;
            }
        }

        // Eliminar registro de la tabla
        modeloTabla.removeRow(filaSeleccionada);
        JOptionPane.showMessageDialog(null, "Pedido cancelado y stock restaurado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}