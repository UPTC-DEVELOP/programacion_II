package co.edu.uptc.tienda.gui.parcial;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.negocio.parcial.Producto;

public class PanelExpresion extends JPanel {

    private JComboBox<Producto> comboProductos;

    private JLabel lblPrecio;
    private JLabel lblStock;
    private JLabel lblDescuento;
    private JLabel lblIVA;
    private JLabel lblTotal;

    private JTextField txtCantidad;

    private JButton btnRegistrarPedido;
    private JButton btnCancelarPedido;

    private JTable tablaPedidos;

    private DefaultTableModel modeloTabla;

    private DecimalFormat formatoDinero;

    public PanelExpresion() {

        formatoDinero =
                new DecimalFormat("#,##0.00");

        setLayout(
                new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10));

        crearComponentes();

        organizarComponentes();
    }

    private void crearComponentes() {

        /*
         * Campo para seleccionar producto.
         *
         * NO es editable.
         *
         * Al hacer clic se abrirá
         * un JOptionPane.
         */
        comboProductos =
                new JComboBox<>();

        comboProductos.setEditable(false);

        lblPrecio =
                new JLabel("$0.00");

        lblStock =
                new JLabel("0");

        lblDescuento =
                new JLabel("0%");

        lblIVA =
                new JLabel("0%");

        lblTotal =
                new JLabel("$0.00");

        txtCantidad =
                new JTextField();

        btnRegistrarPedido =
                new JButton(
                        "Registrar Pedido");

        btnCancelarPedido =
                new JButton(
                        "Cancelar Pedido Seleccionado");

        /*
         * Modelo de la tabla.
         */
        modeloTabla =
                new DefaultTableModel(
                        new Object[] {
                                "Producto",
                                "Cantidad",
                                "Precio Unitario",
                                "Total"
                        },
                        0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        tablaPedidos =
                new JTable(modeloTabla);

        tablaPedidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
    }

    private void organizarComponentes() {

        JPanel panelInformacion =
                new JPanel(
                        new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;

        gbc.insets.set(
                5,
                5,
                5,
                5);

        agregarFila(
                panelInformacion,
                gbc,
                0,
                "Producto:",
                comboProductos);

        agregarFila(
                panelInformacion,
                gbc,
                1,
                "Precio Base:",
                lblPrecio);

        agregarFila(
                panelInformacion,
                gbc,
                2,
                "Stock Disponible:",
                lblStock);

        agregarFila(
                panelInformacion,
                gbc,
                3,
                "Descuento:",
                lblDescuento);

        agregarFila(
                panelInformacion,
                gbc,
                4,
                "IVA:",
                lblIVA);

        agregarFila(
                panelInformacion,
                gbc,
                5,
                "Cantidad:",
                txtCantidad);

        agregarFila(
                panelInformacion,
                gbc,
                6,
                "Total:",
                lblTotal);

        /*
         * Botones.
         */
        JPanel panelBotones =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                10));

        panelBotones.add(
                btnRegistrarPedido);

        panelBotones.add(
                btnCancelarPedido);

        /*
         * Parte superior.
         */
        JPanel panelSuperior =
                new JPanel(
                        new BorderLayout());

        panelSuperior.add(
                panelInformacion,
                BorderLayout.CENTER);

        panelSuperior.add(
                panelBotones,
                BorderLayout.SOUTH);

        add(
                panelSuperior,
                BorderLayout.NORTH);

        /*
         * Tabla.
         */
        JScrollPane scrollTabla =
                new JScrollPane(
                        tablaPedidos);

        add(
                scrollTabla,
                BorderLayout.CENTER);
    }

    private void agregarFila(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            Component componente) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.0;

        panel.add(
                new JLabel(texto),
                gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        panel.add(
                componente,
                gbc);
    }

    /**
     * Muestra el producto en el JComboBox.
     */
    public void mostrarProductoSeleccionado(
            Producto producto) {

        comboProductos.removeAllItems();

        if (producto != null) {

            comboProductos.addItem(producto);

            comboProductos.setSelectedItem(
                    producto);
        }
    }

    public JComboBox<Producto> getComboProductos() {
        return comboProductos;
    }

    public JLabel getLblPrecio() {
        return lblPrecio;
    }

    public JLabel getLblStock() {
        return lblStock;
    }

    public JLabel getLblDescuento() {
        return lblDescuento;
    }

    public JLabel getLblIVA() {
        return lblIVA;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public JTextField getTxtCantidad() {
        return txtCantidad;
    }

    public JButton getBtnRegistrarPedido() {
        return btnRegistrarPedido;
    }

    public JButton getBtnCancelarPedido() {
        return btnCancelarPedido;
    }

    public JTable getTablaPedidos() {
        return tablaPedidos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public DecimalFormat getFormatoDinero() {
        return formatoDinero;
    }
}