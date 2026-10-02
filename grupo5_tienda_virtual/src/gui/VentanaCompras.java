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
import javax.swing.JOptionPane;

import java.util.Map;

import negocio.CarritoCompras;
import negocio.Libro;
import negocio.MetodoPago;
import negocio.Cliente;
import negocio.ControladorLibro;
import negocio.Compra;
import negocio.ItemCompra;


public class VentanaCompras extends JFrame {

    
	private static final long serialVersionUID = 1L;

	private CarritoCompras carrito;
	
	// CLIENTE ACTUAL Y CONTROLADOR DE LIBROS
	private Cliente cliente;
	private ControladorLibro controladorLibro;
	
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

 // RECIBIR EL CLIENTE Y EL CONTROLADOR DE LIBROS
    public VentanaCompras(Cliente cliente, ControladorLibro controladorLibro) {

        this.cliente = cliente;
        this.controladorLibro = controladorLibro;
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
        btnModificar.addActionListener(e -> modificarCantidad());
        btnEliminar.addActionListener(e -> eliminarDelCarrito());
        btnVaciar.addActionListener(e -> vaciarCarrito());
        btnComprar.addActionListener(e -> finalizarCompra());
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
    
    private void modificarCantidad() {

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

        carrito.modificarCantidad(libro, cantidad);

        actualizarTabla();
    } 
    
    private void eliminarDelCarrito() {

        Libro libro = (Libro) cmbLibro.getSelectedItem();

        if (libro == null) {
            return;
        }

        carrito.eliminarLibro(libro);

        actualizarTabla();
    }
    
    private void vaciarCarrito() {

        carrito.vaciar();

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
    
 // FINALIZAR LA COMPRA
    private void finalizarCompra() {

        // VALIDAR QUE EXISTA UN CLIENTE
        if (cliente == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay una sesion iniciada."
            );
            return;
        }

        // VALIDAR QUE EL CARRITO NO ESTE VACIO
        if (carrito.estaVacio()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El carrito esta vacio."
            );
            return;
        }

        // OBTENER EL METODO DE PAGO SELECCIONADO
        MetodoPago metodoPago =
                (MetodoPago) cmbMetodoPago.getSelectedItem();

        if (metodoPago == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un metodo de pago."
            );
            return;
        }

        // VALIDAR LA DISPONIBILIDAD DEL STOCK
        for (Map.Entry<Libro, Integer> entrada :
                carrito.getItems().entrySet()) {

            Libro libro = entrada.getKey();
            int cantidad = entrada.getValue();

            if (!controladorLibro.validarDisponibilidad(
                    libro.getIsbn(),
                    cantidad)) {

                JOptionPane.showMessageDialog(
                        this,
                        "No hay suficiente stock para: "
                                + libro.getTitulo()
                );
                return;
            }
        }

        // CREAR LA COMPRA
        Compra compra = new Compra(metodoPago.getNombre());

        // CREAR LOS ITEMS DE LA COMPRA
        for (Map.Entry<Libro, Integer> entrada :
                carrito.getItems().entrySet()) {

            Libro libro = entrada.getKey();
            int cantidad = entrada.getValue();

            ItemCompra item = new ItemCompra(libro, cantidad);

            compra.agregarItem(item);
        }

        // ACTUALIZAR EL INVENTARIO
        for (Map.Entry<Libro, Integer> entrada :
                carrito.getItems().entrySet()) {

            Libro libro = entrada.getKey();
            int cantidad = entrada.getValue();

            controladorLibro.actualizarInventario(
                    libro.getIsbn(),
                    cantidad
            );
        }

        // GUARDAR LA COMPRA EN EL CLIENTE
        cliente.agregarCompra(compra);

        // MOSTRAR RESUMEN DE LA COMPRA
        JOptionPane.showMessageDialog(
                this,
                "COMPRA REALIZADA CORRECTAMENTE\n\n"
                        + "Cliente: " + cliente.getNombreCompleto()
                        + "\nMetodo de pago: " + metodoPago.getNombre()
                        + "\nFecha: " + compra.getFecha()
                        + "\nTotal: $" + compra.calcularTotal()
        );

        // VACIAR EL CARRITO
        carrito.vaciar();

        // ACTUALIZAR LA TABLA Y LOS TOTALES
        actualizarTabla();
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