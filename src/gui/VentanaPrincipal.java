package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import negocio.ControladorProducto;
import negocio.Producto;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private ControladorProducto controlador;

    private JComboBox<Producto> cmbProductos;
    private JLabel lblPrecioBase;
    private JLabel lblStock;
    private JLabel lblDescuento;
    private JLabel lblIva;
    private JTextField txtCantidad;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VentanaPrincipal(ControladorProducto controlador) {
        this.controlador = controlador;

        setTitle("Gestion de Productos - Grupo 5");
        setSize(780, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearPanelDatos(), BorderLayout.NORTH);
        add(new JScrollPane(crearTabla()), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        cargarProductos();
        mostrarProducto();
    }

    private JPanel crearPanelDatos() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 6, 6));

        cmbProductos = new JComboBox<Producto>();
        lblPrecioBase = new JLabel("-");
        lblStock = new JLabel("-");
        lblDescuento = new JLabel("-");
        lblIva = new JLabel("-");
        txtCantidad = new JTextField();

        panel.add(new JLabel("Producto:"));
        panel.add(cmbProductos);
        panel.add(new JLabel("Precio base:"));
        panel.add(lblPrecioBase);

        panel.add(new JLabel("Stock:"));
        panel.add(lblStock);
        panel.add(new JLabel("Descuento:"));
        panel.add(lblDescuento);

        panel.add(new JLabel("IVA:"));
        panel.add(lblIva);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);

        cmbProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarProducto();
            }
        });

        return panel;
    }

    private JTable crearTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[] { "Producto", "Cantidad", "Total" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        return tabla;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();

        JButton btnRegistrar = new JButton("Registrar Pedido");
        JButton btnCancelar = new JButton("Cancelar Pedido Seleccionado");

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPedido();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarPedido();
            }
        });

        panel.add(btnRegistrar);
        panel.add(btnCancelar);
        return panel;
    }

    private void cargarProductos() {
        for (Producto producto : controlador.listar()) {
            cmbProductos.addItem(producto);
        }
    }

    private void mostrarProducto() {
        Producto producto = (Producto) cmbProductos.getSelectedItem();
        if (producto == null) {
            return;
        }
        lblPrecioBase.setText(String.valueOf(producto.getPrecioBase()));
        lblStock.setText(String.valueOf(producto.getStock()));
        lblDescuento.setText(String.valueOf(producto.getPorcentajeDescuento()));
        lblIva.setText(String.valueOf(producto.getImpuestoIVA()));
    }

    private void registrarPedido() {
        Producto producto = (Producto) cmbProductos.getSelectedItem();
        if (producto == null) {
            return;
        }
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.");
                return;
            }
            if (cantidad > producto.getStock()) {
                JOptionPane.showMessageDialog(this,
                        "No hay stock suficiente. Disponible: " + producto.getStock());
                return;
            }

            double total = cantidad * producto.calcularPrecioFinal();
            producto.setStock(producto.getStock() - cantidad);
            modeloTabla.addRow(new Object[] { producto, cantidad, total });
            txtCantidad.setText("");
            mostrarProducto();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad numerica valida.");
        }
    }

    private void cancelarPedido() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido de la tabla.");
            return;
        }
        Producto producto = (Producto) modeloTabla.getValueAt(fila, 0);
        int cantidad = (int) modeloTabla.getValueAt(fila, 1);

        producto.setStock(producto.getStock() + cantidad);
        modeloTabla.removeRow(fila);
        mostrarProducto();
    }
}