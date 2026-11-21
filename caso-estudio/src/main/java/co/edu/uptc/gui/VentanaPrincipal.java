package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.ArrayList;
import co.edu.uptc.controller.TiendaController;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.Libro;
import co.edu.uptc.model.UbicacionesColombia;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private final TiendaController controller;
    private DefaultTableModel modeloTablaLibros;
    private DefaultTableModel modeloTablaCarrito;
    private JTable tablaLibros;
    private JTable tablaCarrito;

    // Pestaña Usuario
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtPass;
    private JCheckBox chkMostrarPass;
    private JTextField txtDireccion;
    private JComboBox<String> cbDepto;
    private JComboBox<String> cbCiudad;
    private JTextField txtTelefono;
    private JComboBox<String> cbTipoCliente;
    private JLabel lblEstadoUsuario;

    // Búsqueda en Libros
    private JTextField txtBuscarLibro;

    // Carrito & Pago
    private JLabel lblTotalCarrito;
    private JComboBox<String> cbMetodoPago, cbBanco;
    private JTextField txtTarjeta, txtExp, txtCvv, txtNequi;
    private JPanel panelDatosTarjeta, panelDatosNequi;

    public VentanaPrincipal() {
        this.controller = new TiendaController();
        initUI();
    }

    private void initUI() {
        setTitle("Tienda Virtual de Libros - UPTC");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Usuario", crearPanelUsuario());
        tabbedPane.addTab("Catálogo & Inventario", crearPanelLibros());
        tabbedPane.addTab("Carrito & Compra", crearPanelCarrito());

        add(tabbedPane);
        actualizarTablaLibros();
    }

    // ==========================================
    // 1. PANEL DE USUARIO (MANTIENE DISEÑO EXACTO)
    // ==========================================
    private JPanel crearPanelUsuario() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(30);
        txtNombre = new JTextField(30);
        txtCorreo = new JTextField(30);
        txtPass = new JPasswordField(22);
        chkMostrarPass = new JCheckBox("Mostrar");
        
        chkMostrarPass.addActionListener(e -> {
            txtPass.setEchoChar(chkMostrarPass.isSelected() ? (char) 0 : '•');
        });

        JPanel panelPass = new JPanel(new BorderLayout(5, 0));
        panelPass.add(txtPass, BorderLayout.CENTER);
        panelPass.add(chkMostrarPass, BorderLayout.EAST);

        txtDireccion = new JTextField(30);

        cbDepto = new JComboBox<>(UbicacionesColombia.getDepartamentos());
        cbCiudad = new JComboBox<>();
        actualizarCiudades();

        cbDepto.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) actualizarCiudades();
        });

        txtTelefono = new JTextField(30);
        cbTipoCliente = new JComboBox<>(new String[]{"ESTANDAR", "ADMINISTRADOR", "VIP"});

        int row = 0;
        agregarFilaFormulario(panelForm, "ID / Cédula:", txtId, gbc, row++);
        agregarFilaFormulario(panelForm, "Nombre Completo:", txtNombre, gbc, row++);
        agregarFilaFormulario(panelForm, "Correo Electrónico:", txtCorreo, gbc, row++);
        agregarFilaFormulario(panelForm, "Contraseña (mín. 6 caracteres):", panelPass, gbc, row++);
        agregarFilaFormulario(panelForm, "Dirección Envío:", txtDireccion, gbc, row++);
        agregarFilaFormulario(panelForm, "Departamento:", cbDepto, gbc, row++);
        agregarFilaFormulario(panelForm, "Ciudad:", cbCiudad, gbc, row++);
        agregarFilaFormulario(panelForm, "Teléfono:", txtTelefono, gbc, row++);
        agregarFilaFormulario(panelForm, "Tipo de Cliente:", cbTipoCliente, gbc, row++);

        panelPrincipal.add(panelForm, BorderLayout.CENTER);

        JPanel panelBotonesContainer = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotonesContainer.setPreferredSize(new Dimension(0, 90));

        JButton btnRegistrar = new JButton("Registrar Usuario");
        JButton btnIniciarSesion = new JButton("Iniciar Sesión");
        JButton btnActualizar = new JButton("Actualizar Datos del Usuario");
        JButton btnOlvidoPass = new JButton("Se me olvidó la contraseña");
        JButton btnEliminarCuenta = new JButton("Eliminar mi cuenta");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        lblEstadoUsuario = new JLabel("Sin iniciar sesión", SwingConstants.CENTER);
        lblEstadoUsuario.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEstadoUsuario.setForeground(new Color(0, 128, 0));

        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnIniciarSesion.addActionListener(e -> iniciarSesion());
        btnActualizar.addActionListener(e -> actualizarDatosUsuario());
        btnOlvidoPass.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "La recuperación de contraseña por correo no está disponible en esta versión.\nContacte al administrador de la tienda."));
        btnEliminarCuenta.addActionListener(e -> eliminarCuentaUsuario());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelBotonesContainer.add(btnRegistrar);
        panelBotonesContainer.add(btnIniciarSesion);
        panelBotonesContainer.add(btnActualizar);
        panelBotonesContainer.add(btnEliminarCuenta);
        panelBotonesContainer.add(btnOlvidoPass);
        panelBotonesContainer.add(btnCerrarSesion);
        panelBotonesContainer.add(lblEstadoUsuario);

        panelPrincipal.add(panelBotonesContainer, BorderLayout.SOUTH);
        return panelPrincipal;
    }

    private void agregarFilaFormulario(JPanel panel, String etiqueta, JComponent comp, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private void actualizarCiudades() {
        cbCiudad.removeAllItems();
        String depto = (String) cbDepto.getSelectedItem();
        if (depto != null) {
            for (String c : UbicacionesColombia.getCiudades(depto)) cbCiudad.addItem(c);
        }
    }

    private void registrarUsuario() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (pass.length() < TiendaController.LONGITUD_MINIMA_CONTRASENA) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener mínimo " + TiendaController.LONGITUD_MINIMA_CONTRASENA + " caracteres.");
            return;
        }

        Cliente c = new Cliente(
            id, nombre, correo, txtDireccion.getText().trim(),
            (String) cbDepto.getSelectedItem(), (String) cbCiudad.getSelectedItem(),
            txtTelefono.getText().trim(), (String) cbTipoCliente.getSelectedItem()
        );
        
        c.setContrasenaHash(pass);

        if (controller.registrarCliente(c)) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado exitosamente!");
        } else {
            JOptionPane.showMessageDialog(this, "El ID o el Correo ya están registrados.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese Correo y Contraseña.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.iniciarSesion(correo, pass)) {
            Cliente log = controller.getUsuarioLogueado();
            cargarFormularioUsuario(log);   // muestra en el formulario los datos guardados del usuario
            lblEstadoUsuario.setText("Usuario: " + log.getNombreCompleto() + " (" + log.getTipoCliente() + ")");
            JOptionPane.showMessageDialog(this, "Bienvenido " + log.getNombreCompleto());
            actualizarTablaCarrito();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        controller.cerrarSesion();
        limpiarFormularioUsuario();
        lblEstadoUsuario.setText("Sin iniciar sesión");
        actualizarTablaCarrito();
        JOptionPane.showMessageDialog(this, "Sesión cerrada.");
    }

    /** Muestra en el formulario los datos guardados del usuario; el ID y el correo no se pueden editar. */
    private void cargarFormularioUsuario(Cliente c) {
        txtId.setText(c.getId());
        txtNombre.setText(c.getNombreCompleto());
        txtCorreo.setText(c.getCorreo());
        txtDireccion.setText(c.getDireccionEnvio());
        txtTelefono.setText(c.getTelefono());
        cbDepto.setSelectedItem(c.getDepartamento());
        actualizarCiudades();
        cbCiudad.setSelectedItem(c.getCiudad());
        cbTipoCliente.setSelectedItem(c.getTipoCliente());
        txtPass.setText("");
        txtId.setEditable(false);
        txtCorreo.setEditable(false);
    }

    /** Deja el formulario de usuario vacío y vuelve a habilitar el ID y el correo. */
    private void limpiarFormularioUsuario() {
        txtId.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtPass.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtId.setEditable(true);
        txtCorreo.setEditable(true);
    }

    /** ACTUALIZAR: guarda los cambios del formulario en el usuario con sesión iniciada. */
    private void actualizarDatosUsuario() {
        Cliente logueado = controller.getUsuarioLogueado();
        if (logueado == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para actualizar sus datos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // El ID y el correo se toman del usuario en sesión (no se pueden cambiar)
        Cliente datos = new Cliente(
            logueado.getId(), txtNombre.getText().trim(), logueado.getCorreo(),
            txtDireccion.getText().trim(), (String) cbDepto.getSelectedItem(), (String) cbCiudad.getSelectedItem(),
            txtTelefono.getText().trim(), (String) cbTipoCliente.getSelectedItem()
        );

        // Si el campo de contraseña está vacío, se conserva la contraseña actual
        String nuevaPass = new String(txtPass.getPassword());
        String error = controller.actualizarUsuario(datos, nuevaPass);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente actualizado = controller.getUsuarioLogueado();
        lblEstadoUsuario.setText("Usuario: " + actualizado.getNombreCompleto() + " (" + actualizado.getTipoCliente() + ")");
        txtPass.setText("");
        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
    }

    /** ELIMINAR: borra la cuenta del usuario con sesión iniciada, pidiendo su contraseña como confirmación. */
    private void eliminarCuentaUsuario() {
        if (controller.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para eliminar su cuenta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPasswordField campoPass = new JPasswordField();
        int opcion = JOptionPane.showConfirmDialog(this,
            new Object[]{"Esta acción eliminará su cuenta de forma permanente.", "Ingrese su contraseña para confirmar:", campoPass},
            "Eliminar cuenta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        if (controller.eliminarUsuarioLogueado(new String(campoPass.getPassword()))) {
            limpiarFormularioUsuario();
            lblEstadoUsuario.setText("Sin iniciar sesión");
            actualizarTablaCarrito();
            actualizarTablaLibros();   // el stock de su carrito vuelve al catálogo
            JOptionPane.showMessageDialog(this, "Su cuenta fue eliminada correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta. No se eliminó la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================
    // 2. PANEL DE LIBROS (GESTIÓN COMPLETA Y MEJORAS)
    // ==========================================
    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de búsqueda superior
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar (Título, Autor, ISBN, Categoría):"));
        txtBuscarLibro = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Mostrar Todos");

        btnBuscar.addActionListener(e -> buscarLibros());
        btnLimpiar.addActionListener(e -> {
            txtBuscarLibro.setText("");
            actualizarTablaLibros();
        });

        panelBusqueda.add(txtBuscarLibro);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnLimpiar);
        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla con todas las columnas
        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Categoría", "Editorial", "Páginas", "Precio", "Stock", "Formato", "IVA %"};
        
        modeloTablaLibros = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilita la edición directa
            }
        };

        tablaLibros = new JTable(modeloTablaLibros);
        tablaLibros.setAutoCreateRowSorter(true); // Ordenamiento por columnas

        // Resaltar en rojo cuando el stock <= 3
        tablaLibros.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int modelRow = table.convertRowIndexToModel(row);
                int stock = (int) table.getModel().getValueAt(modelRow, 8);
                
                if (!isSelected) {
                    if (stock <= 3) {
                        c.setBackground(new Color(255, 230, 230));
                        c.setForeground(Color.RED);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });

        panel.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        // Botones C-R-U-D
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAgregar = new JButton("Agregar Libro");
        JButton btnEditar = new JButton("Editar Libro");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        JButton btnAgregarCarrito = new JButton("Agregar al Carrito");

        btnAgregar.addActionListener(e -> mostrarFormularioLibro(null));
        btnEditar.addActionListener(e -> editarLibroSeleccionado());
        btnEliminar.addActionListener(e -> eliminarLibroSeleccionado());
        btnAgregarCarrito.addActionListener(e -> agregarLibroSeleccionadoAlCarrito());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnAgregarCarrito);

        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTablaLibros() {
        modeloTablaLibros.setRowCount(0);
        List<Libro> lista = controller.listarLibros();
        if (lista != null) {
            for (Libro l : lista) {
                modeloTablaLibros.addRow(new Object[]{
                    l.getIsbn(), l.getTitulo(), l.getAutor(), l.getAnio(),
                    l.getCategoria(), l.getEditorial(), l.getPaginas(),
                    l.getPrecioVenta(), l.getStock(), l.getFormato(), l.getPorcentajeIva()
                });
            }
        }
    }

    private void buscarLibros() {
        String q = txtBuscarLibro.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            actualizarTablaLibros();
            return;
        }

        modeloTablaLibros.setRowCount(0);
        List<Libro> lista = controller.listarLibros();
        if (lista != null) {
            for (Libro l : lista) {
                if (l.getIsbn().toLowerCase().contains(q) ||
                    l.getTitulo().toLowerCase().contains(q) ||
                    l.getAutor().toLowerCase().contains(q) ||
                    l.getCategoria().toLowerCase().contains(q)) {
                    modeloTablaLibros.addRow(new Object[]{
                        l.getIsbn(), l.getTitulo(), l.getAutor(), l.getAnio(),
                        l.getCategoria(), l.getEditorial(), l.getPaginas(),
                        l.getPrecioVenta(), l.getStock(), l.getFormato(), l.getPorcentajeIva()
                    });
                }
            }
        }
    }

    private void mostrarFormularioLibro(Libro libroExistente) {
        boolean esEdicion = (libroExistente != null);

        JTextField txtIsbnForm = new JTextField(esEdicion ? libroExistente.getIsbn() : "");
        JTextField txtTituloForm = new JTextField(esEdicion ? libroExistente.getTitulo() : "");
        JTextField txtAutorForm = new JTextField(esEdicion ? libroExistente.getAutor() : "");
        JTextField txtAnioForm = new JTextField(esEdicion ? String.valueOf(libroExistente.getAnio()) : "");

        String[] categorias = {"Programación", "Arquitectura", "Literatura", "Ciencia", "Ingeniería", "Historia"};
        JComboBox<String> cbCategoriaForm = new JComboBox<>(categorias);
        if (esEdicion && libroExistente.getCategoria() != null) cbCategoriaForm.setSelectedItem(libroExistente.getCategoria());

        JTextField txtEditorialForm = new JTextField(esEdicion ? libroExistente.getEditorial() : "");
        JTextField txtPaginasForm = new JTextField(esEdicion ? String.valueOf(libroExistente.getPaginas()) : "");
        JTextField txtPrecioForm = new JTextField(esEdicion ? String.valueOf(libroExistente.getPrecioVenta()) : "");
        JTextField txtStockForm = new JTextField(esEdicion ? String.valueOf(libroExistente.getStock()) : "");

        JComboBox<String> cbFormatoForm = new JComboBox<>(new String[]{"Físico", "Digital"});
        if (esEdicion && libroExistente.getFormato() != null) cbFormatoForm.setSelectedItem(libroExistente.getFormato());

        JTextField txtIvaForm = new JTextField(esEdicion ? String.valueOf(libroExistente.getPorcentajeIva()) : "19.0");

        if (esEdicion) txtIsbnForm.setEditable(false);

        Object[] form = {
            "ISBN:", txtIsbnForm,
            "Título:", txtTituloForm,
            "Autor:", txtAutorForm,
            "Año Publicación:", txtAnioForm,
            "Categoría:", cbCategoriaForm,
            "Editorial:", txtEditorialForm,
            "Páginas:", txtPaginasForm,
            "Precio Venta (con IVA):", txtPrecioForm,
            "Stock:", txtStockForm,
            "Formato:", cbFormatoForm,
            "Porcentaje IVA (%):", txtIvaForm
        };

        int op = JOptionPane.showConfirmDialog(this, form, esEdicion ? "Editar Libro" : "Agregar Nuevo Libro", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            try {
                Libro l = esEdicion ? libroExistente : new Libro();
                l.setIsbn(txtIsbnForm.getText().trim());
                l.setTitulo(txtTituloForm.getText().trim());
                l.setAutor(txtAutorForm.getText().trim());
                l.setAnio(Integer.parseInt(txtAnioForm.getText().trim()));
                l.setCategoria((String) cbCategoriaForm.getSelectedItem());
                l.setEditorial(txtEditorialForm.getText().trim());
                l.setPaginas(Integer.parseInt(txtPaginasForm.getText().trim()));
                l.setPrecioVenta(Double.parseDouble(txtPrecioForm.getText().trim()));
                l.setStock(Integer.parseInt(txtStockForm.getText().trim()));
                l.setFormato((String) cbFormatoForm.getSelectedItem());
                
             // =========================================================================
             // Fragmento representativo: Registro/actualización de libro y validación UI
             // =========================================================================
                l.setPorcentajeIva(Double.parseDouble(txtIvaForm.getText().trim()));

             // 1. Llamada al controlador para actualizar o registrar el libro en persistencia
                String error = esEdicion ? controller.actualizarLibro(l) : controller.registrarLibro(l);

             // 2. Verificación del resultado de la operación y notificación en la interfaz gráfica
                if (error != null) {
                    JOptionPane.showMessageDialog(this, error, "Error de Validación", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "¡Libro guardado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTablaLibros();
                }
            } catch (NumberFormatException ex) {
            	// 3. Captura de excepciones si el usuario ingresa caracteres no numéricos en el formulario
                JOptionPane.showMessageDialog(this, "Asegúrese de ingresar valores numéricos válidos en Año, Páginas, Precio, Stock e IVA.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para editar.");
            return;
        }
        int filaModelo = tablaLibros.convertRowIndexToModel(fila);
        String isbn = (String) modeloTablaLibros.getValueAt(filaModelo, 0);
        Libro libro = controller.buscarLibroPorIsbn(isbn);
        if (libro != null) mostrarFormularioLibro(libro);
    }

    private void eliminarLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.");
            return;
        }
        int filaModelo = tablaLibros.convertRowIndexToModel(fila);
        String isbn = (String) modeloTablaLibros.getValueAt(filaModelo, 0);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Desea eliminar permanentemente el libro con ISBN " + isbn + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminarLibro(isbn)) {
                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.");
                actualizarTablaLibros();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarLibroSeleccionadoAlCarrito() {
        if (controller.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión primero en la pestaña 'Usuario'.");
            return;
        }
        int fila = tablaLibros.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro del catálogo.");
            return;
        }
        int filaModelo = tablaLibros.convertRowIndexToModel(fila);
        String isbn = (String) modeloTablaLibros.getValueAt(filaModelo, 0);
        Libro l = controller.buscarLibroPorIsbn(isbn);

        if (l != null) {
            String cantStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad a comprar:", "1");
            if (cantStr != null) {
                try {
                    int cant = Integer.parseInt(cantStr);
                    if (cant <= 0) {
                        JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                        return;
                    }
                    if (cant > l.getStock()) {
                        JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + l.getStock());
                        return;
                    }
                    controller.agregarAlCarrito(l, cant);
                    actualizarTablaCarrito();
                    JOptionPane.showMessageDialog(this, "Libro añadido al carrito con éxito.");
                } catch (NumberFormatException e) {
                	actualizarTablaLibros();
                    JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                }
            }
        }
    }

    // ==========================================
    // 3. PANEL DE CARRITO & PAGO
    // ==========================================
    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ISBN", "Título", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTablaCarrito = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = new JTable(modeloTablaCarrito);
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        
        JPanel panelAccionesCarrito = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnActualizarCantidad = new JButton("Actualizar Cantidad");
        JButton btnEliminarCarrito = new JButton("Eliminar Seleccionados");

        btnActualizarCantidad.addActionListener(e -> actualizarCantidadSeleccionada());
        btnEliminarCarrito.addActionListener(e -> eliminarSeleccionadosDelCarrito());

        panelAccionesCarrito.add(btnActualizarCantidad);
        panelAccionesCarrito.add(btnEliminarCarrito);
        panel.add(panelAccionesCarrito, BorderLayout.NORTH);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        lblTotalCarrito = new JLabel("Total Compra: $0.0", SwingConstants.RIGHT);
        lblTotalCarrito.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelInferior.add(lblTotalCarrito, BorderLayout.NORTH);

        JPanel panelPago = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelPago.setBorder(BorderFactory.createTitledBorder("Pasarela de Pago"));

        cbMetodoPago = new JComboBox<>(new String[]{TiendaController.METODO_TARJETA, TiendaController.METODO_TRANSFERENCIA});
        cbBanco = new JComboBox<>(new String[]{"Bancolombia", "Davivienda", "Banco de Bogotá", "BBVA"});

        txtTarjeta = new JTextField(12);
        txtExp = new JTextField(5);
        txtCvv = new JTextField(4);
        txtNequi = new JTextField(10);

        panelDatosTarjeta = new JPanel();
        panelDatosTarjeta.add(new JLabel("Banco:")); panelDatosTarjeta.add(cbBanco);
        panelDatosTarjeta.add(new JLabel("Tarjeta:")); panelDatosTarjeta.add(txtTarjeta);
        panelDatosTarjeta.add(new JLabel("MM/AA:")); panelDatosTarjeta.add(txtExp);
        panelDatosTarjeta.add(new JLabel("CVV:")); panelDatosTarjeta.add(txtCvv);

        panelDatosNequi = new JPanel();
        panelDatosNequi.add(new JLabel("Número Nequi:")); panelDatosNequi.add(txtNequi);
        panelDatosNequi.setVisible(false);

        cbMetodoPago.addActionListener(e -> {
            boolean esTarjeta = TiendaController.METODO_TARJETA.equals(cbMetodoPago.getSelectedItem());
            panelDatosTarjeta.setVisible(esTarjeta);
            panelDatosNequi.setVisible(!esTarjeta);
            panelPago.revalidate();
        });

        panelPago.add(new JLabel("Método:"));
        panelPago.add(cbMetodoPago);
        panelPago.add(panelDatosTarjeta);
        panelPago.add(panelDatosNequi);

        JButton btnFinalizar = new JButton("Finalizar Compra");
        btnFinalizar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnFinalizar.addActionListener(e -> realizarCompra());
        panelPago.add(btnFinalizar);

        panelInferior.add(panelPago, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarTablaCarrito() {
        modeloTablaCarrito.setRowCount(0);
        List<ItemCarrito> items = controller.obtenerCarritoActual().getItems();
        double total = 0;
        if (items != null) {
            for (ItemCarrito it : items) {
                if (it.getLibro() == null) continue;
                double subtotal = it.getLibro().getPrecioVenta() * it.getCantidad();
                total += subtotal;
                modeloTablaCarrito.addRow(new Object[]{
                    it.getLibro().getIsbn(),
                    it.getLibro().getTitulo(),
                    it.getCantidad(),
                    it.getLibro().getPrecioVenta(),
                    subtotal
                });
            }
        }
        lblTotalCarrito.setText("Total Compra: $" + String.format("%.2f", total));
    }
    
    private void actualizarCantidadSeleccionada() {
        if (controller.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión primero en la pestaña 'Usuario'.");
            return;
        }
        int[] filas = tablaCarrito.getSelectedRows();
        if (filas.length != 1) {
            JOptionPane.showMessageDialog(this, "Seleccione un solo libro del carrito para actualizar su cantidad.");
            return;
        }
        int filaModelo = tablaCarrito.convertRowIndexToModel(filas[0]);
        String isbn = (String) modeloTablaCarrito.getValueAt(filaModelo, 0);
        int cantidadActual = (int) modeloTablaCarrito.getValueAt(filaModelo, 2);

        String cantStr = JOptionPane.showInputDialog(this, "Ingrese la nueva cantidad:", String.valueOf(cantidadActual));
        if (cantStr != null) {
            try {
                int cant = Integer.parseInt(cantStr.trim());
                String error = controller.actualizarCantidadCarrito(isbn, cant);
                if (error != null) {
                    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    actualizarTablaCarrito();
                    actualizarTablaLibros();
                    JOptionPane.showMessageDialog(this, "Cantidad actualizada correctamente.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        }
    }

    private void eliminarSeleccionadosDelCarrito() {
        if (controller.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión primero en la pestaña 'Usuario'.");
            return;
        }
        int[] filas = tablaCarrito.getSelectedRows();
        if (filas.length == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un libro del carrito para eliminar.");
            return;
        }
        List<String> isbns = new ArrayList<>();
        for (int fila : filas) {
            int filaModelo = tablaCarrito.convertRowIndexToModel(fila);
            isbns.add((String) modeloTablaCarrito.getValueAt(filaModelo, 0));
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Desea eliminar " + isbns.size() + " libro(s) del carrito? Las unidades regresarán al stock.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminarDelCarrito(isbns)) {
                JOptionPane.showMessageDialog(this, "Libro(s) eliminado(s) del carrito correctamente.");
                actualizarTablaCarrito();
                actualizarTablaLibros();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar del carrito.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void realizarCompra() {
        if (controller.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para realizar la compra.");
            return;
        }

        String metodo = (String) cbMetodoPago.getSelectedItem();
        String errorPago = controller.registrarPago(
            metodo,
            (String) cbBanco.getSelectedItem(),
            txtTarjeta.getText().trim(),
            txtExp.getText().trim(),
            txtCvv.getText().trim(),
            txtNequi.getText().trim()
        );

        if (errorPago != null) {
            JOptionPane.showMessageDialog(this, errorPago, "Error en los datos de pago", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (controller.finalizarCompra()) {
            JOptionPane.showMessageDialog(this, "¡Compra realizada con éxito! Se ha procesado su orden.");
            actualizarTablaCarrito();
            actualizarTablaLibros();
        } else {
            JOptionPane.showMessageDialog(this, "El carrito está vacío o ocurrió un error al procesar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}