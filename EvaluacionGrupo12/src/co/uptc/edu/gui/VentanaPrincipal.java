package co.uptc.edu.gui;

import co.uptc.edu.negocio.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private JComboBox<Producto> comboProductos;
    private JLabel lblPrecioBase, lblStock, lblDescuento, lblIVA, lblPrecioFinal;
    private JTextField txtCantidad;
    private JButton btnRegistrar, btnCancelarPedido;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private List<Producto> listaProductos;
    private List<DetallePedido> listaPedidos;
    private static int contadorPedidos = 1;

    private static class DetallePedido {
        String idPedido;
        Producto producto;
        int cantidad;
        double totalPagado;

        public DetallePedido(String idPedido, Producto producto, int cantidad, double totalPagado) {
            this.idPedido = idPedido;
            this.producto = producto;
            this.cantidad = cantidad;
            this.totalPagado = totalPagado;
        }
    }

    public VentanaPrincipal() {
        super("Control de Inventario y Registro de Ventas - UPTC");
        inicializarDatos();
        configurarVentana();
        construirInterfaz();
        registrarEventos();
        actualizarDetallesProducto();
    }

    private void inicializarDatos() {
        listaProductos = new ArrayList<>();
        listaPedidos = new ArrayList<>();

        listaProductos.add(new Producto("P001", "Laptop Gamer", 1200.0, 10, 10.0, 19.0));
        listaProductos.add(new Producto("P002", "Mouse Inalámbrico", 25.0, 30, 5.0, 19.0));
        listaProductos.add(new Producto("P003", "Teclado Mecánico", 85.0, 15, 12.0, 19.0));
        listaProductos.add(new Producto("P004", "Monitor 27''", 300.0, 8, 15.0, 19.0));
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}
    }

    private void construirInterfaz() {
        JPanel panelContenedor = new JPanel(new BorderLayout(10, 10));
        panelContenedor.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Detalles del Producto Seleccionado",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 13)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelSuperior.add(new JLabel("Seleccionar Producto:"), gbc);

        comboProductos = new JComboBox<>(listaProductos.toArray(new Producto[0]));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 3;
        panelSuperior.add(comboProductos, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        panelSuperior.add(new JLabel("Precio Base:"), gbc);
        lblPrecioBase = new JLabel("$0.00");
        lblPrecioBase.setFont(lblPrecioBase.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 1; gbc.gridy = 1;
        panelSuperior.add(lblPrecioBase, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelSuperior.add(new JLabel("Stock Disponible:"), gbc);
        lblStock = new JLabel("0");
        lblStock.setFont(lblStock.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 3; gbc.gridy = 1;
        panelSuperior.add(lblStock, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelSuperior.add(new JLabel("Descuento:"), gbc);
        lblDescuento = new JLabel("0%");
        gbc.gridx = 1; gbc.gridy = 2;
        panelSuperior.add(lblDescuento, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        panelSuperior.add(new JLabel("IVA:"), gbc);
        lblIVA = new JLabel("0%");
        gbc.gridx = 3; gbc.gridy = 2;
        panelSuperior.add(lblIVA, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelSuperior.add(new JLabel("Precio Unitario Final:"), gbc);
        lblPrecioFinal = new JLabel("$0.00");
        lblPrecioFinal.setForeground(new Color(0, 102, 204));
        lblPrecioFinal.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3;
        panelSuperior.add(lblPrecioFinal, gbc);

        panelContenedor.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFormulario.add(new JLabel("Cantidad a Comprar:"));

        txtCantidad = new JTextField(8);
        panelFormulario.add(txtCantidad);

        btnRegistrar = new JButton("Registrar Pedido");
        btnRegistrar.setBackground(new Color(40, 167, 69));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        panelFormulario.add(btnRegistrar);

        panelCentro.add(panelFormulario, BorderLayout.NORTH);

        String[] columnas = {"ID Pedido", "Producto", "Cantidad", "Precio Unit. Final", "Total Pagado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setRowHeight(24);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Historial de Transacciones"));

        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        panelContenedor.add(panelCentro, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelarPedido = new JButton("Cancelar Pedido Seleccionado");
        btnCancelarPedido.setBackground(new Color(220, 53, 69));
        btnCancelarPedido.setForeground(Color.WHITE);
        btnCancelarPedido.setFocusPainted(false);
        panelInferior.add(btnCancelarPedido);

        panelContenedor.add(panelInferior, BorderLayout.SOUTH);

        add(panelContenedor);
    }

    private void registrarEventos() {
        comboProductos.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    actualizarDetallesProducto();
                }
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarRegistroPedido();
            }
        });

        btnCancelarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarCancelacionPedido();
            }
        });
    }

    private void actualizarDetallesProducto() {
        Producto seleccionado = (Producto) comboProductos.getSelectedItem();
        if (seleccionado != null) {
            lblPrecioBase.setText(String.format("$%.2f", seleccionado.getPrecioBase()));
            lblStock.setText(String.valueOf(seleccionado.getStock()));
            lblDescuento.setText(String.format("%.1f%%", seleccionado.getPorcentajeDescuento()));
            lblIVA.setText(String.format("%.1f%%", seleccionado.getImpuestoIVA()));
            lblPrecioFinal.setText(String.format("$%.2f", seleccionado.calcularPrecioFinal()));
        }
    }

    private void procesarRegistroPedido() {
        Producto seleccionado = (Producto) comboProductos.getSelectedItem();

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            }

            if (cantidad > seleccionado.getStock()) {
                JOptionPane.showMessageDialog(this,
                        "Stock insuficiente. Stock actual: " + seleccionado.getStock(),
                        "Advertencia de Inventario",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            seleccionado.setStock(seleccionado.getStock() - cantidad);

            String idPedido = "PED-" + String.format("%03d", contadorPedidos++);
            double totalPagado = seleccionado.calcularPrecioFinal() * cantidad;

            DetallePedido nuevoPedido = new DetallePedido(idPedido, seleccionado, cantidad, totalPagado);
            listaPedidos.add(nuevoPedido);

            Object[] fila = {
                    idPedido,
                    seleccionado.getNombre(),
                    cantidad,
                    String.format("$%.2f", seleccionado.calcularPrecioFinal()),
                    String.format("$%.2f", totalPagado)
            };
            modeloTabla.addRow(fila);

            txtCantidad.setText("");
            actualizarDetallesProducto();

            JOptionPane.showMessageDialog(this, "Pedido registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número entero válido para la cantidad.",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error de Entrada",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void procesarCancelacionPedido() {
        int filaSeleccionada = tablaHistorial.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un pedido de la tabla para cancelar.",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetallePedido pedidoACancelar = listaPedidos.get(filaSeleccionada);
        Producto producto = pedidoACancelar.producto;

        producto.setStock(producto.getStock() + pedidoACancelar.cantidad);

        listaPedidos.remove(filaSeleccionada);
        modeloTabla.removeRow(filaSeleccionada);

        actualizarDetallesProducto();

        JOptionPane.showMessageDialog(this,
                "Pedido " + pedidoACancelar.idPedido + " cancelado. Stock reincorporado.",
                "Cancelación Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
}