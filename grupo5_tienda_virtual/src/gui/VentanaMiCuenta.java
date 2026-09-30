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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import negocio.Cliente;
import negocio.ClienteRegular;
import negocio.ControladorCliente;

/**
 * Ventana "Mi Cuenta": el cliente actualiza sus datos (segun el prototipo).
 */
public class VentanaMiCuenta extends JFrame {

    private static final long serialVersionUID = 1L;

    private ControladorCliente controlador;
    private Cliente cliente;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;

    public VentanaMiCuenta(ControladorCliente controlador, Cliente cliente) {
        this.controlador = controlador;
        this.cliente = cliente;

        setTitle("Mi Cuenta");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBoton(), BorderLayout.SOUTH);

        cargarDatos();
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtNombre = new JTextField(18);
        txtCorreo = new JTextField(18);
        txtDireccion = new JTextField(18);
        txtTelefono = new JTextField(18);

        agregarCampo(panel, "Nombre completo:", txtNombre, 0);
        agregarCampo(panel, "Correo electr\u00f3nico:", txtCorreo, 1);
        agregarCampo(panel, "Direcci\u00f3n:", txtDireccion, 2);
        agregarCampo(panel, "Tel\u00e9fono:", txtTelefono, 3);

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
        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionGuardar();
            }
        });
        panel.add(btnGuardar);
        return panel;
    }

    private void cargarDatos() {
        if (cliente == null) {
            return;
        }
        txtNombre.setText(cliente.getNombreCompleto());
        txtCorreo.setText(cliente.getCorreo());
        txtDireccion.setText(cliente.getDireccion());
        txtTelefono.setText(cliente.getTelefono());
    }

    private void accionGuardar() {
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No hay sesion iniciada.");
            return;
        }

        Cliente datos = new ClienteRegular();
        datos.setNombreCompleto(txtNombre.getText().trim());
        datos.setDireccion(txtDireccion.getText().trim());
        datos.setTelefono(txtTelefono.getText().trim());
        datos.setContrasena(cliente.getContrasena());

        if (controlador.actualizar(cliente.getCorreo(), datos)) {
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron actualizar los datos.");
        }
    }
}
