package co.uptc.edu.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDocumento;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JComboBox<String> cmbTipoUsuario;

    private JButton btnRegistrar;
    private JButton btnLimpiar;
    private JButton btnSalir;

    public Login() {

        setTitle("Registro de Usuarios");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        crearInterfaz();
    }

    private void crearInterfaz() {

        // Layout principal
        setLayout(new BorderLayout());

        // TÍTULO
        JLabel titulo = new JLabel(
                "REGISTRO DE USUARIOS",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        add(titulo, BorderLayout.NORTH);

        // PANEL DEL FORMULARIO
      
        JPanel panelFormulario = new JPanel();

        panelFormulario.setLayout(new GridLayout(6, 2, 10, 10));

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        // Nombre
        panelFormulario.add(new JLabel("Nombre:"));

        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        // Apellido
        panelFormulario.add(new JLabel("Apellido:"));

        txtApellido = new JTextField();
        panelFormulario.add(txtApellido);

        // Documento
        panelFormulario.add(new JLabel("Documento:"));

        txtDocumento = new JTextField();
        panelFormulario.add(txtDocumento);

        // Correo
        panelFormulario.add(new JLabel("Correo:"));

        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo);

        // Teléfono
        panelFormulario.add(new JLabel("Teléfono:"));

        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);

        // Tipo de usuario
        panelFormulario.add(new JLabel("Tipo de usuario:"));

        String[] tipos = {
            "Regular",
            "Premium",
            "Corporativo"
        };

        cmbTipoUsuario = new JComboBox<>(tipos);

        panelFormulario.add(cmbTipoUsuario);

        add(panelFormulario, BorderLayout.CENTER);
        
        // BOTONES
       
        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(new FlowLayout());

        btnRegistrar = new JButton("REGISTRAR");
        btnLimpiar = new JButton("LIMPIAR");
        btnSalir = new JButton("SALIR");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.SOUTH);

        // EVENTOS
        
        btnRegistrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(
                        Login.this,
                        "Invocación: registrarUsuario()\n"
                        + "El usuario será registrado en el sistema.",
                        "Registro de usuario",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                limpiarFormulario();

                JOptionPane.showMessageDialog(
                       Login.this,
                        "Invocación: limpiarFormulario()\n"
                        + "Los campos han sido limpiados.",
                        "Limpiar",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        btnSalir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(
                        Login.this,
                        "Invocación: salir()\n"
                        + "Cerrando el módulo de registro.",
                        "Salir",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
            }
        });
    }

    private void limpiarFormulario() {

        txtNombre.setText("");
        txtApellido.setText("");
        txtDocumento.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");

        cmbTipoUsuario.setSelectedIndex(0);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Login ventana = new Login();

            ventana.setVisible(true);
        });
    }
}