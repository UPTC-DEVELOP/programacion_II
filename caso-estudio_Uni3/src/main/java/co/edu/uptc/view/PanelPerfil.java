package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;

// PanelPerfil permite editar datos personales; el controlador delega la validación al servicio.
public class PanelPerfil extends JPanel {
    private final JLabel lblCorreo=new JLabel(); private final JLabel lblTipo=new JLabel(); private final JTextField txtNombre=new JTextField(),txtDireccion=new JTextField(),txtTelefono=new JTextField(); private final JPasswordField txtContrasena=new JPasswordField(); private final JButton btnGuardar=new JButton("Guardar cambios");
    public PanelPerfil(){setLayout(new GridLayout(0,2,6,6));setBorder(BorderFactory.createEmptyBorder(15,15,15,15));add(new JLabel("Correo:"));add(lblCorreo);add(new JLabel("Tipo:"));add(lblTipo);add(new JLabel("Nombre:"));add(txtNombre);add(new JLabel("Dirección:"));add(txtDireccion);add(new JLabel("Teléfono:"));add(txtTelefono);add(new JLabel("Nueva contraseña:"));add(txtContrasena);btnGuardar.setActionCommand("ACTUALIZAR_DATOS");add(new JLabel());add(btnGuardar);}
    public JTextField getTxtNombre(){return txtNombre;}public JTextField getTxtDireccion(){return txtDireccion;}public JTextField getTxtTelefono(){return txtTelefono;}public JPasswordField getTxtContrasena(){return txtContrasena;}public JButton getBtnGuardar(){return btnGuardar;}public JLabel getLblCorreo(){return lblCorreo;}public JLabel getLblTipo(){return lblTipo;}
}
