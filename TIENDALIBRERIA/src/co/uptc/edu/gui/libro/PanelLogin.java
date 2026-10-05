package co.uptc.edu.gui.libro;

import javax.swing.*;
import java.awt.*;

public class PanelLogin extends JPanel {
    private JTextField txUsuario;
    private JPasswordField txContrasenia;
    private JButton btnLogin;
    private JButton btnCancelar;

    public PanelLogin(Evento evento) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txUsuario = new JTextField(15);
        txContrasenia = new JPasswordField(15);
        btnLogin = new JButton(Evento.LOGIN);
        btnCancelar =new JButton(Evento.CANCELAR);
        
        btnLogin.setActionCommand(Evento.LOGIN);
        btnLogin.addActionListener(evento);
        btnCancelar.setActionCommand(Evento.CANCELAR);
        btnCancelar.addActionListener(evento);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        add(txUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        add(txContrasenia, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(btnCancelar, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        add(btnLogin, gbc);
    }

    public String getUsuario() {
        return txUsuario.getText();
    }

    public String getContrasenia() {
        return new String(txContrasenia.getPassword());
    }
}