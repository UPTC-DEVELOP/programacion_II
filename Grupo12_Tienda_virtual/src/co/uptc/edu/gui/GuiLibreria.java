package co.uptc.edu.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.edu.uptc.negocio.ItemCarrito;
import co.edu.uptc.negocio.Libro;

/**
 * Aplicación de GUI para Librería Virtual utilizando Swing y AWT.
 * Diseñada para Eclipse IDE - Simulación completa de interfaces.
 */
public class GuiLibreria extends JFrame {
	    // Layout principal para alternar entre pantallas
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Estado del usuario e inventario
    private String currentUserRole = "GUEST"; // "ADMIN" o "CLIENTE"
    private String currentUsername = "";
    private List<Libro> inventarioLibros;
    private List<ItemCarrito> carritoCompras;

    // Componentes compartidos que requieren actualización dinámica
    private DefaultTableModel tablaInventarioModel;
    private DefaultTableModel tablaCarritoModel;
    private JPanel panelCatalogoLibros;
    private JLabel lblTotalCarrito;
    private JLabel lblUsuarioActual;

    public GuiLibreria() {
        // Configuración de la ventana principal
        setTitle("Sistemas de Gestión - Librería Virtual 'BiblioTech'");
        setSize(1000, 680);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar colecciones de datos simulados
        inicializarDatosSimulados();

        // Configurar CardLayout en la ventana principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Crear e integrar las vistas al CardLayout
        mainPanel.add(crearPantallaPresentacion(), "PRESENTACION");
        mainPanel.add(crearPantallaLogin(), "LOGIN");
        mainPanel.add(crearPantallaUsuario(), "USUARIO_HOME");
        mainPanel.add(crearPantallaCatalogo(), "CATALOGO");
        mainPanel.add(crearPantallaCarrito(), "CARRITO");
        mainPanel.add(crearPantallaAdmin(), "ADMIN_HOME");
        mainPanel.add(crearPantallaInventario(), "INVENTARIO");

        add(mainPanel);

        // Iniciar en la pantalla de presentación
        cardLayout.show(mainPanel, "PRESENTACION");
    }

    private void inicializarDatosSimulados() {
        inventarioLibros = new ArrayList<>();
        carritoCompras = new ArrayList<>();

        inventarioLibros.add(new Libro(101, "Cien Años de Soledad", "Gabriel García Márquez", 45000, 15, "Novela"));
        inventarioLibros.add(new Libro(102, "El Código Da Vinci", "Dan Brown", 38000, 8, "Misterio"));
        inventarioLibros.add(new Libro(103, "Clean Code in Java", "Robert C. Martin", 120000, 5, "Tecnología"));
        inventarioLibros.add(new Libro(104, "Hábitos Atómicos", "James Clear", 52000, 20, "Superación"));
        inventarioLibros.add(new Libro(105, "El Principito", "Antoine de Saint-Exupéry", 25000, 12, "Fábula"));
        inventarioLibros.add(new Libro(106, "Don Quijote de la Mancha", "Miguel de Cervantes", 60000, 7, "Clásico"));
    }

    private JPanel crearPantallaPresentacion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        // Cabecera superior
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(207, 105, 85));
        panelHeader.setPreferredSize(new Dimension(0, 80));
        JLabel lblTitulo = new JLabel("BIBLIOTECA VIRTUAL");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelHeader.add(lblTitulo);

        // Centro con Banner y bienvenida
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JLabel lblSub = new JLabel("Tu portal digital hacia el conocimiento y la lectura");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblSub.setForeground(new Color(71, 85, 105));

        JLabel lblIcono = new JLabel("📖");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));

        JButton btnIngresar = crearBotonEstilizado("Ingresar al Sistema", new Color(79, 70, 229));
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnIngresar.setPreferredSize(new Dimension(220, 50));
        btnIngresar.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        gbc.gridy = 0; panelCentro.add(lblIcono, gbc);
        gbc.gridy = 1; panelCentro.add(lblSub, gbc);
        gbc.gridy = 2; panelCentro.add(btnIngresar, gbc);

        panel.add(panelHeader, BorderLayout.NORTH);
        panel.add(panelCentro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPantallaLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(241, 245, 249));

        JPanel card = new JPanel(new GridLayout(6, 1, 10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(207, 105, 85), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel lblTitle = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(207, 105, 85));

        JTextField txtUser = new JTextField();
        txtUser.setBorder(BorderFactory.createTitledBorder("Usuario"));

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(BorderFactory.createTitledBorder("Contraseña"));

        JComboBox<String> comboRol = new JComboBox<>(new String[]{"Cliente", "Administrador"});
        comboRol.setBorder(BorderFactory.createTitledBorder("Rol de Acceso"));

        JButton btnLogin = crearBotonEstilizado("Entrar", new Color(207, 105, 85));
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFocusPainted(false);

        // Panel de botones horizontal
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnVolver);
        panelBotones.add(btnLogin);

        card.add(lblTitle);
        card.add(txtUser);
        card.add(txtPass);
        card.add(comboRol);
        card.add(new JLabel("*(Tip: Puedes presionar entrar sin datos para simular)", SwingConstants.CENTER));
        card.add(panelBotones);

        // Listeners
        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "PRESENTACION"));
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            currentUsername = user.isEmpty() ? "Usuario Demo" : user;
            String rol = (String) comboRol.getSelectedItem();

            if ("Administrador".equals(rol)) {
                currentUserRole = "ADMIN";
                cardLayout.show(mainPanel, "ADMIN_HOME");
            } else {
                currentUserRole = "CLIENTE";
                lblUsuarioActual.setText("Bienvenido, " + currentUsername);
                cardLayout.show(mainPanel, "USUARIO_HOME");
            }
        });

        panel.add(card);
        return panel;
    }

    private JPanel crearPantallaUsuario() {
        JPanel panel = new JPanel(new BorderLayout());

        // Header
        JPanel header = crearHeaderSuperior("Panel Principal de Cliente");
        lblUsuarioActual = new JLabel("Bienvenido, Cliente");
        lblUsuarioActual.setForeground(Color.WHITE);
        lblUsuarioActual.setFont(new Font("SansSerif", Font.ITALIC, 14));
        header.add(lblUsuarioActual, BorderLayout.EAST);
        header.setBackground(new Color(207, 105, 85));

        // Contenido Central con Menú de opciones
        JPanel menuGrid = new JPanel(new GridLayout(1, 2, 20, 20));
        menuGrid.setBorder(new EmptyBorder(50, 50, 50, 50));
        menuGrid.setBackground(new Color(248, 250, 252));

        JButton btnCatalogo = crearCardBoton("Explora el Catálogo", "Examina y compra libros disponibles", new Color(207, 105, 85));
        JButton btnCarrito = crearCardBoton("Carrito de Compras", "Revisa tus productos y finaliza la compra", new Color(207, 105, 85));

        btnCatalogo.addActionListener(e -> {
            refrescarCatalogoGUI();
            cardLayout.show(mainPanel, "CATALOGO");
        });

        btnCarrito.addActionListener(e -> {
            refrescarTablaCarrito();
            cardLayout.show(mainPanel, "CARRITO");
        });

        menuGrid.add(btnCatalogo);
        menuGrid.add(btnCarrito);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        footer.add(btnLogout);

        panel.add(header, BorderLayout.NORTH);
        panel.add(menuGrid, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPantallaCatalogo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(crearHeaderSuperior("Catálogo de Libros Disponible"), BorderLayout.NORTH);

        panelCatalogoLibros = new JPanel(new GridLayout(0, 3, 15, 15));
        panelCatalogoLibros.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelCatalogoLibros.setBackground(new Color(241, 245, 249));

        JScrollPane scrollPane = new JScrollPane(panelCatalogoLibros);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Barra inferior de navegación
        JPanel navBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver al Inicio");
        JButton btnIrCarrito = crearBotonEstilizado("Ver Carrito", new Color(207, 105, 85));

        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "USUARIO_HOME"));
        btnIrCarrito.addActionListener(e -> {
            refrescarTablaCarrito();
            cardLayout.show(mainPanel, "CARRITO");
        });

        navBottom.add(btnVolver);
        navBottom.add(btnIrCarrito);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(navBottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refrescarCatalogoGUI() {
        panelCatalogoLibros.removeAll();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        for (Libro libro : inventarioLibros) {
            JPanel cardLibro = new JPanel(new BorderLayout(5, 5));
            cardLibro.setBackground(Color.WHITE);
            cardLibro.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(207, 105, 85), 1),
                    new EmptyBorder(10, 10, 10, 10)
            ));

            JLabel lblTitulo = new JLabel("<html><b>" + libro.getTitulo() + "</b></html>");
            lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel lblInfo = new JLabel("<html>Autor: " + libro.getAutor() + "<br>Categoría: " + libro.getCategoria() +
                    "<br>Stock: " + libro.getStock() + " un.</html>");
            lblInfo.setForeground(new Color(100, 116, 139));

            JLabel lblPrecio = new JLabel(formatter.format(libro.getPrecio()));
            lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 15));
            lblPrecio.setForeground(new Color(16, 185, 129));

            JButton btnAgregar = new JButton("Agregar 🛒");
            btnAgregar.setEnabled(libro.getStock() > 0);
            btnAgregar.addActionListener(e -> agregarAlCarrito(libro));

            JPanel bottomCard = new JPanel(new BorderLayout());
            bottomCard.setOpaque(false);
            bottomCard.add(lblPrecio, BorderLayout.WEST);
            bottomCard.add(btnAgregar, BorderLayout.EAST);

            cardLibro.add(lblTitulo, BorderLayout.NORTH);
            cardLibro.add(lblInfo, BorderLayout.CENTER);
            cardLibro.add(bottomCard, BorderLayout.SOUTH);

            panelCatalogoLibros.add(cardLibro);
        }
        panelCatalogoLibros.revalidate();
        panelCatalogoLibros.repaint();
    }

    private JPanel crearPantallaCarrito() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(crearHeaderSuperior("Carrito de Compras (Simulación)"), BorderLayout.NORTH);

        String[] columnas = {"ID", "Título", "Precio Unitario", "Cantidad", "Subtotal"};
        tablaCarritoModel = new DefaultTableModel(columnas, 0) {
        	
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabla = new JTable(tablaCarritoModel);
        tabla.setRowHeight(25);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel de Checkout e importe
        JPanel panelCheckout = new JPanel(new BorderLayout());
        panelCheckout.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelCheckout.setBackground(Color.WHITE);

        lblTotalCarrito = new JLabel("Total: $0", SwingConstants.LEFT);
        lblTotalCarrito.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVaciar = new JButton("Vaciar Carrito");
        JButton btnVolver = new JButton("Seguir Comprando");
        JButton btnComprar = crearBotonEstilizado("Finalizar Compra", new Color(207, 105, 85));

        btnVaciar.addActionListener(e -> {
            carritoCompras.clear();
            refrescarTablaCarrito();
        });

        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "CATALOGO"));

        btnComprar.addActionListener(e -> ejecutarSimulacionCompra());

        panelAcciones.add(btnVaciar);
        panelAcciones.add(btnVolver);
        panelAcciones.add(btnComprar);

        panelCheckout.add(lblTotalCarrito, BorderLayout.WEST);
        panelCheckout.add(panelAcciones, BorderLayout.EAST);

        panel.add(panelCheckout, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPantallaAdmin() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(crearHeaderSuperior("Panel de Administración General"), BorderLayout.NORTH);

        JPanel menuGrid = new JPanel(new GridLayout(1, 2, 20, 20));
        menuGrid.setBorder(new EmptyBorder(50, 50, 50, 50));
        menuGrid.setBackground(new Color(248, 250, 252));

        JButton btnGestionarInv = crearCardBoton("Gestor de Inventario", "Alta, consulta y control de stock de libros", new Color(207, 105, 85));
        JButton btnReportes = crearCardBoton("Métricas de Ventas", "Visualiza simulaciones y reportes del sistema", new Color(207, 105, 85));

        btnGestionarInv.addActionListener(e -> {
            refrescarTablaInventario();
            cardLayout.show(mainPanel, "INVENTARIO");
        });

        btnReportes.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Simulación de Reportes: No hay estadísticas registradas aún.", "Información", JOptionPane.INFORMATION_MESSAGE)
        );

        menuGrid.add(btnGestionarInv);
        menuGrid.add(btnReportes);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLogout = new JButton("Cerrar Sesión Admin");
        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        footer.add(btnLogout);

        panel.add(menuGrid, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPantallaInventario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(crearHeaderSuperior("Administración de Inventario de Libros"), BorderLayout.NORTH);

        // Tabla de inventario
        String[] columnas = {"ID", "Título", "Autor", "Precio", "Stock", "Categoría"};
        tablaInventarioModel = new DefaultTableModel(columnas, 0);
        JTable tablaInv = new JTable(tablaInventarioModel);
        tablaInv.setRowHeight(24);
        panel.add(new JScrollPane(tablaInv), BorderLayout.CENTER);

        // Formulario de alta de libro
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Agregar Nuevo Libro"));
        formPanel.setBackground(Color.WHITE);

        JTextField txtId = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();
        JTextField txtCat = new JTextField();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(new JLabel("Título:"));
        formPanel.add(new JLabel("Autor:"));
        formPanel.add(new JLabel("Precio:"));
        formPanel.add(new JLabel("Stock:"));
        formPanel.add(new JLabel("Categoría:"));

        formPanel.add(txtId);
        formPanel.add(txtTitulo);
        formPanel.add(txtAutor);
        formPanel.add(txtPrecio);
        formPanel.add(txtStock);
        formPanel.add(txtCat);

        // Botones de acción
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregar = crearBotonEstilizado("Guardar Libro", new Color(207, 105, 85));
        JButton btnVolver = new JButton("Volver al Panel Admin");

        btnAgregar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String tit = txtTitulo.getText();
                String aut = txtAutor.getText();
                double prc = Double.parseDouble(txtPrecio.getText());
                int stk = Integer.parseInt(txtStock.getText());
                String cat = txtCat.getText();

                inventarioLibros.add(new Libro(id, tit, aut, prc, stk, cat));
                refrescarTablaInventario();

                // Limpiar campos
                txtId.setText(""); txtTitulo.setText(""); txtAutor.setText("");
                txtPrecio.setText(""); txtStock.setText(""); txtCat.setText("");

                JOptionPane.showMessageDialog(this, "Libro agregado con éxito al inventario.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Por favor verifique los datos ingresados (Campos numéricos válidos).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "ADMIN_HOME"));

        actionPanel.add(btnAgregar);
        actionPanel.add(btnVolver);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(formPanel, BorderLayout.CENTER);
        panelInferior.add(actionPanel, BorderLayout.SOUTH);

        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    private void agregarAlCarrito(Libro libro) {
        for (ItemCarrito item : carritoCompras) {
            if (item.getLibro().getId() == libro.getId()) {
                if (item.getCantidad() < libro.getStock()) {
                    item.setCantidad(item.getCantidad() + 1);
                    JOptionPane.showMessageDialog(this, "Se incrementó la cantidad de '" + libro.getTitulo() + "' en el carrito.");
                } else {
                    JOptionPane.showMessageDialog(this, "No hay suficiente stock disponible.", "Límite superado", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }
        }
        carritoCompras.add(new ItemCarrito(libro, 1));
        JOptionPane.showMessageDialog(this, "Libro '" + libro.getTitulo() + "' añadido al carrito.");
    }

    private void refrescarTablaCarrito() {
        tablaCarritoModel.setRowCount(0);
        double total = 0;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        for (ItemCarrito item : carritoCompras) {
            total += item.getSubtotal();
            tablaCarritoModel.addRow(new Object[]{
                    item.getLibro().getId(),
                    item.getLibro().getTitulo(),
                    formatter.format(item.getLibro().getPrecio()),
                    item.getCantidad(),
                    formatter.format(item.getSubtotal())
            });
        }
        lblTotalCarrito.setText("Total a pagar: " + formatter.format(total));
    }

    private void refrescarTablaInventario() {
        tablaInventarioModel.setRowCount(0);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        for (Libro libro : inventarioLibros) {
            tablaInventarioModel.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    formatter.format(libro.getPrecio()),
                    libro.getStock(),
                    libro.getCategoria()
            });
        }
    }

    private void ejecutarSimulacionCompra() {
        if (carritoCompras.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito se encuentra vacío.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Descontar stock simulado
        for (ItemCarrito item : carritoCompras) {
            Libro l = item.getLibro();
            l.setStock(l.getStock() - item.getCantidad());
        }

        JOptionPane.showMessageDialog(this,
                "🎉 ¡Gracias por tu compra simulada!\nSe ha generado el recibo digital a nombre de: " + currentUsername,
                "Compra Finalizada", JOptionPane.INFORMATION_MESSAGE);

        carritoCompras.clear();
        refrescarTablaCarrito();
        cardLayout.show(mainPanel, "USUARIO_HOME");
    }

    private JPanel crearHeaderSuperior(String titulo) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(207, 105, 85));
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);

        header.add(lbl, BorderLayout.WEST);
        return header;
    }

    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.ORANGE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private JButton crearCardBoton(String titulo, String subtitulo, Color accentColor) {
        JButton btn = new JButton("<html><center><font size='5'><b>" + titulo + "</b></font><br><br><font color='#64748B'>" + subtitulo + "</font></center></html>");
        btn.setBackground(Color.WHITE);
        btn.setForeground(accentColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return btn;
    }

    public static void main(String[] args) {
        // Configurar Look and Feel del sistema para una apariencia nativa
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Ejecutar en el hilo de eventos de AWT/Swing
        SwingUtilities.invokeLater(() -> {
            GuiLibreria app = new GuiLibreria();
            app.setVisible(true);
        });
    }
}