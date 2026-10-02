package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import negocio.Cliente;
import negocio.ControladorCliente;
import negocio.ControladorLibro;

/**
 * Ventana "Iniciar Sesion" segun el prototipo.
 */
public class VentanaLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    private ControladorCliente controlador;
    private ControladorLibro controladorLibro;
    
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;

    public VentanaLogin(ControladorCliente controlador, ControladorLibro controladorLibro) {
        this.controlador = controlador;
        this.controladorLibro = controladorLibro;
        
        setTitle("Iniciar Sesi\u00f3n");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);
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
        JButton btnIniciarSesion = new JButton("Iniciar sesi\u00f3n");
        JButton btnRegistrarse = new JButton("Registrarse");
        fila.add(btnIniciarSesion);
        fila.add(btnRegistrarse);

        JPanel filaAdmin = new JPanel();
        JButton btnAdministrador = new JButton("Entrar como Administrador");
        filaAdmin.add(btnAdministrador);

        contenedor.add(fila);
        contenedor.add(filaAdmin);

        btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionIniciarSesion();
            }
        });

        btnRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaRegistroCliente(controlador, VentanaLogin.this).setVisible(true);
                setVisible(false);
            }
        });

        btnAdministrador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(VentanaLogin.this,
                        "Acceso de administrador (pendiente).");
            }
        });

        return contenedor;
    }

    private void accionIniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Correo y contraseña obligatorios.");
            return;
        }

        Cliente cliente = controlador.iniciarSesion(correo, contrasena);
        if (cliente != null) {
        	new VentanaMiCuenta(
        	        controlador,
        	        controladorLibro,
        	        cliente,
        	        VentanaLogin.this
        	).setVisible(true); 
        } else {
            JOptionPane.showMessageDialog(this, "Correo o contrase\u00f1a no v\u00e1lidos.");
        }
    }
}