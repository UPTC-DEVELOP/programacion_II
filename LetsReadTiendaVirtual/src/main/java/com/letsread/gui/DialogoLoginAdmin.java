package com.letsread.gui;

import com.letsread.persistence.ArchivoTxtDAO;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DialogoLoginAdmin extends JDialog {
    private static final long serialVersionUID = 1L;

    private static final String ADMIN_USER = "";  //se le puede colocar nombe de admin y 
   // ontraseña para que no sea tan obvio, pero por motivos de seguridad se deja vacio
    
    private static final String ADMIN_PASS = "";

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private boolean autenticado = false;

    public DialogoLoginAdmin(Frame parent) {
        super(parent, "Ingreso Administrador - Let's Read", true);
        setSize(360, 200);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        txtUsuario = new JTextField();
        txtClave = new JPasswordField();

        panelCampos.add(new JLabel("Usuario Admin:"));
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtClave);

        JPanel panelBotones = new JPanel();
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnCancelar = new JButton("Cancelar");

        btnIngresar.addActionListener(e -> validarCredenciales());
        btnCancelar.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(btnIngresar);

        panelBotones.add(btnIngresar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void validarCredenciales() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());

        if (ADMIN_USER.equals(usuario) && ADMIN_PASS.equals(clave)) {
            autenticado = true;
            ArchivoTxtDAO.registrarOperacion("Inicio de sesión exitoso de Administrador (" + usuario + ").");
            dispose();
        } else {
            ArchivoTxtDAO.registrarOperacion("Intento fallido de acceso Administrador con usuario: " + usuario);
            JOptionPane.showMessageDialog(this, "Credenciales de administrador incorrectas.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            txtClave.setText("");
        }
    }

    public boolean isAutenticado() {
        return autenticado;
    }
}