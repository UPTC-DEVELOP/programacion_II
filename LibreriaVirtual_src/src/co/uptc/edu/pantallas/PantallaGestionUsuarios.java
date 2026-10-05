package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Rol;
import co.uptc.edu.model.Tienda;
import co.uptc.edu.model.Usuario;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Administración de usuarios y roles (RF-04): listar, crear, editar y eliminar. */
public class PantallaGestionUsuarios extends JPanel implements Refrescable {

    private static final String CLIENTE = "Cliente";
    private static final String ADMINISTRADOR = "Administrador";

    private final Tienda tienda;
    private final DefaultTableModel tablaModel;
    private final JTable tabla;
    private final TitledBorder bordeFormulario;

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JComboBox<String> comboRol = new JComboBox<>(new String[]{CLIENTE, ADMINISTRADOR});
    private final JPasswordField txtPass = new JPasswordField();

    /** Correo del usuario que se está editando; null cuando el formulario es de alta. */
    private String correoEditando = null;
    private boolean cargando = false;

    public PantallaGestionUsuarios(Tienda tienda, Navegador nav) {
        super(new BorderLayout());
        this.tienda = tienda;

        add(EstiloUI.crearHeaderSuperior("Gestión de Usuarios y Roles"), BorderLayout.NORTH);

        String[] columnas = {"Nombre", "Apellido", "Correo", "Teléfono", "Dirección", "Rol"};
        tablaModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(tablaModel);
        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !cargando) {
                cargarSeleccion();
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 8, 4));
        bordeFormulario = BorderFactory.createTitledBorder("Nuevo usuario");
        formPanel.setBorder(bordeFormulario);
        formPanel.setBackground(Color.WHITE);
        formPanel.add(conTitulo(txtNombre, "Nombre"));
        formPanel.add(conTitulo(txtApellido, "Apellido"));
        formPanel.add(conTitulo(txtCorreo, "Correo"));
        formPanel.add(conTitulo(txtDireccion, "Dirección"));
        formPanel.add(conTitulo(txtTelefono, "Teléfono"));
        formPanel.add(conTitulo(comboRol, "Rol"));
        formPanel.add(conTitulo(txtPass, "Contraseña (vacía = conservar)"));
        formPanel.add(new JLabel(""));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nuevo / Limpiar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnGuardar = EstiloUI.crearBotonEstilizado("Guardar", EstiloUI.PRIMARIO);
        JButton btnVolver = new JButton("Volver al Panel Admin");
        btnNuevo.addActionListener(e -> nuevo());
        btnEliminar.addActionListener(e -> eliminar());
        btnGuardar.addActionListener(e -> guardar());
        btnVolver.addActionListener(e -> nav.irA(Vista.ADMIN_HOME));
        actionPanel.add(btnNuevo);
        actionPanel.add(btnEliminar);
        actionPanel.add(btnGuardar);
        actionPanel.add(btnVolver);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(formPanel, BorderLayout.CENTER);
        panelInferior.add(actionPanel, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
    }

    @Override
    public void refrescar() {
        recargarTabla();
        nuevo();
    }

    // ---------- Acciones ----------
    private void guardar() {
        if (!esAdmin()) return;
        String pass = new String(txtPass.getPassword());
        Rol rol = ADMINISTRADOR.equals(comboRol.getSelectedItem()) ? Rol.ADMIN : Rol.CLIENTE;
        try {
            if (correoEditando == null) {
                tienda.getGestionUsuarios().agregarUsuario(new Usuario(txtNombre.getText(), txtApellido.getText(),
                        txtCorreo.getText(), txtDireccion.getText(), txtTelefono.getText(), rol, pass));
                mensaje("Usuario creado con éxito.");
            } else {
                tienda.getGestionUsuarios().actualizarUsuario(correoEditando, txtNombre.getText(), txtApellido.getText(),
                        txtCorreo.getText(), txtDireccion.getText(), txtTelefono.getText(), rol, pass);
                mensaje("Usuario actualizado con éxito.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refrescar();
    }

    private void eliminar() {
        if (!esAdmin()) return;
        if (correoEditando == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al usuario " + correoEditando + "? Esta acción no se puede deshacer.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            Usuario sesion = tienda.getUsuarioSesion();
            tienda.getGestionUsuarios().eliminarUsuario(correoEditando, sesion == null ? null : sesion.getCorreo());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo eliminar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mensaje("Usuario eliminado.");
        refrescar();
    }

    // ---------- Formulario ----------
    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        Usuario u = tienda.getGestionUsuarios().buscarPorCorreo((String) tablaModel.getValueAt(fila, 2));
        if (u == null) return;
        correoEditando = u.getCorreo();
        txtNombre.setText(u.getNombre());
        txtApellido.setText(u.getApellido());
        txtCorreo.setText(u.getCorreo());
        txtDireccion.setText(u.getDireccion());
        txtTelefono.setText(u.getTelefono());
        comboRol.setSelectedItem(u.getTipoUsuario() == Rol.ADMIN ? ADMINISTRADOR : CLIENTE);
        txtPass.setText("");
        // Un administrador no puede cambiarse el rol a sí mismo desde aquí.
        Usuario sesion = tienda.getUsuarioSesion();
        comboRol.setEnabled(sesion == null || !sesion.getCorreo().equals(u.getCorreo()));
        bordeFormulario.setTitle("Editando: " + u.getCorreo());
        repaint();
    }

    private void nuevo() {
        cargando = true;
        tabla.clearSelection();
        cargando = false;
        correoEditando = null;
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtPass.setText("");
        comboRol.setSelectedItem(CLIENTE);
        comboRol.setEnabled(true);
        bordeFormulario.setTitle("Nuevo usuario (la contraseña es obligatoria)");
        repaint();
    }

    private void recargarTabla() {
        cargando = true;
        tablaModel.setRowCount(0);
        for (Usuario u : tienda.getGestionUsuarios().listarUsuarios()) {
            tablaModel.addRow(new Object[]{
                    u.getNombre(), u.getApellido(), u.getCorreo(), u.getTelefono(), u.getDireccion(),
                    u.getTipoUsuario() == Rol.ADMIN ? ADMINISTRADOR : CLIENTE
            });
        }
        cargando = false;
    }

    // ---------- Auxiliares ----------
    private boolean esAdmin() {
        if (tienda.getRolActual() != Rol.ADMIN) {
            JOptionPane.showMessageDialog(this, "Solo un administrador puede gestionar usuarios.",
                    "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    private static JComponent conTitulo(JComponent campo, String titulo) {
        campo.setBorder(BorderFactory.createTitledBorder(titulo));
        return campo;
    }
}
