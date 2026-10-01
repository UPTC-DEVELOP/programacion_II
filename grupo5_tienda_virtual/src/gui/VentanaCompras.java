package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import negocio.CarritoCompras;
import negocio.Libro;
import negocio.MetodoPago;

public class VentanaCompras extends JFrame {

    
	private static final long serialVersionUID = 1L;

	private CarritoCompras carrito;
	
    // Campos de selección
    private JComboBox<Libro> cmbLibro;
    private JTextField txtCantidad;

    // Tabla del carrito
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;

    // Información de la compra
    private JLabel lblSubtotal;
    private JLabel lblIva19;
    private JLabel lblIva5;
    private JLabel lblTotal;

    // Método de pago
    private JComboBox<MetodoPago> cmbMetodoPago;

    // Botones
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnVaciar;
    private JButton btnComprar;

    public VentanaCompras() {
    	
        carrito = new CarritoCompras();
        

        setTitle("Gestion de Compras");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearTablaCarrito(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        
    }
    
    private void agregarAlCarrito() {

        Libro libro = (Libro) cmbLibro.getSelectedItem();

        if (libro == null) {
            return;
        }

        int cantidad;

        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
        } catch (NumberFormatException e) {
            return;
        }

        if (cantidad <= 0) {
            return;
        }

        carrito.agregarLibro(libro, cantidad);

        actualizarTabla();
    }
    
    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Map.Entry<Libro, Integer> entrada : carrito.getItems().entrySet()) {

            Libro libro = entrada.getKey();
            int cantidad = entrada.getValue();

            double subtotal = libro.getPrecio() * cantidad;

            modeloTabla.addRow(new Object[] {
                    libro,
                    cantidad,
                    libro.getPrecio(),
                    subtotal
            });
        }

        lblSubtotal.setText(String.valueOf(carrito.calcularSubtotal()));
        lblIva19.setText(String.valueOf(carrito.calcularIVA19()));
        lblIva5.setText(String.valueOf(carrito.calcularIVA5()));
        lblTotal.setText(String.valueOf(carrito.calcularTotal()));
    }

    private JPanel crearPanelSuperior() {

        JPanel panel = new JPanel(new GridLayout(2, 4, 6, 6));

        cmbLibro = new JComboBox<Libro>();
        txtCantidad = new JTextField();

        btnAgregar = new JButton("Agregar al carrito");
        btnModificar = new JButton("Modificar cantidad");

        panel.add(new JLabel("Libro:"));
        panel.add(cmbLibro);

        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);

        panel.add(btnAgregar);
        panel.add(btnModificar);

        return panel;
    }

    private JScrollPane crearTablaCarrito() {

        modeloTabla = new DefaultTableModel(
                new Object[] {
                        "Libro",
                        "Cantidad",
                        "Precio",
                        "Subtotal"
                }, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloTabla);

        return new JScrollPane(tablaCarrito);
    }

    private JPanel crearPanelInferior() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(6, 6));

        JPanel panelTotales = new JPanel(new GridLayout(4, 2, 6, 6));

        lblSubtotal = new JLabel("$0.00");
        lblIva19 = new JLabel("$0.00");
        lblIva5 = new JLabel("$0.00");
        lblTotal = new JLabel("$0.00");

        panelTotales.add(new JLabel("Subtotal:"));
        panelTotales.add(lblSubtotal);

        panelTotales.add(new JLabel("IVA 19%:"));
        panelTotales.add(lblIva19);

        panelTotales.add(new JLabel("IVA 5%:"));
        panelTotales.add(lblIva5);

        panelTotales.add(new JLabel("TOTAL:"));
        panelTotales.add(lblTotal);

        JPanel panelPago = new JPanel(new GridLayout(1, 2, 6, 6));

        cmbMetodoPago = new JComboBox<MetodoPago>(
                new MetodoPago[] {
                        new MetodoPago("Efectivo", "Pago en efectivo"),
                        new MetodoPago("Tarjeta", "Pago con tarjeta"),
                        new MetodoPago("Transferencia", "Pago mediante transferencia")
                });

        panelPago.add(new JLabel("Método de pago:"));
        panelPago.add(cmbMetodoPago);

        panelPago.add(new JLabel("Método de pago:"));
        panelPago.add(cmbMetodoPago);

        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 6, 6));

        btnEliminar = new JButton("Eliminar");
        btnVaciar = new JButton("Vaciar carrito");
        btnComprar = new JButton("Finalizar compra");

        panelBotones.add(btnEliminar);
        panelBotones.add(btnVaciar);
        panelBotones.add(btnComprar);

        JPanel panelFinal = new JPanel(new BorderLayout(6, 6));

        panelFinal.add(panelPago, BorderLayout.NORTH);
        panelFinal.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelTotales, BorderLayout.NORTH);
        panelPrincipal.add(panelFinal, BorderLayout.SOUTH);

        return panelPrincipal;
    }

    public JComboBox<Libro> getCmbLibro() {
        return cmbLibro;
    }

    public JTextField getTxtCantidad() {
        return txtCantidad;
    }

    public JTable getTablaCarrito() {
        return tablaCarrito;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JLabel getLblSubtotal() {
        return lblSubtotal;
    }

    public JLabel getLblIva19() {
        return lblIva19;
    }

    public JLabel getLblIva5() {
        return lblIva5;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public JComboBox<MetodoPago> getCmbMetodoPago() {
        return cmbMetodoPago;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnVaciar() {
        return btnVaciar;
    }

    public JButton getBtnComprar() {
        return btnComprar;
    }
}