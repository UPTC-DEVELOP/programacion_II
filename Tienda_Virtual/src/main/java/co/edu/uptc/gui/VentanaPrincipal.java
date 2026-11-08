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
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(9, 2, 10, 10));

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();
        JPanel panelContrasena = new JPanel(new BorderLayout(5, 0));
        panelContrasena.add(txtContrasena, BorderLayout.CENTER);
        panelContrasena.add(crearCasillaMostrar(txtContrasena), BorderLayout.EAST);
        JTextField txtDireccion = new JTextField();
        JComboBox<String> cbDepartamento = new JComboBox<>(UbicacionesColombia.departamentos());
        JComboBox<String> cbCiudad = new JComboBox<>();
        enlazarCiudades(cbDepartamento, cbCiudad);
        JTextField txtTelefono = new JTextField();
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"ESTANDAR", "PREMIUM"});

        form.add(new JLabel("ID / Cédula:"));
        form.add(txtId);
        form.add(new JLabel("Nombre Completo:"));
        form.add(txtNombre);
        form.add(new JLabel("Correo Electrónico:"));
        form.add(txtCorreo);
        form.add(new JLabel("Contraseña (mín. " + TiendaController.LONGITUD_MINIMA_CONTRASENA + " caracteres):"));
        form.add(panelContrasena);
        form.add(new JLabel("Dirección Envío:"));
        form.add(txtDireccion);
        form.add(new JLabel("Departamento:"));
        form.add(cbDepartamento);
        form.add(new JLabel("Ciudad:"));
        form.add(cbCiudad);
        form.add(new JLabel("Teléfono:"));
        form.add(txtTelefono);
        form.add(new JLabel("Tipo de Cliente:"));
        form.add(cbTipo);

        JButton btnRegistrar = new JButton("Registrar Usuario");
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnActualizar = new JButton("Actualizar Datos del Usuario");
        JButton btnOlvide = new JButton("Se me olvidó la contraseña");
        JButton btnCerrar = new JButton("Cerrar Sesión");

        lblUsuario = new JLabel("Estado: Sin sesión iniciada");
        lblUsuario.setForeground(Color.RED);

        // --- Registrar ---
        btnRegistrar.addActionListener(e -> {
            String ciudad = String.valueOf(cbCiudad.getSelectedItem()).trim();
            String contrasena = new String(txtContrasena.getPassword());
            if (txtId.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()
                    || txtCorreo.getText().trim().isEmpty() || ciudad.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete ID, Nombre, Correo, Departamento y Ciudad.");
                return;
            }
            if (!txtCorreo.getText().contains("@")) {
                JOptionPane.showMessageDialog(this, "Ingrese un correo válido.");
                return;
            }
            if (contrasena.length() < TiendaController.LONGITUD_MINIMA_CONTRASENA) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos "
                        + TiendaController.LONGITUD_MINIMA_CONTRASENA + " caracteres.");
                return;
            }
            Cliente c = new Cliente(txtId.getText().trim(), txtNombre.getText().trim(), txtCorreo.getText().trim(),
                    txtDireccion.getText().trim(), (String) cbDepartamento.getSelectedItem(), ciudad,
                    txtTelefono.getText().trim(), (String) cbTipo.getSelectedItem());
            if (controller.registrarCliente(c, contrasena)) {
                txtContrasena.setText("");
                JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar: el ID o el correo ya existen.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Iniciar sesión (correo + contraseña) ---
        btnLogin.addActionListener(e -> {
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());
            if (controller.iniciarSesion(correo, contrasena)) {
                Cliente u = controller.getUsuarioLogueado();
                actualizarEtiquetaSesion();
                txtContrasena.setText("");
                actualizarTablaCarrito();
                JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombreCompleto());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Correo o contraseña incorrectos.\nSi aún no tiene contraseña, use \"Se me olvidó la contraseña\".",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Se me olvidó la contraseña ---
        btnOlvide.addActionListener(e -> mostrarRecuperarContrasena(txtCorreo.getText().trim()));

        // --- Actualizar datos del usuario ---
        btnActualizar.addActionListener(e -> mostrarFormularioActualizarUsuario());

        // --- Cerrar sesión ---
        btnCerrar.addActionListener(e -> {
            if (controller.getUsuarioLogueado() == null) {
                JOptionPane.showMessageDialog(this, "No hay una sesión iniciada.");
                return;
            }
            controller.cerrarSesion();
            actualizarEtiquetaSesion();
            txtId.setText("");
            txtNombre.setText("");
            txtCorreo.setText("");
            txtContrasena.setText("");
            txtDireccion.setText("");
            txtTelefono.setText("");
            cbDepartamento.setSelectedIndex(0);
            cbTipo.setSelectedIndex(0);
            actualizarTablaCarrito();
            JOptionPane.showMessageDialog(this, "Sesión cerrada correctamente.");
        });

        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 10, 10));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLogin);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnOlvide);
        panelBotones.add(btnCerrar);
        panelBotones.add(lblUsuario);

        panel.add(form, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    /** Casilla "Mostrar" que alterna entre ver y ocultar el texto de uno o varios campos de contraseña. */
    private static JCheckBox crearCasillaMostrar(JPasswordField... campos) {
        JCheckBox chk = new JCheckBox("Mostrar");
        char oculto = campos[0].getEchoChar();
        chk.addActionListener(e -> {
            for (JPasswordField campo : campos) {
                campo.setEchoChar(chk.isSelected() ? (char) 0 : oculto);
            }
        });
        return chk;
    }

    /** Carga las ciudades del departamento elegido y las vuelve a cargar cada vez que éste cambie. */
    private static void enlazarCiudades(JComboBox<String> cbDepartamento, JComboBox<String> cbCiudad) {
        cbCiudad.setEditable(true); // permite escribir una ciudad que no esté en la lista
        Runnable cargar = () -> {
            cbCiudad.removeAllItems();
            for (String c : UbicacionesColombia.ciudadesDe((String) cbDepartamento.getSelectedItem())) {
                cbCiudad.addItem(c);
            }
        };
        cargar.run();
        cbDepartamento.addActionListener(e -> cargar.run());
    }

    private void actualizarEtiquetaSesion() {
        Cliente u = controller.getUsuarioLogueado();
        if (u == null) {
            lblUsuario.setText("Estado: Sin sesión iniciada");
            lblUsuario.setForeground(Color.RED);
        } else {
            lblUsuario.setText("Usuario: " + u.getNombreCompleto() + " (" + u.getTipoCliente() + ")");
            lblUsuario.setForeground(new Color(0, 128, 0));
        }
    }

    private void mostrarFormularioActualizarUsuario() {
        Cliente u = controller.getUsuarioLogueado();
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para actualizar sus datos.");
            return;
        }

        JTextField txtNombre = new JTextField(u.getNombreCompleto());
        JTextField txtCorreo = new JTextField(u.getCorreo());
        JTextField txtDireccion = new JTextField(u.getDireccionEnvio());
        JComboBox<String> cbDepartamento = new JComboBox<>(UbicacionesColombia.departamentos());
        JComboBox<String> cbCiudad = new JComboBox<>();
        enlazarCiudades(cbDepartamento, cbCiudad);
        if (!u.getDepartamento().isEmpty()) {
            cbDepartamento.setSelectedItem(u.getDepartamento());
        }
        cbCiudad.setSelectedItem(u.getCiudad());
        JTextField txtTelefono = new JTextField(u.getTelefono());
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"ESTANDAR", "PREMIUM"});
        cbTipo.setSelectedItem(u.getTipoCliente());

        Object[] message = {
                "ID / Cédula (no editable): " + u.getId(),
                "Nombre Completo:", txtNombre,
                "Correo Electrónico:", txtCorreo,
                "Dirección Envío:", txtDireccion,
                "Departamento:", cbDepartamento,
                "Ciudad:", cbCiudad,
                "Teléfono:", txtTelefono,
                "Tipo de Cliente:", cbTipo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Actualizar datos del usuario",
                JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String ciudad = String.valueOf(cbCiudad.getSelectedItem()).trim();
        if (txtNombre.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty() || ciudad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, Correo y Ciudad son obligatorios.");
            return;
        }
        if (!txtCorreo.getText().contains("@")) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo válido.");
            return;
        }

        Cliente nuevo = new Cliente(u.getId(), txtNombre.getText().trim(), txtCorreo.getText().trim(),
                txtDireccion.getText().trim(), (String) cbDepartamento.getSelectedItem(), ciudad,
                txtTelefono.getText().trim(), (String) cbTipo.getSelectedItem());
        try {
            if (controller.actualizarCliente(nuevo)) {
                actualizarEtiquetaSesion();
                actualizarTablaCarrito(); // el descuento puede cambiar si cambió el tipo de cliente
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudieron actualizar los datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarRecuperarContrasena(String correoInicial) {
        JTextField txtCorreo = new JTextField(correoInicial);
        JTextField txtId = new JTextField();
        JPasswordField txtNueva = new JPasswordField();
        JPasswordField txtConfirmar = new JPasswordField();

        Object[] message = {
                "Para verificar su identidad ingrese el correo y el ID / Cédula con los que se registró.",
                "Correo Electrónico:", txtCorreo,
                "ID / Cédula:", txtId,
                "Nueva contraseña (mín. " + TiendaController.LONGITUD_MINIMA_CONTRASENA + " caracteres):", txtNueva,
                "Confirmar nueva contraseña:", txtConfirmar,
                crearCasillaMostrar(txtNueva, txtConfirmar)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Recuperar contraseña",
                JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String nueva = new String(txtNueva.getPassword());
        if (!nueva.equals(new String(txtConfirmar.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }
        if (nueva.length() < TiendaController.LONGITUD_MINIMA_CONTRASENA) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos "
                    + TiendaController.LONGITUD_MINIMA_CONTRASENA + " caracteres.");
            return;
        }
        if (controller.restablecerContrasena(txtCorreo.getText().trim(), txtId.getText().trim(), nueva)) {
            JOptionPane.showMessageDialog(this, "Contraseña actualizada. Ya puede iniciar sesión.");
        } else {
            JOptionPane.showMessageDialog(this, "El correo y el ID no coinciden con ningún usuario registrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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
                if (TiendaController.requiereDatosBancarios(metodo) && controller.getUsuarioLogueado() != null
                        && !controller.obtenerCarritoActual().getItems().isEmpty()
                        && !solicitarDatosTransferencia(metodo)) {
                    return;
                }
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
    
    private boolean solicitarDatosTransferencia(String metodo) {
        JComboBox<String> cbBanco = new JComboBox<>(DatosTransferencia.bancos());
        if (!TiendaController.esTransferencia(metodo)) cbBanco.removeItem(DatosTransferencia.NEQUI); // Nequi solo en transferencia
        JTextField txtTarjeta = new JTextField();
        JPasswordField txtCodigo = new JPasswordField();
        JTextField txtExpiracion = new JTextField();
        JTextField txtTelefonoNequi = new JTextField(controller.getUsuarioLogueado().getTelefono());

        JPanel panelCodigo = new JPanel(new BorderLayout(5, 0));
        panelCodigo.add(txtCodigo, BorderLayout.CENTER);
        panelCodigo.add(crearCasillaMostrar(txtCodigo), BorderLayout.EAST);

        JPanel panelTarjeta = new JPanel(new GridLayout(0, 1, 5, 5));
        panelTarjeta.add(new JLabel("Número de tarjeta:"));
        panelTarjeta.add(txtTarjeta);
        panelTarjeta.add(new JLabel("Código de seguridad (CVV):"));
        panelTarjeta.add(panelCodigo);
        panelTarjeta.add(new JLabel("Fecha de expiración (MM/AA):"));
        panelTarjeta.add(txtExpiracion);

        JPanel panelNequi = new JPanel(new GridLayout(0, 1, 5, 5));
        panelNequi.add(new JLabel("Número de teléfono Nequi:"));
        panelNequi.add(txtTelefonoNequi);
        JPanel contenedorNequi = new JPanel(new BorderLayout());
        contenedorNequi.add(panelNequi, BorderLayout.NORTH);

        // El teléfono solo se muestra si el banco es Nequi; los datos de la tarjeta se muestran siempre
        CardLayout cartas = new CardLayout();
        JPanel panelTelefono = new JPanel(cartas);
        panelTelefono.add(new JPanel(), "VACIO");
        panelTelefono.add(contenedorNequi, "NEQUI");
        cbBanco.addActionListener(e -> cartas.show(panelTelefono,
                DatosTransferencia.NEQUI.equals(cbBanco.getSelectedItem()) ? "NEQUI" : "VACIO"));
        JPanel panelDatos = new JPanel(new BorderLayout(5, 5));
        panelDatos.add(panelTelefono, BorderLayout.NORTH);
        panelDatos.add(panelTarjeta, BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout(5, 10));
        JPanel panelBanco = new JPanel(new GridLayout(0, 1, 5, 5));
        panelBanco.add(new JLabel("Banco:"));
        panelBanco.add(cbBanco);
        panel.add(panelBanco, BorderLayout.NORTH);
        panel.add(panelDatos, BorderLayout.CENTER);

        while (true) {
            int option = JOptionPane.showConfirmDialog(this, panel, "Datos de pago - " + metodo,
                    JOptionPane.OK_CANCEL_OPTION);
            if (option != JOptionPane.OK_OPTION) return false;

            boolean nequi = DatosTransferencia.NEQUI.equals(cbBanco.getSelectedItem());
            DatosTransferencia datos = new DatosTransferencia((String) cbBanco.getSelectedItem(),
                    txtTarjeta.getText().trim(),
                    new String(txtCodigo.getPassword()).trim(),
                    txtExpiracion.getText().trim(),
                    nequi ? txtTelefonoNequi.getText().trim() : "");
            try {
                controller.validarDatosTransferencia(datos);
                controller.setDatosTransferencia(datos);
                return true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos de transferencia",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
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