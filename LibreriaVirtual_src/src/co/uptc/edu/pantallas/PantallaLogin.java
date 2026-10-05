package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Rol;
import co.uptc.edu.model.Tienda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Pantalla de inicio de sesión con correo y contraseña (RF-01). */
public class PantallaLogin extends JPanel implements Refrescable {

    private final Tienda tienda;
    private final JTextField txtCorreo = new JTextField();
    private final JPasswordField txtPass = new JPasswordField();

    public PantallaLogin(Tienda tienda, Navegador nav) {
        super(new GridBagLayout());
        this.tienda = tienda;
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

        txtCorreo.setBorder(BorderFactory.createTitledBorder("Correo electrónico"));
        txtPass.setBorder(BorderFactory.createTitledBorder("Contraseña"));

        JLabel lblTip = new JLabel("<html><center>Demo: cliente@bibliotech.com / Cliente123<br>"
                + "admin@bibliotech.com / Admin123</center></html>", SwingConstants.CENTER);
        lblTip.setForeground(EstiloUI.TEXTO_INFO);

        JButton btnRegistro = new JButton("¿No tienes cuenta? Regístrate");
        btnRegistro.setFocusPainted(false);

        JButton btnLogin = EstiloUI.crearBotonEstilizado("Entrar", EstiloUI.PRIMARIO);
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFocusPainted(false);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnVolver);
        panelBotones.add(btnLogin);

        card.add(lblTitle);
        card.add(txtCorreo);
        card.add(txtPass);
        card.add(lblTip);
        card.add(btnRegistro);
        card.add(panelBotones);

        btnVolver.addActionListener(e -> nav.irA(Vista.PRESENTACION));
        btnRegistro.addActionListener(e -> nav.irA(Vista.REGISTRO));
        java.awt.event.ActionListener entrar = e -> {
            String contrasena = new String(txtPass.getPassword());
            if (tienda.iniciarSesion(txtCorreo.getText(), contrasena)) {
                txtPass.setText("");
                nav.irA(tienda.getRolActual() == Rol.ADMIN ? Vista.ADMIN_HOME : Vista.USUARIO_HOME);
            } else {
                JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos.",
                        "No se pudo iniciar sesión", JOptionPane.ERROR_MESSAGE);
            }
        };
        btnLogin.addActionListener(entrar);
        txtPass.addActionListener(entrar);

        add(card);
    }

    /** Cada vez que se muestra el login se cierra la sesión anterior y se limpian los campos. */
    @Override
    public void refrescar() {
        tienda.cerrarSesion();
        txtCorreo.setText("");
        txtPass.setText("");
    }
}
