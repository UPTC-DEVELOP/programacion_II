package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import service.IServicioClientes;

/**
 * Manejador de eventos de la ventana de registro de clientes.
 */
public class EventosRegistroCliente extends Eventos {

    private final IServicioClientes controlador;

    public EventosRegistroCliente(VentanaRegistroCliente vista, IServicioClientes controlador) {
        super(vista);
        this.controlador = controlador;
    }

    public void registrarEventos(JTextField txtNombre, JTextField txtCorreo,
            JTextField txtDireccion, JTextField txtTelefono,
            JPasswordField txtContrasena, JComboBox<String> cmbTipo,
            JButton btnAtras, JButton btnRegistrarse, JFrame padre) {
        btnAtras.addActionListener(e -> volver(padre));
        btnRegistrarse.addActionListener(e -> registrarCliente(
                txtNombre, txtCorreo, txtDireccion, txtTelefono, txtContrasena, cmbTipo));

        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (padre != null) {
                    padre.setVisible(true);
                }
            }
        });
    }

    private void registrarCliente(JTextField txtNombre, JTextField txtCorreo,
            JTextField txtDireccion, JTextField txtTelefono,
            JPasswordField txtContrasena, JComboBox<String> cmbTipo) {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        String tipo = (String) cmbTipo.getSelectedItem();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Nombre, correo y contrase\u00f1a son obligatorios.");
            return;
        }

        if (controlador.registrarCliente(tipo, nombre, correo, direccion, telefono, contrasena)) {
            mostrarInfo("Cliente registrado correctamente.");
            limpiarFormulario(txtNombre, txtCorreo, txtDireccion, txtTelefono, txtContrasena, cmbTipo);
        } else if (controlador.buscar(correo) != null) {
            mostrarError("Ya existe un cliente con ese correo.");
        } else {
            mostrarError("No se pudo guardar el cliente. Verifique el almacenamiento.");
        }
    }

    private void limpiarFormulario(JTextField txtNombre, JTextField txtCorreo,
            JTextField txtDireccion, JTextField txtTelefono,
            JPasswordField txtContrasena, JComboBox<String> cmbTipo) {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtContrasena.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}