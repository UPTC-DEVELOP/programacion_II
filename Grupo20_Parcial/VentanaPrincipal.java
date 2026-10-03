import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;

public class VentanaPrincipal extends JFrame {

    private final Producto[] productos = {
        new Producto("Arroz", 5000, 20, 0.10, 0.19),
        new Producto("Leche", 4200, 15, 0.05, 0.19),
        new Producto("Pan", 3000, 30, 0.00, 0.05),
        new Producto("Cafe", 18000, 10, 0.15, 0.19)
    };

    private final JComboBox<Producto> cmbProductos = new JComboBox<>(productos);
    private final JTextField txtPrecioBase = campoSoloLectura();
    private final JTextField txtStock = campoSoloLectura();
    private final JTextField txtDescuento = campoSoloLectura();
    private final JTextField txtIVA = campoSoloLectura();
    private final JTextField txtPrecioFinal = campoSoloLectura();
    private final JTextField txtCantidad = new JTextField();
    private final JLabel lblTotal = new JLabel("Total pagado: $0.00");
    private final JButton btnRegistrar = new JButton("Registrar Pedido");
    private final JButton btnCancelar = new JButton("Cancelar Pedido Seleccionado");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Producto", "Cantidad", "Precio unitario", "Total"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaHistorial = new JTable(modeloTabla);

    public VentanaPrincipal() {
        super("Inventario y Ventas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelDatos = new JPanel(new GridLayout(0, 2, 8, 6));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Producto"));
        panelDatos.add(new JLabel("Producto:"));         panelDatos.add(cmbProductos);
        panelDatos.add(new JLabel("Precio base:"));      panelDatos.add(txtPrecioBase);
        panelDatos.add(new JLabel("Stock disponible:")); panelDatos.add(txtStock);
        panelDatos.add(new JLabel("Descuento:"));        panelDatos.add(txtDescuento);
        panelDatos.add(new JLabel("IVA:"));              panelDatos.add(txtIVA);
        panelDatos.add(new JLabel("Precio final:"));     panelDatos.add(txtPrecioFinal);
        panelDatos.add(new JLabel("Cantidad:"));         panelDatos.add(txtCantidad);
        panelDatos.add(btnRegistrar);                    panelDatos.add(lblTotal);

        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel panelHistorial = new JPanel(new BorderLayout(5, 5));
        panelHistorial.setBorder(BorderFactory.createTitledBorder("Historial de pedidos"));
        panelHistorial.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        panelHistorial.add(btnCancelar, BorderLayout.SOUTH);

        add(panelDatos, BorderLayout.NORTH);
        add(panelHistorial, BorderLayout.CENTER);

        cmbProductos.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actualizarCampos();
            }
        });

        btnRegistrar.addActionListener(e -> registrarPedido());

        btnCancelar.addActionListener(e -> cancelarPedido());

        actualizarCampos();
    }

    private void actualizarCampos() {
        Producto p = (Producto) cmbProductos.getSelectedItem();
        if (p == null) return;
        txtPrecioBase.setText(dinero(p.getPrecioBase()));
        txtStock.setText(String.valueOf(p.getStock()));
        txtDescuento.setText(porcentaje(p.getPorcentajeDescuento()));
        txtIVA.setText(porcentaje(p.getImpuestoIVA()));
        txtPrecioFinal.setText(dinero(p.calcularPrecioFinal()));
    }

    private void registrarPedido() {
        Producto p = (Producto) cmbProductos.getSelectedItem();
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            p.descontarStock(cantidad);

            double precioUnitario = p.calcularPrecioFinal();
            double total = precioUnitario * cantidad;

            modeloTabla.addRow(new Object[]{p, cantidad, dinero(precioUnitario), dinero(total)});

            lblTotal.setText("Total pagado: " + dinero(total));
            txtCantidad.setText("");
            actualizarCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser un número entero.",
                    "Entrada no válida", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "No se pudo registrar", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelarPedido() {
        int fila = tablaHistorial.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un pedido de la tabla.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Producto p = (Producto) modeloTabla.getValueAt(fila, 0);
        int cantidad = (Integer) modeloTabla.getValueAt(fila, 1);

        p.reponerStock(cantidad);
        modeloTabla.removeRow(fila);
        cmbProductos.setSelectedItem(p);
        actualizarCampos();

        JOptionPane.showMessageDialog(this,
                "Pedido cancelado. Se devolvieron " + cantidad + " unidades de " + p.getNombre() + ".");
    }

    private static JTextField campoSoloLectura() {
        JTextField campo = new JTextField();
        campo.setEditable(false);
        return campo;
    }

    private static String dinero(double valor) { return String.format("$%,.2f", valor); }

    private static String porcentaje(double valor) { return String.format("%.0f%%", valor * 100); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}