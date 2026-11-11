package com.tiendavirtual.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.negocio.GestionCliente;

public class GestionClientesFrame extends JFrame {

    private GestionCliente gestionCliente;
    private DefaultTableModel modelo;
    private JTable tabla;

    public GestionClientesFrame(GestionCliente gestionCliente) {

        this.gestionCliente = gestionCliente;

        setTitle("GESTIÓN DE CLIENTES");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(
            new String[]{"Tipo documento", "Documento", "Nombre",
                "Apellido", "Correo", "Teléfono"}, 0) {

            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        JButton btnListar = new JButton("Actualizar lista");
        JButton btnEliminar = new JButton("Eliminar cliente");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnListar);
        botones.add(btnEliminar);
        botones.add(btnCerrar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnListar.addActionListener(e -> listarClientes());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnCerrar.addActionListener(e -> dispose());

        listarClientes();
    }

    private void listarClientes() {
        modelo.setRowCount(0);

        for (Cliente cliente : gestionCliente.listarClientes()) {
            modelo.addRow(new Object[]{
                cliente.getTipoDocumento(),
                cliente.getNumeroDocumento(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCorreo(),
                cliente.getTelefono()
            });
        }
    }

    private void eliminarCliente() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un cliente de la tabla.");
            return;
        }

        int documento = Integer.parseInt(
            modelo.getValueAt(fila, 1).toString());

        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Desea eliminar este cliente?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {

            if (gestionCliente.eliminarCliente(documento)) {
                JOptionPane.showMessageDialog(this,
                    "Cliente eliminado correctamente.");
                listarClientes();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el cliente.");
            }
        }
    }
}