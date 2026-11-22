package co.edu.uptc.gui;

import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.*;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.UbicacionesColombia;
import co.edu.uptc.model.enums.TipoCliente;
import co.edu.uptc.negocio.dto.CredencialDto;

public class PanelUsuario extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField txtId, txtNombre, txtCorreo, txtDireccion, txtTelefono;
    private JPasswordField txtPass;
    private JCheckBox chkMostrarPass;
    private JComboBox<String> cbDepto, cbCiudad;
    private JComboBox<TipoCliente> cbTipoCliente;
    private JLabel lblEstadoUsuario;

    public PanelUsuario(Evento evento) {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        initComponentes(evento);
    }

    private void initComponentes(Evento evento) {
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(30);
        txtNombre = new JTextField(30);
        txtCorreo = new JTextField(30);
        txtPass = new JPasswordField(22);
        chkMostrarPass = new JCheckBox("Mostrar");
        chkMostrarPass.addActionListener(e -> txtPass.setEchoChar(chkMostrarPass.isSelected() ? (char) 0 : '•'));

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
        cbTipoCliente = new JComboBox<>(new TipoCliente[]{TipoCliente.ESTANDAR, TipoCliente.ADMINISTRADOR, TipoCliente.VIP});

        int row = 0;
        agregarFila(panelForm, "ID / Cédula:", txtId, gbc, row++);
        agregarFila(panelForm, "Nombre Completo:", txtNombre, gbc, row++);
        agregarFila(panelForm, "Correo Electrónico:", txtCorreo, gbc, row++);
        agregarFila(panelForm, "Contraseña (mín. 6 caracteres):", panelPass, gbc, row++);
        agregarFila(panelForm, "Dirección Envío:", txtDireccion, gbc, row++);
        agregarFila(panelForm, "Departamento:", cbDepto, gbc, row++);
        agregarFila(panelForm, "Ciudad:", cbCiudad, gbc, row++);
        agregarFila(panelForm, "Teléfono:", txtTelefono, gbc, row++);
        agregarFila(panelForm, "Tipo de Cliente:", cbTipoCliente, gbc, row++);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotones.setPreferredSize(new Dimension(0, 90));

        panelBotones.add(crearBoton("Registrar Usuario", Evento.REGISTRAR_USUARIO, evento));
        panelBotones.add(crearBoton("Iniciar Sesión", Evento.INICIAR_SESION, evento));
        panelBotones.add(crearBoton("Actualizar Datos del Usuario", Evento.ACTUALIZAR_USUARIO, evento));
        panelBotones.add(crearBoton("Eliminar mi cuenta", Evento.ELIMINAR_USUARIO, evento));
        panelBotones.add(crearBoton("Se me olvidó la contraseña", Evento.OLVIDO_PASSWORD, evento));
        panelBotones.add(crearBoton("Cerrar Sesión", Evento.CERRAR_SESION, evento));

        lblEstadoUsuario = new JLabel("Sin iniciar sesión", SwingConstants.CENTER);
        lblEstadoUsuario.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEstadoUsuario.setForeground(new Color(0, 128, 0));
        panelBotones.add(lblEstadoUsuario);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, String comando, Evento evento) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(comando);
        btn.addActionListener(evento);
        return btn;
    }

    private void agregarFila(JPanel panel, String etiqueta, JComponent comp, GridBagConstraints gbc, int row) {
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

    public CredencialDto obtenerCredenciales() {
        return new CredencialDto(txtCorreo.getText().trim(), new String(txtPass.getPassword()));
    }

    public Cliente construirClienteDesdeFormulario(String idFijo, String correoFijo) {
        String id = (idFijo != null) ? idFijo : txtId.getText().trim();
        String correo = (correoFijo != null) ? correoFijo : txtCorreo.getText().trim();
        TipoCliente tipo = (TipoCliente) cbTipoCliente.getSelectedItem();

        Cliente c = new Cliente(
            id, txtNombre.getText().trim(), correo, txtDireccion.getText().trim(),
            (String) cbDepto.getSelectedItem(), (String) cbCiudad.getSelectedItem(),
            txtTelefono.getText().trim(), tipo != null ? tipo.name() : TipoCliente.ESTANDAR.name()
        );
        c.setContrasenaHash(new String(txtPass.getPassword()));
        return c;
    }

    public String obtenerPasswordIngresada() {
        return new String(txtPass.getPassword());
    }

    public void cargarDatosUsuario(Cliente c) {
        txtId.setText(c.getId());
        txtNombre.setText(c.getNombreCompleto());
        txtCorreo.setText(c.getCorreo());
        txtDireccion.setText(c.getDireccionEnvio());
        txtTelefono.setText(c.getTelefono());
        cbDepto.setSelectedItem(c.getDepartamento());
        actualizarCiudades();
        cbCiudad.setSelectedItem(c.getCiudad());
        try {
            cbTipoCliente.setSelectedItem(TipoCliente.valueOf(c.getTipoCliente().toUpperCase()));
        } catch (Exception e) {
            cbTipoCliente.setSelectedItem(TipoCliente.ESTANDAR);
        }
        txtPass.setText("");
        txtId.setEditable(false);
        txtCorreo.setEditable(false);
        lblEstadoUsuario.setText("Usuario: " + c.getNombreCompleto() + " (" + c.getTipoCliente() + ")");
    }

    public void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtPass.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtId.setEditable(true);
        txtCorreo.setEditable(true);
        lblEstadoUsuario.setText("Sin iniciar sesión");
    }

    public void limpiarPassword() {
        txtPass.setText("");
    }
}
