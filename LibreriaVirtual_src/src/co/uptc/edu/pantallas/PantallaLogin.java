package co.uptc.edu.gui.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Rol;
import co.uptc.edu.model.Tienda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Pantalla de inicio de sesión (simulada). */
public class PantallaLogin extends JPanel {

    public PantallaLogin(Tienda tienda, Navegador nav) {
        super(new GridBagLayout());
        setBackground(EstiloUI.FONDO_LOGIN);

        JPanel card = new JPanel(new GridLayout(6, 1, 10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.PRIMARIO, 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel lblTitle = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(EstiloUI.PRIMARIO);

        JTextField txtUser = new JTextField();
        txtUser.setBorder(BorderFactory.createTitledBorder("Usuario"));

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(BorderFactory.createTitledBorder("Contraseña"));

        JComboBox<String> comboRol = new JComboBox<>(new String[]{"Cliente", "Administrador"});
        comboRol.setBorder(BorderFactory.createTitledBorder("Rol de Acceso"));

        JButton btnLogin = EstiloUI.crearBotonEstilizado("Entrar", EstiloUI.PRIMARIO);
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFocusPainted(false);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnVolver);
        panelBotones.add(btnLogin);

        card.add(lblTitle);
        card.add(txtUser);
        card.add(txtPass);
        card.add(comboRol);
        card.add(new JLabel("*(Tip: Puedes presionar entrar sin datos para simular)", SwingConstants.CENTER));
        card.add(panelBotones);

        btnVolver.addActionListener(e -> nav.irA(Vista.PRESENTACION));
        btnLogin.addActionListener(e -> {
            boolean esAdmin = "Administrador".equals(comboRol.getSelectedItem());
            tienda.iniciarSesion(txtUser.getText(), esAdmin ? Rol.ADMIN : Rol.CLIENTE);
            nav.irA(esAdmin ? Vista.ADMIN_HOME : Vista.USUARIO_HOME);
        });

        add(card);
    }
}
