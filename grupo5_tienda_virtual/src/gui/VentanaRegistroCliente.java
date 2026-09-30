package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

    private ControladorCliente controlador;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JComboBox<String> cmbTipo;

    public VentanaRegistroCliente(ControladorCliente controlador) {
        this.controlador = controlador;

        setTitle("Registro de Cliente");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBoton(), BorderLayout.SOUTH);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtNombre = new JTextField(18);
        txtCorreo = new JTextField(18);
        txtDireccion = new JTextField(18);
        txtTelefono = new JTextField(18);
        cmbTipo = new JComboBox<String>(new String[] { "Regular", "Premium" });

        agregarCampo(panel, "Nombre completo:", txtNombre, 0);
        agregarCampo(panel, "Correo electr\u00f3nico:", txtCorreo, 1);
        agregarCampo(panel, "Direcci\u00f3n:", txtDireccion, 2);
        agregarCampo(panel, "Tel\u00e9fono:", txtTelefono, 3);
        agregarCampo(panel, "Tipo de cliente:", cmbTipo, 4);

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
        JButton btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionRegistrarse();
            }
        });
        panel.add(btnRegistrarse);
        return panel;
    }

    private void accionRegistrarse() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y correo son obligatorios.");
            return;
        }

        Cliente cliente;
        if ("Premium".equals(cmbTipo.getSelectedItem())) {
            cliente = new ClientePremium(nombre, correo, "");
        } else {
            cliente = new ClienteRegular(nombre, correo, "");
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
        cmbTipo.setSelectedIndex(0);
    }
}
