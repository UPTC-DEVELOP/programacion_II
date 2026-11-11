package com.tiendavirtual.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.tiendavirtual.controlador.VentaController;
import com.tiendavirtual.modelo.Venta;
import com.tiendavirtual.negocio.VentaService;
import com.tiendavirtual.repositorio.VentaRepositoryMemoria;

public class VentaFrame extends JFrame {

    private JTextField txtId, txtFecha, txtMonto, txtCliente;
    private JTable tabla;
    private DefaultTableModel modelo;
    private VentaController controller;

    public VentaFrame() {
        VentaRepositoryMemoria repositorio = new VentaRepositoryMemoria();
        VentaService servicio = new VentaService(repositorio);
        controller = new VentaController(servicio);

        setTitle("Gestión de Ventas");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 8, 8));

        txtId = new JTextField();
        txtFecha = new JTextField();
        txtMonto = new JTextField();
        txtCliente = new JTextField();

        formulario.add(new JLabel("ID de venta:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Fecha:"));
        formulario.add(txtFecha);
        formulario.add(new JLabel("Monto total:"));
        formulario.add(txtMonto);
        formulario.add(new JLabel("ID del cliente:"));
        formulario.add(txtCliente);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar");

        formulario.add(btnRegistrar);
        formulario.add(btnConsultar);

        JPanel botones = new JPanel();
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnListar);

        modelo = new DefaultTableModel(
            new String[]{"ID", "Fecha", "Monto", "ID Cliente"}, 0
        );

        tabla = new JTable(modelo);
        tabla.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        // Al seleccionar una fila, cargar sus datos.
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarVentaSeleccionada();
            }
        });

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrar());
        btnConsultar.addActionListener(e -> consultar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnListar.addActionListener(e -> listar());
    }

    private Venta obtenerVenta() {
        String id = txtId.getText().trim();
        String fecha = txtFecha.getText().trim();
        String montoTexto = txtMonto.getText().trim();
        String clienteTexto = txtCliente.getText().trim();

        if (id.isEmpty() || fecha.isEmpty()
                || montoTexto.isEmpty() || clienteTexto.isEmpty()) {
            throw new IllegalArgumentException(
                "Complete todos los campos."
            );
        }

        double monto = Double.parseDouble(montoTexto);
        int cliente = Integer.parseInt(clienteTexto);

        return new Venta(id, fecha, monto, cliente);
    }

    private String obtenerIdSeleccionado() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            return modelo.getValueAt(fila, 0).toString().trim();
        }

        return txtId.getText().trim();
    }

    private void cargarVentaSeleccionada() {
        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        txtId.setText(modelo.getValueAt(fila, 0).toString());
        txtFecha.setText(modelo.getValueAt(fila, 1).toString());
        txtMonto.setText(modelo.getValueAt(fila, 2).toString());
        txtCliente.setText(modelo.getValueAt(fila, 3).toString());
    }

    private void registrar() {
        try {
            controller.registrar(obtenerVenta());

            JOptionPane.showMessageDialog(
                this, "Venta registrada correctamente."
            );

            listar();
            limpiar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void consultar() {
        try {
            String id = obtenerIdSeleccionado();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Seleccione una venta o escriba su ID."
                );
                return;
            }

            Venta venta = controller.consultar(id);

            if (venta == null) {
                JOptionPane.showMessageDialog(
                    this, "No se encontró la venta."
                );
                return;
            }

            txtId.setText(venta.getId());
            txtFecha.setText(venta.getFecha());
            txtMonto.setText(String.valueOf(venta.getMontoTotal()));
            txtCliente.setText(String.valueOf(venta.getIdCliente()));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizar() {
        try {
            controller.actualizar(obtenerVenta());

            JOptionPane.showMessageDialog(
                this, "Venta actualizada correctamente."
            );

            listar();
            limpiar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminar() {
        try {
            String id = obtenerIdSeleccionado();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Seleccione una venta o escriba su ID."
                );
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar esta venta?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                controller.eliminar(id);

                JOptionPane.showMessageDialog(
                    this, "Venta eliminada correctamente."
                );

                listar();
                limpiar();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void listar() {
        modelo.setRowCount(0);

        List<Venta> ventas = controller.listar();

        for (Venta venta : ventas) {
            modelo.addRow(new Object[]{
                venta.getId(),
                venta.getFecha(),
                venta.getMontoTotal(),
                venta.getIdCliente()
            });
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtFecha.setText("");
        txtMonto.setText("");
        txtCliente.setText("");
        tabla.clearSelection();
    }
}