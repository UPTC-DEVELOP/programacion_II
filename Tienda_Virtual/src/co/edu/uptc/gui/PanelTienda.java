package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

public class PanelTienda extends JPanel {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancelar;

    public PanelTienda(Eventos listener) {
        // Configuración del layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título del formulario
        JLabel lblTitulo = new JLabel("Tienda Virtual de Libros", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        // Subtítulo / Instrucción
        JLabel lblSubtitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(lblSubtitulo, gbc);

        // Etiqueta y Campo de Usuario
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Correo / Usuario:"), gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtUsuario, gbc);

        // Etiqueta y Campo de Contraseña
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(txtPassword, gbc);

        // Botones con sus ActionCommands entre comillas dobles (String)
        btnLogin = new JButton("Ingresar");
        btnLogin.setActionCommand("LOGIN"); // Evita el error "LOGIN cannot be resolved"
        btnLogin.addActionListener(listener);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setActionCommand("CANCELAR"); // Evita el error "CANCELAR cannot be resolved"
        btnCancelar.addActionListener(listener);

        // Panel inferior para organizar los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.add(btnLogin);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);
    }

    // Métodos Getters para obtener los datos si los necesitas en la validación
    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void limpiarCampos() {
        txtUsuario.setText("");
        txtPassword.setText("");
    }
}