package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// RegistroDialog recoge datos de entrada, pero la validación final queda en AutenticacionService.
public class RegistroDialog extends JDialog {
    private final JTextField txtNombre=new JTextField(22), txtCorreo=new JTextField(22), txtDireccion=new JTextField(22), txtTelefono=new JTextField(22);
    private final JPasswordField txtContrasena=new JPasswordField(22); private final JComboBox<String> cmbTipo=new JComboBox<>(new String[]{"Regular","Premium"}); private final JButton btnRegistrar=new JButton("Registrar"), btnCancelar=new JButton("Cancelar");
    public RegistroDialog(Frame padre){super(padre,"Registro de cliente",true); JPanel campos=new JPanel(new GridLayout(6,2,5,5)); campos.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); campos.add(new JLabel("Nombre:"));campos.add(txtNombre);campos.add(new JLabel("Correo:"));campos.add(txtCorreo);campos.add(new JLabel("Contraseña:"));campos.add(txtContrasena);campos.add(new JLabel("Dirección:"));campos.add(txtDireccion);campos.add(new JLabel("Teléfono:"));campos.add(txtTelefono);campos.add(new JLabel("Tipo:"));campos.add(cmbTipo); JPanel botones=new JPanel();btnRegistrar.setActionCommand("REGISTRAR_CLIENTE");btnCancelar.setActionCommand("CANCELAR_REGISTRO");botones.add(btnRegistrar);botones.add(btnCancelar);add(campos,BorderLayout.CENTER);add(botones,BorderLayout.SOUTH);pack();setLocationRelativeTo(padre);}
    public JTextField getTxtNombre(){return txtNombre;} public JTextField getTxtCorreo(){return txtCorreo;} public JPasswordField getTxtContrasena(){return txtContrasena;} public JTextField getTxtDireccion(){return txtDireccion;} public JTextField getTxtTelefono(){return txtTelefono;} public JComboBox<String> getCmbTipo(){return cmbTipo;} public JButton getBtnRegistrar(){return btnRegistrar;} public JButton getBtnCancelar(){return btnCancelar;}
}
