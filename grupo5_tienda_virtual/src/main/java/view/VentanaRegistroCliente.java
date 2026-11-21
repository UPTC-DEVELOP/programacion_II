package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import service.IServicioClientes;

/**
 * Ventana "Registro de Cliente" segun el prototipo.
 */
public class VentanaRegistroCliente extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String TIPO_REGULAR = "Regular";
    private static final String TIPO_PREMIUM = "Premium";

    private final EventosRegistroCliente eventos;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JPasswordField txtContrasena;
    private JComboBox<String> cmbTipo;
    private JButton btnAtras;
    private JButton btnRegistrarse;

    public VentanaRegistroCliente(IServicioClientes controlador, JFrame padre) {
        this.eventos = new EventosRegistroCliente(this, controlador);

        setTitle("Registro de Cliente");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBoton(), BorderLayout.SOUTH);

        eventos.registrarEventos(txtNombre, txtCorreo, txtDireccion, txtTelefono,
                txtContrasena, cmbTipo, btnAtras, btnRegistrarse, padre);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtNombre = new JTextField(18);
        txtCorreo = new JTextField(18);
        txtDireccion = new JTextField(18);
        txtTelefono = new JTextField(18);
        txtContrasena = new JPasswordField(18);
        cmbTipo = new JComboBox<String>(new String[] { TIPO_REGULAR, TIPO_PREMIUM });

        agregarCampo(panel, "Nombre completo:", txtNombre, 0);
        agregarCampo(panel, "Correo electr\u00f3nico:", txtCorreo, 1);
        agregarCampo(panel, "Direcci\u00f3n:", txtDireccion, 2);
        agregarCampo(panel, "Tel\u00e9fono:", txtTelefono, 3);
        agregarCampo(panel, "Contrase\u00f1a:", txtContrasena, 4);
        agregarCampo(panel, "Tipo de cliente:", cmbTipo, 5);

        return panel;
    }

    private void agregarCampo(JPanel panel, String etiqueta, Component campo, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private JPanel crearBoton() {
        JPanel panel = new JPanel();
        btnAtras = new JButton("Atr\u00e1s");
        btnRegistrarse = new JButton("Registrarse");
        panel.add(btnAtras);
        panel.add(btnRegistrarse);
        return panel;
    }
}
