package co.edu.uptc.tienda.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.modelo.Producto;

public class RegistrarPedidoListener implements ActionListener {
    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad;
    private JLabel lblStock;
    private DefaultTableModel modeloTabla;

    public RegistrarPedidoListener(JComboBox<Producto> comboProductos, JTextField txtCantidad,
                                   JLabel lblStock, DefaultTableModel modeloTabla) {
        this.comboProductos = comboProductos;
        this.txtCantidad = txtCantidad;
        this.lblStock = lblStock;
        this.modeloTabla = modeloTabla;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Producto productoSeleccionado = (Producto) comboProductos.getSelectedItem();
        if (productoSeleccionado == null) return;

        try {
            // Manejo de excepción por formato no numérico
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validación de stock
            if (cantidad > productoSeleccionado.getStock()) {
                JOptionPane.showMessageDialog(null, "Stock insuficiente. Disponible: " + productoSeleccionado.getStock(), 
                        "Error de Stock", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Cálculo del total
            double total = productoSeleccionado.calcularPrecioFinal(cantidad);

            // Actualización de stock (Integración con Módulo C)
            productoSeleccionado.reducirStock(cantidad);
            lblStock.setText(String.valueOf(productoSeleccionado.getStock()));

            // Agregar al historial en la tabla (Integración con Módulo C)
            Object[] fila = {productoSeleccionado.getNombre(), cantidad, String.format("$%.2f", total)};
            modeloTabla.addRow(fila);

            // Limpieza y confirmación
            txtCantidad.setText("");
            JOptionPane.showMessageDialog(null, "Pedido registrado exitosamente.\nTotal a pagar: $" + String.format("%.2f", total), 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            // Alerta visual sin bloquear ni cerrar la aplicación
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un valor numérico válido para la cantidad.", 
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}