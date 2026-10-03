package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    private JComboBox<Producto> comboProductos;

    private JTextField txtPrecioBase;
    private JTextField txtStock;
    private JTextField txtDescuento;
    private JTextField txtIVA;
    private JTextField txtCantidad;
    private JTextField txtTotal;

    private JButton btnRegistrar;

    private final double IVA = 0.19;

    public VentanaPrincipal() {

        setTitle("Sistema de Pedidos");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
        cargarProductos();
        configurarEventos();
    }

    private void crearInterfaz() {

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        panel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        panel.add(new JLabel("Producto:"));

        comboProductos = new JComboBox<>();
        panel.add(comboProductos);

        panel.add(new JLabel("Precio Base:"));

        txtPrecioBase = new JTextField();
        txtPrecioBase.setEditable(false);
        panel.add(txtPrecioBase);

        panel.add(new JLabel("Stock Disponible:"));

        txtStock = new JTextField();
        txtStock.setEditable(false);
        panel.add(txtStock);

        panel.add(new JLabel("Descuento:"));

        txtDescuento = new JTextField();
        txtDescuento.setEditable(false);
        panel.add(txtDescuento);

        panel.add(new JLabel("IVA:"));

        txtIVA = new JTextField();
        txtIVA.setEditable(false);
        panel.add(txtIVA);

        panel.add(new JLabel("Cantidad:"));

        txtCantidad = new JTextField();
        panel.add(txtCantidad);

        panel.add(new JLabel("Total a pagar:"));

        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        panel.add(txtTotal);

        btnRegistrar = new JButton("Registrar Pedido");
        panel.add(new JLabel());
        panel.add(btnRegistrar);

        add(panel);
    }

    private void cargarProductos() {

        comboProductos.addItem(
            new Producto("Laptop", 2500000, 10, 0.10)
        );

        comboProductos.addItem(
            new Producto("Mouse", 80000, 20, 0.05)
        );

        comboProductos.addItem(
            new Producto("Teclado", 150000, 15, 0.10)
        );

        comboProductos.addItem(
            new Producto("Monitor", 900000, 8, 0.15)
        );
    }

    private void configurarEventos() {

        comboProductos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Producto producto =
                    (Producto) comboProductos.getSelectedItem();

                if (producto != null) {
                    mostrarDatosProducto(producto);
                }
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                registrarPedido();
            }
        });
    }

    private void mostrarDatosProducto(Producto producto) {

        txtPrecioBase.setText(
            String.format("$ %.2f", producto.getPrecioBase())
        );

        txtStock.setText(
            String.valueOf(producto.getStock())
        );

        txtDescuento.setText(
            String.format(
                "%.0f%%",
                producto.getPorcentajeDescuento() * 100
            )
        );

        txtIVA.setText(
            String.format("%.0f%%", IVA * 100)
        );

        txtTotal.setText("");
    }

    private void registrarPedido() {

        try {

            Producto producto =
                (Producto) comboProductos.getSelectedItem();

            if (producto == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un producto.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int cantidad =
                Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a 0."
                );
            }

            if (cantidad > producto.getStock()) {
                throw new IllegalArgumentException(
                    "Stock insuficiente. Disponible: "
                    + producto.getStock()
                );
            }

            double precioConDescuento =
                producto.calcularPrecioFinal();

            double subtotal =
                precioConDescuento * cantidad;

            double valorIVA =
                subtotal * IVA;

            double total =
                subtotal + valorIVA;

            producto.descontarStock(cantidad);

            txtTotal.setText(
                String.format("$ %.2f", total)
            );

            txtStock.setText(
                String.valueOf(producto.getStock())
            );

            JOptionPane.showMessageDialog(
                this,
                "Pedido registrado correctamente.\n"
                + "Producto: " + producto.getNombre()
                + "\nCantidad: " + cantidad
                + "\nTotal: $ " + String.format("%.2f", total),
                "Pedido registrado",
                JOptionPane.INFORMATION_MESSAGE
            );

            txtCantidad.setText("");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "La cantidad debe ser un número entero.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventana =
                new VentanaPrincipal();

            ventana.setVisible(true);
        });
    }
}