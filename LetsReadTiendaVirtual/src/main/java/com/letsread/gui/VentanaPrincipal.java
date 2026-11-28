package com.letsread.gui;

import com.letsread.model.Cliente;
import com.letsread.model.ClientePremium;
import com.letsread.model.ElementoCarrito;
import com.letsread.model.Libro;
import com.letsread.service.ClienteService;
import com.letsread.service.TiendaService;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
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
import com.letsread.service.LibroService;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private TiendaService service;
    private LibroService libroService;
    private JTabbedPane pestanias;
    // Campos para Iniciar Sesión
    private JTextField txtCorreoLogin;
    private JPasswordField txtClaveLogin;
    private JLabel lblEstadoSesion;
    // Campos para Registro
    private JTextField txtNombre, txtApellido, txtCorreo, txtDireccion, txtTelefono;
    private JPasswordField txtClave;
    private JComboBox<String> comboTipoCliente;

    private JTable tablaLibros, tablaCarrito;
    private DefaultTableModel modeloLibros, modeloCarrito;
    private JSpinner spinnerCantidad;
    private JLabel lblSubtotal, lblImpuestos, lblDescuento, lblTotal;
    private JTextArea txtRecibo;
    private final ClienteService clienteService;

    
    
    public VentanaPrincipal(ClienteService clienteService, LibroService libroService) {
        this.clienteService = clienteService;
        this.libroService = libroService;
        service = new TiendaService(libroService);

        setTitle("Let's Read - Tienda Virtual de Libros    YEISSON CRUZ & CRISTTOPHER MORENO  ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pestanias = new JTabbedPane();
        pestanias.addTab("Cliente / Registro", crearPanelCliente());
        pestanias.addTab("Catálogo de Libros", crearPanelCatalogo());
        pestanias.addTab("Carrito y Pago", crearPanelCarrito());

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(Box.createHorizontalGlue());

        JButton btnAdmin = new JButton("Ingreso Administrador");
        btnAdmin.setFocusable(false);
        btnAdmin.addActionListener(e -> abrirPanelAdministrador());

        menuBar.add(btnAdmin);
        setJMenuBar(menuBar);

        add(pestanias);
    }

    private JPanel crearPanelCliente() {
        JPanel panelContenedor = new JPanel(new BorderLayout(10, 10));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane subPestaniasCliente = new JTabbedPane();

        // --- SUB-PESTAÑA 1: INICIAR SESIÓN ---
        JPanel panelLogin = new JPanel(new BorderLayout(10, 10));
        panelLogin.setBorder(BorderFactory.createTitledBorder("Acceso de Cliente Registrado"));

        JPanel panelCamposLogin = new JPanel(new GridLayout(3, 2, 12, 15));
        panelCamposLogin.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        txtCorreoLogin = new JTextField();
        txtClaveLogin = new JPasswordField();

        JButton btnIniciarSesion = new JButton("Iniciar Sesión");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setEnabled(false);

        panelCamposLogin.add(new JLabel("Correo Electrónico:"));
        panelCamposLogin.add(txtCorreoLogin);
        panelCamposLogin.add(new JLabel("Contraseña:"));
        panelCamposLogin.add(txtClaveLogin);

        JPanel panelBotonesLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotonesLogin.add(btnIniciarSesion);
        panelBotonesLogin.add(btnCerrarSesion);

        panelCamposLogin.add(new JLabel(""));
        panelCamposLogin.add(panelBotonesLogin);

        lblEstadoSesion = new JLabel("Ningún usuario logueado actualmente.", JLabel.CENTER);
        lblEstadoSesion.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        panelLogin.add(panelCamposLogin, BorderLayout.CENTER);
        panelLogin.add(lblEstadoSesion, BorderLayout.SOUTH);

        // --- SUB-PESTAÑA 2: REGISTRARSE ---
        JPanel panelRegistro = new JPanel(new BorderLayout(10, 10));
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Crear Nueva Cuenta - Let's Read"));

        JPanel panelCamposRegistro = new JPanel(new GridLayout(7, 2, 10, 8));
        panelCamposRegistro.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtCorreo = new JTextField();
        txtClave = new JPasswordField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        comboTipoCliente = new JComboBox<>(new String[]{"Regular", "Premium"});

        panelCamposRegistro.add(new JLabel("Nombres:")); panelCamposRegistro.add(txtNombre);
        panelCamposRegistro.add(new JLabel("Apellidos:")); panelCamposRegistro.add(txtApellido);
        panelCamposRegistro.add(new JLabel("Correo Electrónico:")); panelCamposRegistro.add(txtCorreo);
        panelCamposRegistro.add(new JLabel("Contraseña:")); panelCamposRegistro.add(txtClave);
        panelCamposRegistro.add(new JLabel("Dirección:")); panelCamposRegistro.add(txtDireccion);
        panelCamposRegistro.add(new JLabel("Teléfono:")); panelCamposRegistro.add(txtTelefono);
        panelCamposRegistro.add(new JLabel("Tipo de Cliente:")); panelCamposRegistro.add(comboTipoCliente);

        JPanel panelBotonRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRegistrar = new JButton("Crear Cuenta");
        panelBotonRegistro.add(btnRegistrar);

        panelRegistro.add(panelCamposRegistro, BorderLayout.CENTER);
        panelRegistro.add(panelBotonRegistro, BorderLayout.SOUTH);

        // --- LÓGICA DE EVENTOS ---
        btnIniciarSesion.addActionListener(e -> {
            String correo = txtCorreoLogin.getText().trim().toLowerCase();
            String clave = new String(txtClaveLogin.getPassword());

            if (correo.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese correo y contraseña.");
                return;
            }

            Cliente cliente = clienteService.autenticar(correo, clave).orElse(null);
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos, o el usuario no está registrado.");
                return;
            }

            String tipo = (cliente instanceof ClientePremium) ? "Premium" : "Regular";
            service.setClienteActual(cliente);
            lblEstadoSesion.setText("Sesión activa: " + cliente.getNombreCompleto() + " (" + tipo + ")");
            btnCerrarSesion.setEnabled(true);
            txtClaveLogin.setText("");

            JOptionPane.showMessageDialog(this, "¡Bienvenido/a de nuevo, " + cliente.getNombreCompleto() + " (" + tipo + ")!");
            actualizarTablaCarrito();
        });

        btnCerrarSesion.addActionListener(e -> {
            service.setClienteActual(null);
            lblEstadoSesion.setText("Ningún usuario logueado actualmente.");
            btnCerrarSesion.setEnabled(false);
            txtCorreoLogin.setText("");
            txtClaveLogin.setText("");
            actualizarTablaCarrito();
            JOptionPane.showMessageDialog(this, "Sesión cerrada correctamente.");
        });

        btnRegistrar.addActionListener(e -> {
            String nombres = txtNombre.getText().trim();
            String apellidos = txtApellido.getText().trim();
            String correo = txtCorreo.getText().trim().toLowerCase();
            String clave = new String(txtClave.getPassword());

            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || clave.isEmpty()
                    || txtDireccion.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos para registrarse.");
                return;
            }

            String nombreCompleto = nombres + " " + apellidos;
            String tipo = (String) comboTipoCliente.getSelectedItem();
            Cliente cliente;
            try {
                cliente = clienteService.registrar(tipo, nombreCompleto, correo,
                        txtDireccion.getText(), txtTelefono.getText(), clave);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                return;
            }

            service.setClienteActual(cliente);
            lblEstadoSesion.setText("Sesión activa: " + cliente.getNombreCompleto() + " (" + tipo + ")");
            btnCerrarSesion.setEnabled(true);

            txtNombre.setText("");
            txtApellido.setText("");
            txtCorreo.setText("");
            txtClave.setText("");
            txtDireccion.setText("");
            txtTelefono.setText("");
            subPestaniasCliente.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this, "¡Registro exitoso! Bienvenido/a a Let's Read, " + cliente.getNombreCompleto() + " (" + tipo + ")");
            actualizarTablaCarrito();
        });

        subPestaniasCliente.addTab("Iniciar Sesión", panelLogin);
        subPestaniasCliente.addTab("Registrarse", panelRegistro);

        panelContenedor.add(subPestaniasCliente, BorderLayout.CENTER);
        return panelContenedor;
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

    private void abrirPanelAdministrador() {
        DialogoLoginAdmin login = new DialogoLoginAdmin(this);
        login.setVisible(true);

        if (login.isAutenticado()) {
            VentanaAdmin adminWindow = new VentanaAdmin(service, this::actualizarTablaLibros);
            adminWindow.setVisible(true);
        }
    }
}