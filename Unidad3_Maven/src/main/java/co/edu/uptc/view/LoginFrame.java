package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

// LoginFrame solo dibuja el formulario de acceso y expone sus componentes al controlador.
public class LoginFrame extends JFrame {
    private final JTextField txtCorreo = new JTextField(25);
    private final JPasswordField txtContrasena = new JPasswordField(25);
    private final JButton btnIniciar = new JButton("Iniciar sesión");
    private final JButton btnRegistro = new JButton("Crear cuenta");
    public LoginFrame() {
        setTitle("Tienda Virtual de Libros - Acceso"); setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); setMinimumSize(new Dimension(430, 250)); setLocationRelativeTo(null);
        JPanel formulario = new JPanel(new GridBagLayout()); GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(6,6,6,6); c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;c.gridy=0;formulario.add(new JLabel("Correo:"),c); c.gridx=1;formulario.add(txtCorreo,c);
        c.gridx=0;c.gridy=1;formulario.add(new JLabel("Contraseña:"),c); c.gridx=1;formulario.add(txtContrasena,c);
        JPanel botones = new JPanel(new FlowLayout()); btnIniciar.setActionCommand("INICIAR_SESION"); btnRegistro.setActionCommand("ABRIR_REGISTRO"); botones.add(btnIniciar); botones.add(btnRegistro);
        add(formulario, BorderLayout.CENTER); add(botones, BorderLayout.SOUTH); pack();
    }
    public JTextField getTxtCorreo(){return txtCorreo;} public JPasswordField getTxtContrasena(){return txtContrasena;} public JButton getBtnIniciar(){return btnIniciar;} public JButton getBtnRegistro(){return btnRegistro;}
}
