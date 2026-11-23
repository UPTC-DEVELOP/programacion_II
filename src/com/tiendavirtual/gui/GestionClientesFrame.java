package com.tiendavirtual.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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
            new String[]{
                "Tipo documento",
                "Documento",
                "Nombre",
                "Apellido",
                "Correo",
                "Teléfono"
            }, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        JButton btnListar = new JButton("Actualizar lista");
        JButton btnActualizar = new JButton("Actualizar datos");
        JButton btnEliminar = new JButton("Eliminar cliente");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel(new FlowLayout());

        botones.add(btnListar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnCerrar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnListar.addActionListener(e -> listarClientes());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnCerrar.addActionListener(e -> dispose());

        listarClientes();
    }

    // LISTAR CLIENTES
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

    // ACTUALIZAR CLIENTE
    private void actualizarCliente() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un cliente de la tabla.");
            return;
        }

        int documentoOriginal = Integer.parseInt(
            modelo.getValueAt(fila, 1).toString());

        Cliente clienteActual = gestionCliente.buscarPorDocumento(
            documentoOriginal);

        if (clienteActual == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró el cliente.");
            return;
        }

        JComboBox<String> tipoDocumento = new JComboBox<>(
            new String[]{"CC", "TI", "CE", "Pasaporte"});

        tipoDocumento.setSelectedItem(
            clienteActual.getTipoDocumento());

        JTextField numeroDocumento = new JTextField(
            String.valueOf(clienteActual.getNumeroDocumento()));

        JTextField nombre = new JTextField(
            clienteActual.getNombre());

        JTextField apellido = new JTextField(
            clienteActual.getApellido());

        JTextField correo = new JTextField(
            clienteActual.getCorreo());

        JTextField telefono = new JTextField(
            clienteActual.getTelefono());

        JPasswordField contrasenia = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));

        panel.add(new JLabel("Tipo documento:"));
        panel.add(tipoDocumento);

        panel.add(new JLabel("Número documento:"));
        panel.add(numeroDocumento);

        panel.add(new JLabel("Nombre:"));
        panel.add(nombre);

        panel.add(new JLabel("Apellido:"));
        panel.add(apellido);

        panel.add(new JLabel("Correo:"));
        panel.add(correo);

        panel.add(new JLabel("Teléfono:"));
        panel.add(telefono);

        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(contrasenia);

        int resultado = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Actualizar cliente",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            int nuevoDocumento = Integer.parseInt(
                numeroDocumento.getText().trim());

            // Por ahora conservamos el documento original.
            // Así evitamos perder la referencia del registro.
            if (nuevoDocumento != documentoOriginal) {
                JOptionPane.showMessageDialog(this,
                    "Por ahora no cambie el número de documento.");
                return;
            }

            String nuevaContrasenia =
                new String(contrasenia.getPassword());

            if (nuevaContrasenia.isEmpty()) {
                nuevaContrasenia = clienteActual.getContrasenia();
            }

            Cliente actualizado = new Cliente(
                tipoDocumento.getSelectedItem().toString(),
                nuevoDocumento,
                nombre.getText().trim(),
                apellido.getText().trim(),
                correo.getText().trim(),
                telefono.getText().trim(),
                nuevaContrasenia
            );

            if (gestionCliente.actualizarCliente(actualizado)) {

                JOptionPane.showMessageDialog(this,
                    "Cliente actualizado correctamente.");

                listarClientes();

            } else {

                JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el cliente. "
                    + "Verifique los datos o el correo.");
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                "El número de documento debe ser válido.");
        }
    }

    // ELIMINAR CLIENTE
    private void eliminarCliente() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un cliente de la tabla.");
            return;
        }

        int documento = Integer.parseInt(
            modelo.getValueAt(fila, 1).toString());

        int confirmar = JOptionPane.showConfirmDialog(
            this,
            "¿Desea eliminar este cliente?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );

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