package main.java.co.edu.uptc.gui;

import main.java.co.edu.uptc.controller.TiendaController;
import main.java.co.edu.uptc.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Hola a todos
public class VentanaPrincipal extends JFrame {
    private final TiendaController controller;
    private DefaultTableModel modeloTablaLibros;
    private DefaultTableModel modeloTablaCarrito;
    private JTable tablaLibros;
    private JTable tablaCarrito;
    private JLabel lblUsuario;
    private JLabel lblTotal;

    public VentanaPrincipal() {
        this.controller = new TiendaController();
        initUI();
    }

    private void initUI() {
        setTitle("Tienda Virtual de Libros - UPTC");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Usuario", crearPanelUsuario());
        tabbedPane.addTab("Catálogo & Inventario", crearPanelLibros());
        tabbedPane.addTab("Carrito & Compra", crearPanelCarrito());

        add(tabbedPane);
    }

    private JPanel crearPanelUsuario() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtTelefono = new JTextField();
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"ESTANDAR", "PREMIUM"});

        panel.add(new JLabel("ID / Cédula:"));
        panel.add(txtId);
        panel.add(new JLabel("Nombre Completo:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Correo Electrónico:"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Dirección Envío:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Tipo de Cliente:"));
        panel.add(cbTipo);

        JButton btnRegistrar = new JButton("Registrar Usuario");
        JButton btnLogin = new JButton("Iniciar Sesión (con Correo)");

        lblUsuario = new JLabel("Estado: Sin sesión iniciada");
        lblUsuario.setForeground(Color.RED);

        btnRegistrar.addActionListener(e -> {
            if (txtId.getText().isEmpty() || txtCorreo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete ID y Correo.");
                return;
            }
            Cliente c = new Cliente(txtId.getText(), txtNombre.getText(), txtCorreo.getText(),
                    txtDireccion.getText(), txtTelefono.getText(), (String) cbTipo.getSelectedItem());
            if (controller.registrarCliente(c)) {
                JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.");
            }
        });

        btnLogin.addActionListener(e -> {
            String correo = txtCorreo.getText().trim();
            if (controller.iniciarSesion(correo)) {
                Cliente u = controller.getUsuarioLogueado();
                lblUsuario.setText("Usuario: " + u.getNombreCompleto() + " (" + u.getTipoCliente() + ")");
                lblUsuario.setForeground(new Color(0, 128, 0));
                JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombreCompleto());
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(btnRegistrar);
        panel.add(btnLogin);
        panel.add(lblUsuario);

        return panel;
    }

    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout());

        modeloTablaLibros = new DefaultTableModel(new String[]{
                "ISBN", "Título", "Autor", "Año", "Categoría", "Editorial", "Páginas", "Precio", "Stock", "Formato", "IVA %"
        }, 0);
        tablaLibros = new JTable(modeloTablaLibros);
        actualizarTablaLibros();

        JPanel panelBotones = new JPanel();
        JButton btnNuevoLibro = new JButton("Agregar Libro");
        JButton btnEliminarLibro = new JButton("Eliminar Seleccionado");
        JButton btnAgregarCarrito = new JButton("Agregar al Carrito");

        btnNuevoLibro.addActionListener(e -> mostrarFormularioLibro());

        btnEliminarLibro.addActionListener(e -> {
            int row = tablaLibros.getSelectedRow();
            if (row != -1) {
                String isbn = (String) modeloTablaLibros.getValueAt(row, 0);
                if (controller.eliminarLibro(isbn)) {
                    actualizarTablaLibros();
                    JOptionPane.showMessageDialog(this, "Libro eliminado.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla.");
            }
        });

        btnAgregarCarrito.addActionListener(e -> {
            int row = tablaLibros.getSelectedRow();
            if (row != -1) {
                String isbn = (String) modeloTablaLibros.getValueAt(row, 0);
                String cantStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad:");
                if (cantStr != null && !cantStr.isEmpty()) {
                    try {
                        int cant = Integer.parseInt(cantStr);
                        controller.agregarAlCarrito(isbn, cant);
                        actualizarTablaCarrito();
                        JOptionPane.showMessageDialog(this, "Agregado al carrito.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un libro.");
            }
        });

        panelBotones.add(btnNuevoLibro);
        panelBotones.add(btnEliminarLibro);
        panelBotones.add(btnAgregarCarrito);

        panel.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout());

        modeloTablaCarrito = new DefaultTableModel(new String[]{
                "Título", "Cantidad", "Precio U.", "Subtotal", "Impuestos"
        }, 0);
        tablaCarrito = new JTable(modeloTablaCarrito);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: $0.0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));

        JComboBox<String> cbPago = new JComboBox<>(new String[]{"Tarjeta de Crédito", "Efectivo", "Transferencia"});
        JButton btnFinalizar = new JButton("Pagar y Finalizar Compra");

        btnFinalizar.addActionListener(e -> {
            try {
                String metodo = (String) cbPago.getSelectedItem();
                if (controller.finalizarCompra(metodo)) {
                    JOptionPane.showMessageDialog(this, "¡Compra realizada con éxito!\nRecibo guardado en 'registro_operaciones.txt'.");
                    actualizarTablaLibros();
                    actualizarTablaCarrito();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Compra", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelInferior.add(lblTotal);
        panelInferior.add(new JLabel("  Método de Pago:"));
        panelInferior.add(cbPago);
        panelInferior.add(btnFinalizar);

        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private void mostrarFormularioLibro() {
        JTextField txtIsbn = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtAnio = new JTextField("2024");
        JTextField txtCategoria = new JTextField();
        JTextField txtEditorial = new JTextField();
        JTextField txtPaginas = new JTextField("300");
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();
        JComboBox<String> cbFormato = new JComboBox<>(new String[]{"Físico", "Digital"});
        JTextField txtIva = new JTextField("19");

        Object[] message = {
                "ISBN:", txtIsbn,
                "Título:", txtTitulo,
                "Autor:", txtAutor,
                "Año Publicación:", txtAnio,
                "Categoría:", txtCategoria,
                "Editorial:", txtEditorial,
                "Nº Páginas:", txtPaginas,
                "Precio Venta ($):", txtPrecio,
                "Stock:", txtStock,
                "Formato:", cbFormato,
                "IVA (%):", txtIva
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nuevo Libro - Caso de Estudio", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Libro l = new Libro(
                        txtIsbn.getText(),
                        txtTitulo.getText(),
                        txtAutor.getText(),
                        Integer.parseInt(txtAnio.getText()),
                        txtCategoria.getText(),
                        txtEditorial.getText(),
                        Integer.parseInt(txtPaginas.getText()),
                        Double.parseDouble(txtPrecio.getText()),
                        Integer.parseInt(txtStock.getText()),
                        (String) cbFormato.getSelectedItem(),
                        Double.parseDouble(txtIva.getText())
                );
                controller.registrarLibro(l);
                actualizarTablaLibros();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados. Verifique los campos numéricos.");
            }
        }
    }

    private void actualizarTablaLibros() {
        modeloTablaLibros.setRowCount(0);
        List<Libro> lista = controller.listarLibros();
        for (Libro l : lista) {
            modeloTablaLibros.addRow(new Object[]{
                    l.getIsbn(), l.getTitulo(), l.getAutor(), l.getAnioPublicacion(),
                    l.getCategoria(), l.getEditorial(), l.getNumeroPaginas(),
                    l.getPrecioVenta(), l.getStock(), l.getFormato(), l.getPorcentajeIva()
            });
        }
    }

    private void actualizarTablaCarrito() {
        modeloTablaCarrito.setRowCount(0);
        Carrito c = controller.obtenerCarritoActual();
        for (ItemCarrito item : c.getItems()) {
            modeloTablaCarrito.addRow(new Object[]{
                    item.getLibro().getTitulo(), item.getCantidad(), item.getLibro().getPrecioVenta(),
                    item.getSubtotalConIva(), item.getImpuestos()
            });
        }
        lblTotal.setText("Total a pagar: $" + controller.calcularTotalCompra(c));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}