package com.letsread.gui;

import com.letsread.model.Cliente;
import com.letsread.model.ClientePremium;
import com.letsread.model.ClienteRegular;
import com.letsread.model.ElementoCarrito;
import com.letsread.model.Libro;
import com.letsread.service.TiendaService;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private TiendaService service;

    private JTabbedPane pestanias;
    private JTextField txtNombre, txtCorreo, txtDireccion, txtTelefono;
    private JPasswordField txtClave;
    private JComboBox<String> comboTipoCliente;
    private JTable tablaLibros, tablaCarrito;
    private DefaultTableModel modeloLibros, modeloCarrito;
    private JSpinner spinnerCantidad;
    private JLabel lblSubtotal, lblImpuestos, lblDescuento, lblTotal;
    private JTextArea txtRecibo;

    public VentanaPrincipal() {
        service = new TiendaService();

        setTitle("Let's Read - Tienda Virtual de Libros    YEISSON CRUZ & CRISTTOPHER MORENO  ");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pestanias = new JTabbedPane();
        pestanias.addTab("Cliente / Registro", crearPanelCliente());
        pestanias.addTab("Catálogo de Libros", crearPanelCatalogo());
        pestanias.addTab("Carrito y Pago", crearPanelCarrito());

        add(pestanias);
    }

    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 15, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Registro de Cliente - Let's Read"));

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtClave = new JPasswordField();
        comboTipoCliente = new JComboBox<>(new String[]{"Regular", "Premium"});

        panel.add(new JLabel("Nombre Completo:")); panel.add(txtNombre);
        panel.add(new JLabel("Correo Electrónico:")); panel.add(txtCorreo);
        panel.add(new JLabel("Contraseña:")); panel.add(txtClave);
        panel.add(new JLabel("Dirección:")); panel.add(txtDireccion);
        panel.add(new JLabel("Teléfono:")); panel.add(txtTelefono);
        panel.add(new JLabel("Tipo de Cliente:")); panel.add(comboTipoCliente);

        JButton btnLogin = new JButton("Iniciar Sesión / Registrar");
        btnLogin.addActionListener(e -> {
            if (txtNombre.getText().isEmpty() || txtCorreo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.");
                return;
            }

            String tipo = (String) comboTipoCliente.getSelectedItem();
            Cliente cliente = tipo.equals("Premium") ?
                    new ClientePremium(txtNombre.getText(), txtCorreo.getText(), txtDireccion.getText(), txtTelefono.getText(), new String(txtClave.getPassword())) :
                    new ClienteRegular(txtNombre.getText(), txtCorreo.getText(), txtDireccion.getText(), txtTelefono.getText(), new String(txtClave.getPassword()));

            service.setClienteActual(cliente);
            JOptionPane.showMessageDialog(this, "¡Bienvenido/a a Let's Read, " + cliente.getNombreCompleto() + " (" + tipo + ")!");
            actualizarTablaCarrito();
        });

        panel.add(btnLogin);
        return panel;
    }

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"ISBN", "Título", "Autor", "Precio", "Stock"};
        modeloLibros = new DefaultTableModel(cols, 0);
        tablaLibros = new JTable(modeloLibros);

        actualizarTablaLibros();

        panel.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        JPanel panelBajo = new JPanel();
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JButton btnAgregar = new JButton("Agregar al Carrito");

        btnAgregar.addActionListener(e -> {
            int fila = tablaLibros.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un libro del catálogo.");
                return;
            }

            Libro l = service.getCatalogo().get(fila);
            int cant = (int) spinnerCantidad.getValue();

            try {
                service.agregarAlCarrito(l, cant);
                actualizarTablaCarrito();
                JOptionPane.showMessageDialog(this, "Libro agregado a tu carrito.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelBajo.add(new JLabel("Cantidad:"));
        panelBajo.add(spinnerCantidad);
        panelBajo.add(btnAgregar);

        panel.add(panelBajo, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"Título", "Cantidad", "Subtotal"};
        modeloCarrito = new DefaultTableModel(cols, 0);
        tablaCarrito = new JTable(modeloCarrito);

        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelDer = new JPanel(new GridLayout(6, 1, 5, 5));
        lblSubtotal = new JLabel("Subtotal: $0.0");
        lblImpuestos = new JLabel("Impuestos: $0.0");
        lblDescuento = new JLabel("Descuento: $0.0");
        lblTotal = new JLabel("Total: $0.0");
        JButton btnFinalizar = new JButton("Confirmar Compra");

        panelDer.add(lblSubtotal);
        panelDer.add(lblImpuestos);
        panelDer.add(lblDescuento);
        panelDer.add(lblTotal);
        panelDer.add(btnFinalizar);

        txtRecibo = new JTextArea(8, 20);
        txtRecibo.setEditable(false);

        panel.add(panelDer, BorderLayout.EAST);
        panel.add(new JScrollPane(txtRecibo), BorderLayout.SOUTH);

        btnFinalizar.addActionListener(e -> {
            if (service.getClienteActual() == null) {
                JOptionPane.showMessageDialog(this, "Debe iniciar sesión para completar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (service.getCarrito().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El carrito de Let's Read está vacío.");
                return;
            }

            StringBuilder recibo = new StringBuilder("=== RECIBO DE COMPRA - LET'S READ ===\n");
            recibo.append("Cliente: ").append(service.getClienteActual().getNombreCompleto()).append("\n");
            for (ElementoCarrito item : service.getCarrito()) {
                recibo.append("- ").append(item.getLibro().getTitulo()).append(" x").append(item.getCantidad()).append(" = $").append(item.getSubtotal()).append("\n");
            }
            recibo.append("-------------------------------------\n");
            recibo.append(lblTotal.getText());

            txtRecibo.setText(recibo.toString());
            service.procesarCompra();
            actualizarTablaLibros();
            actualizarTablaCarrito();

            JOptionPane.showMessageDialog(this, "¡Gracias por comprar en Let's Read!");
        });

        return panel;
    }

    private void actualizarTablaLibros() {
        modeloLibros.setRowCount(0);
        for (Libro l : service.getCatalogo()) {
            modeloLibros.addRow(new Object[]{l.getIsbn(), l.getTitulo(), l.getAutor(), l.getPrecioVenta(), l.getCantidadInventario()});
        }
    }

    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        double subtotal = 0, impuestos = 0;

        for (ElementoCarrito item : service.getCarrito()) {
            modeloCarrito.addRow(new Object[]{item.getLibro().getTitulo(), item.getCantidad(), item.getSubtotal()});
            subtotal += item.getSubtotal();
            impuestos += item.getImpuesto();
        }

        double descuento = 0;
        if (service.getClienteActual() != null) {
            descuento = service.getClienteActual().calcularDescuento(subtotal);
        }

        double total = subtotal - descuento;

        lblSubtotal.setText(String.format("Subtotal: $%.2f", subtotal));
        lblImpuestos.setText(String.format("Impuestos: $%.2f", impuestos));
        lblDescuento.setText(String.format("Descuento: $%.2f", descuento));
        lblTotal.setText(String.format("Total: $%.2f", total));
    }
}