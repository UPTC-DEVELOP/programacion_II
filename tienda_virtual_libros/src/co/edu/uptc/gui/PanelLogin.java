package co.edu.uptc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Panel de inicio de sesión con selección de perfil (Usuario / Admin).
 * 
 * @author Brayan Javier Panqueva Pelayo
 * @version 1.0 - Septiembre 2026
 */
@SuppressWarnings("serial")
public class PanelLogin extends JPanel {

    /* CONSTANTES */
    public static final String INGRESAR = "LOGIN_INGRESAR";
    public static final String OLVIDO = "LOGIN_OLVIDO";
    public static final String REGISTRARSE = "LOGIN_REGISTRARSE";

    public static final String PERFIL_USUARIO = "Usuario";
    public static final String PERFIL_ADMIN = "Admin";

    /* TEXTOS Y BOTONES */
    private JTextField txtCorreo;
    private JComboBox<String> comboPerfil;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private JButton btnOlvido;
    private JButton btnCrearCuenta;
    private JLabel lblEstado;

    public PanelLogin() {
        setBackground(new Color(235, 235, 235));
        setLayout(new GridBagLayout());
        add(construirTarjeta(), new GridBagConstraints());
    }
/**metodo para construir la tarjeta de login con los campos de correo, perfil y contraseña, así como los botones de ingresar, olvido de contraseña y registrarse.
 *  */
    private JPanel construirTarjeta() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));

        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        JLabel titulo = new JLabel("Login");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(Color.BLACK);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        txtCorreo = new JTextField(18);
        configurarCampoTexto(txtCorreo, "Usuario / Correo");

        /*
         * /*
         *  DESPLEGABLE DE PERFILES:
         *  PERMITE SELECCIONAR EXPLÍCITAMENTE ENTRE 'Usuario' Y 'Admin'.
         * */
        comboPerfil = new JComboBox<String>(new String[] { PERFIL_USUARIO, PERFIL_ADMIN });
        comboPerfil.setPreferredSize(new Dimension(200, 28));
        comboPerfil.setMaximumSize(new Dimension(200, 28));
        comboPerfil.setBackground(Color.WHITE);
        comboPerfil.setFont(new Font("SansSerif", Font.PLAIN, 12));
        comboPerfil.setAlignmentX(CENTER_ALIGNMENT);

        txtClave = new JPasswordField(18);
        configurarCampoTexto(txtClave, "password");
        txtClave.setEchoChar((char) 0);

        btnIngresar = crearBotonPrototipo("Ingresar", INGRESAR);
        btnOlvido = crearBotonPrototipo("olvido su contrasenia", OLVIDO);
        btnCrearCuenta = crearBotonPrototipo("Registrarse", REGISTRARSE);

        lblEstado = new JLabel(" ", SwingConstants.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(180, 40, 40));
        lblEstado.setAlignmentX(CENTER_ALIGNMENT);

        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(txtCorreo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(comboPerfil);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(txtClave);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(fila(btnIngresar));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(fila(btnOlvido));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(fila(btnCrearCuenta));
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblEstado);

        return tarjeta;
    }

    private void configurarCampoTexto(JTextField campo, String placeholder) {
        campo.setPreferredSize(new Dimension(200, 28));
        campo.setMaximumSize(new Dimension(200, 28));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);
        campo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        campo.setAlignmentX(CENTER_ALIGNMENT);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar('•');
                    }
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar((char) 0);
                    }
                }
            }
        });
    }

    private JButton crearBotonPrototipo(String texto, String comando) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        boton.setBackground(new Color(215, 215, 215));
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        return boton;
    }

    private JPanel fila(JButton boton) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setBackground(Color.WHITE);
        panel.add(boton);
        return panel;
    }

    public void registrarEvento(ActionListener listener) {
        if (btnIngresar != null) btnIngresar.addActionListener(listener);
        if (btnOlvido != null) btnOlvido.addActionListener(listener);
        if (btnCrearCuenta != null) btnCrearCuenta.addActionListener(listener);
        if (txtClave != null) {
            txtClave.addActionListener(listener);
            txtClave.setActionCommand(INGRESAR);
        }
    }
/*getters and setters para obtener el correo, la clave y el perfil seleccionado en el panel de login.
 * */
    public String getCorreo() {
        return txtCorreo.getText().trim();
    }

    public String getClave() {
        return new String(txtClave.getPassword());
    }

  
    public String getPerfil() {
        return (String) comboPerfil.getSelectedItem();
    }

    public boolean esPerfilAdmin() {
        return PERFIL_ADMIN.equals(comboPerfil.getSelectedItem());
    }

    public void mostrarEstado(String mensaje) {
        lblEstado.setText(mensaje == null ? " " : mensaje);
    }

    public void limpiarClave() {
        txtClave.setText("");
    }
}