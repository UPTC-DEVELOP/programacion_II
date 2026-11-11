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
        double monto = Double.parseDouble(txtMonto.getText().trim());
        int cliente = Integer.parseInt(txtCliente.getText().trim());

        return new Venta(id, fecha, monto, cliente);
    }

    private void registrar() {
        try {
            controller.registrar(obtenerVenta());
            JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");
            listar();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultar() {
        Venta venta = controller.consultar(txtId.getText().trim());

        if (venta == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la venta.");
            return;
        }

        txtFecha.setText(venta.getFecha());
        txtMonto.setText(String.valueOf(venta.getMontoTotal()));
        txtCliente.setText(String.valueOf(venta.getIdCliente()));
    }

    private void actualizar() {
        try {
            controller.actualizar(obtenerVenta());
            JOptionPane.showMessageDialog(this, "Venta actualizada correctamente.");
            listar();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        try {
            controller.eliminar(txtId.getText().trim());
            JOptionPane.showMessageDialog(this, "Venta eliminada correctamente.");
            listar();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
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
    }
}