package co.edu.uptc.gui;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.TipoCliente;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PanelCliente extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JPasswordField txtContrasena;
    private JComboBox<TipoCliente> cmbTipo;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnIniciarSesion;
    private JButton btnCerrarSesion;
    private JLabel lblEstado;

    public PanelCliente() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos del Cliente"),
                BorderFactory.createEmptyBorder(8, 10, 10, 10)));
        setPreferredSize(new Dimension(470, 420));

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtContrasena = new JPasswordField();
        cmbTipo = new JComboBox<>(TipoCliente.values());

        JPanel campos = new JPanel(new GridLayout(0, 2, 8, 10));
        campos.add(new JLabel("Nombre completo:"));
        campos.add(txtNombre);
        campos.add(new JLabel("Correo electrónico:"));
        campos.add(txtCorreo);
        campos.add(new JLabel("Dirección de envío:"));
        campos.add(txtDireccion);
        campos.add(new JLabel("Teléfono:"));
        campos.add(txtTelefono);
        campos.add(new JLabel("Contraseña:"));
        campos.add(txtContrasena);
        campos.add(new JLabel("Tipo de cliente:"));
        campos.add(cmbTipo);

        btnRegistrar = crearBoton("Registrar", Comandos.CLIENTE_REGISTRAR);
        btnActualizar = crearBoton("Actualizar datos", Comandos.CLIENTE_ACTUALIZAR);
        btnIniciarSesion = crearBoton("Iniciar sesión", Comandos.CLIENTE_LOGIN);
        btnCerrarSesion = crearBoton("Cerrar sesión", Comandos.CLIENTE_LOGOUT);
        btnCerrarSesion.setEnabled(false);

        JPanel botones = new JPanel(new GridLayout(0, 2, 8, 8));
        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnIniciarSesion);
        botones.add(btnCerrarSesion);

        lblEstado = new JLabel("Sin sesión iniciada.");
        lblEstado.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        add(campos, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, String comando) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        return boton;
    }

    public void agregarListener(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
        btnActualizar.addActionListener(listener);
        btnIniciarSesion.addActionListener(listener);
        btnCerrarSesion.addActionListener(listener);
    }

    public String getNombre() { return txtNombre.getText().trim(); }
    public String getCorreo() { return txtCorreo.getText().trim(); }
    public String getDireccion() { return txtDireccion.getText().trim(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getContrasena() { return new String(txtContrasena.getPassword()); }
    public TipoCliente getTipo() { return (TipoCliente) cmbTipo.getSelectedItem(); }

    public void cargarCliente(Cliente cliente) {
        txtNombre.setText(cliente.getNombreCompleto());
        txtCorreo.setText(cliente.getCorreoElectronico());
        txtDireccion.setText(cliente.getDireccionEnvio());
        txtTelefono.setText(cliente.getTelefono());
        txtContrasena.setText("");
        cmbTipo.setSelectedItem(cliente.getTipoCliente());
    }

    public void prepararRegistro() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtContrasena.setText("");
        cmbTipo.setSelectedItem(TipoCliente.REGULAR);
        txtCorreo.setEditable(true);
        txtContrasena.setEnabled(true);
        btnRegistrar.setEnabled(true);
        btnActualizar.setEnabled(false);
        actualizarEstado(null);
    }

    public void prepararSesion(Cliente cliente) {
        if (cliente == null) {
            txtCorreo.setEditable(true);
            txtContrasena.setEnabled(true);
            btnRegistrar.setEnabled(true);
            btnActualizar.setEnabled(false);
            btnIniciarSesion.setEnabled(true);
            btnCerrarSesion.setEnabled(false);
            lblEstado.setText("Sin sesión iniciada. La compra requiere autenticación.");
        } else {
            cargarCliente(cliente);
            txtCorreo.setEditable(false);
            btnRegistrar.setEnabled(false);
            btnActualizar.setEnabled(true);
            btnIniciarSesion.setEnabled(false);
            btnCerrarSesion.setEnabled(true);
            lblEstado.setText("Sesión activa: " + cliente.getNombreCompleto()
                    + " | " + cliente.getTipoCliente().getEtiqueta());
        }
    }

    public void actualizarEstado(Cliente cliente) {
        prepararSesion(cliente);
    }
}
