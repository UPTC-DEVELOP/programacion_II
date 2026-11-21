package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Cliente;
import service.IServicioClientes;
import service.IServicioCompras;
import service.IServicioLibros;

/**
 * Ventana "Mi Cuenta": el cliente actualiza sus datos (segun el prototipo).
 */
public class VentanaMiCuenta extends JFrame {

    private static final long serialVersionUID = 1L;

    private final EventosMiCuenta eventos;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JButton btnAtras;
    private JButton btnGuardar;
    private JButton btnCompras;
    private JButton btnHistorial;

    public VentanaMiCuenta(
            IServicioClientes controlador, IServicioLibros controladorLibro,
            IServicioCompras controladorCompras, Cliente cliente, JFrame padre) {
        this.eventos = new EventosMiCuenta(
                this, controlador, controladorLibro, controladorCompras, cliente);

        setTitle("Mi Cuenta");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBoton(), BorderLayout.SOUTH);

        eventos.registrarEventos(txtNombre, txtCorreo, txtDireccion, txtTelefono,
                btnAtras, btnGuardar, btnCompras, btnHistorial, padre);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtNombre = new JTextField(18);
        txtCorreo = new JTextField(18);
        txtCorreo.setEditable(false);
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
        btnAtras = new JButton("Atr\u00e1s");
        btnGuardar = new JButton("Guardar cambios");
        btnCompras = new JButton("Compras");
        btnHistorial = new JButton("Historial");

        panel.add(btnAtras);
        panel.add(btnGuardar);
        panel.add(btnCompras);
        panel.add(btnHistorial);
        return panel;
    }
}