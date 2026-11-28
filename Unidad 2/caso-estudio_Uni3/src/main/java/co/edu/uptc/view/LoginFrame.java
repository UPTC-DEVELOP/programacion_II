package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;

// LoginFrame solo dibuja el formulario de acceso y expone sus componentes al controlador.
public class LoginFrame extends JFrame {
    private final JTextField txtCorreo = new JTextField(25);
    private final JPasswordField txtContrasena = new JPasswordField(25);
    private final JButton btnIniciar = new JButton("Iniciar sesión");
    private final JButton btnRegistro = new JButton("Crear cuenta");
    public LoginFrame() {
        setTitle("Tienda Virtual de Libros - Acceso"); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setMinimumSize(new Dimension(430, 250)); setLocationRelativeTo(null);
        JPanel formulario = new JPanel(new GridBagLayout()); GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(6,6,6,6); c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;c.gridy=0;formulario.add(new JLabel("Correo:"),c); c.gridx=1;formulario.add(txtCorreo,c);
        c.gridx=0;c.gridy=1;formulario.add(new JLabel("Contraseña:"),c); c.gridx=1;formulario.add(txtContrasena,c);
        JPanel botones = new JPanel(new FlowLayout()); btnIniciar.setActionCommand("INICIAR_SESION"); btnRegistro.setActionCommand("ABRIR_REGISTRO"); botones.add(btnIniciar); botones.add(btnRegistro);
        add(formulario, BorderLayout.CENTER); add(botones, BorderLayout.SOUTH); pack();
    }
    public JTextField getTxtCorreo(){return txtCorreo;} public JPasswordField getTxtContrasena(){return txtContrasena;} public JButton getBtnIniciar(){return btnIniciar;} public JButton getBtnRegistro(){return btnRegistro;}
}
