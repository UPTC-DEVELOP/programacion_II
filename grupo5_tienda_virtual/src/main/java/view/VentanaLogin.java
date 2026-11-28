package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import service.IServicioCarrito;
import service.IServicioClientes;
import service.IServicioCompras;
import service.IServicioLibros;

/**
 * Ventana "Iniciar Sesion" segun el prototipo.
 */
public class VentanaLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    private final EventosLogin eventos;

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JButton btnAdministrador;

    public VentanaLogin(IServicioClientes controlador, IServicioLibros controladorLibro,
            IServicioCompras controladorCompras, IServicioCarrito controladorCarrito) {
        this.eventos = new EventosLogin(this, controlador, controladorLibro,
                controladorCompras, controladorCarrito);

        setTitle("Iniciar Sesi\u00f3n");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);

        eventos.registrarEventos(txtCorreo, txtContrasena,
                btnIniciarSesion, btnRegistrarse, btnAdministrador);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtCorreo = new JTextField(18);
        txtContrasena = new JPasswordField(18);

        agregarCampo(panel, "Correo electr\u00f3nico:", txtCorreo, 0);
        agregarCampo(panel, "Contrase\u00f1a:", txtContrasena, 1);

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

    private JPanel crearBotones() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel fila = new JPanel();
        btnIniciarSesion = new JButton("Iniciar sesi\u00f3n");
        btnRegistrarse = new JButton("Registrarse");
        fila.add(btnIniciarSesion);
        fila.add(btnRegistrarse);

        JPanel filaAdmin = new JPanel();
        btnAdministrador = new JButton("Entrar como Administrador");
        filaAdmin.add(btnAdministrador);

        contenedor.add(fila);
        contenedor.add(filaAdmin);

        return contenedor;
    }
}