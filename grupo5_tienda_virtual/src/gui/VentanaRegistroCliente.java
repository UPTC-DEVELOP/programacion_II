package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import negocio.Cliente;
import negocio.ClientePremium;
import negocio.ClienteRegular;
import negocio.ControladorCliente;

/**
 * Ventana "Registro de Cliente" segun el prototipo.
 */
public class VentanaRegistroCliente extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String TIPO_REGULAR = "Regular";
    private static final String TIPO_PREMIUM = "Premium";

    private ControladorCliente controlador;
    private JFrame padre;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JPasswordField txtContrasena;
    private JComboBox<String> cmbTipo;

    public VentanaRegistroCliente(ControladorCliente controlador, JFrame padre) {
        this.controlador = controlador;
        this.padre = padre;

        setTitle("Registro de Cliente");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBoton(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (padre != null) {
                    padre.setVisible(true);
                }
            }
        });
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
        JButton btnAtras = new JButton("Atr\u00e1s");
        JButton btnRegistrarse = new JButton("Registrarse");
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });
        btnRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionRegistrarse();
            }
        });
        panel.add(btnAtras);
        panel.add(btnRegistrarse);
        return panel;
    }

    private void volver() {
        dispose();
        if (padre != null) {
            padre.setVisible(true);
        }
    }

    private void accionRegistrarse() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nombre, correo y contrase\u00f1a son obligatorios.");
            return;
        }

        Cliente cliente;
        if (TIPO_PREMIUM.equals(cmbTipo.getSelectedItem())) {
            cliente = new ClientePremium(nombre, correo, contrasena);
        } else {
            cliente = new ClienteRegular(nombre, correo, contrasena);
        }
        cliente.setDireccion(direccion);
        cliente.setTelefono(telefono);

        if (controlador.registrar(cliente)) {
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese correo.");
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtContrasena.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}
