package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PanelLogin extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonIngresar;

    public PanelLogin() {

   setTitle("Inicio de Sesión");
   setSize(400, 220);
   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   setLocationRelativeTo(null);
   setResizable(false);

    JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

    panel.add(new JLabel("Usuario:"));

    campoUsuario = new JTextField();
    panel.add(campoUsuario);

    panel.add(new JLabel("Contraseña:"));

    campoContrasena = new JPasswordField();
    panel.add(campoContrasena);

    botonIngresar = new JButton("Ingresar");

    panel.add(new JLabel());
    panel.add(botonIngresar);

     add(panel, BorderLayout.CENTER);

     botonIngresar.addActionListener(e -> iniciarSesion());
        }

    private void iniciarSesion() {

        String usuario = campoUsuario.getText();

        String contrasena =
                new String(campoContrasena.getPassword());

        if (!usuario.isEmpty() && !contrasena.isEmpty()) {

            PanelCentral panelCentral = new PanelCentral();

            panelCentral.setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese usuario y contraseña"
            );
        }
    }
}