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
 * Panel de registro de usuarios simplificado (Cliente / Administrador).
 * 
 * @author Brayan Javier Panqueva Pelayo
 * @version 1.3 - Septiembre 2026
 */
@SuppressWarnings("serial")
public class PanelRegistro extends JPanel {

    /* CONSTANTES DE ACCIÓN */
    public static final String REGISTRAR = "REGISTRO_REGISTRAR";
    public static final String CANCELAR = "REGISTRO_CANCELAR";

    /* COMPONENTES DEL FORMULARIO */
    private JTextField txtNombreUsuario;
    private JTextField txtCorreo;
    private JTextField txtCelular;
    private JTextField txtIdentificacion;
    private JTextField txtDireccion;
    private JPasswordField txtContrasenia;
    private JComboBox<String> comboTipoUsuario;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private JLabel lblEstado;

    public PanelRegistro() {
        setBackground(new Color(235, 235, 235));
        setLayout(new GridBagLayout());
        add(construirTarjeta(), new GridBagConstraints());
    }

    private JPanel construirTarjeta() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel titulo = new JLabel("Registro de Usuario");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(Color.BLACK);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        // Campos de entrada
        txtNombreUsuario = crearCampoTexto("Input");
        txtCorreo = crearCampoTexto("Input");
        txtCelular = crearCampoTexto("Input");
        txtIdentificacion = crearCampoTexto("Input");
        txtDireccion = crearCampoTexto("Input");
        
        txtContrasenia = new JPasswordField();
        configurarEstiloCampo(txtContrasenia, "Input");
        txtContrasenia.setEchoChar((char) 0);

        // Únicamente dos tipos de roles
        comboTipoUsuario = new JComboBox<>(new String[] { "Cliente", "cliente VIP", "Administrador" });
        comboTipoUsuario.setPreferredSize(new Dimension(220, 28));
        comboTipoUsuario.setMaximumSize(new Dimension(220, 28));
        comboTipoUsuario.setBackground(Color.WHITE);
        comboTipoUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Estructura vertical del formulario
        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(15));
        
        tarjeta.add(crearFilaCampo("Nombre usuario", txtNombreUsuario));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(crearFilaCampo("Correo electrónico", txtCorreo));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(crearFilaCampo("Celular", txtCelular));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(crearFilaCampo("Identificación", txtIdentificacion));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(crearFilaCampo("Dirección", txtDireccion));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(crearFilaCampo("Contraseña", txtContrasenia));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(crearFilaCampo("Tipo de Usuario", comboTipoUsuario));

        // Botones de acción
        btnRegistrar = crearBotonPrototipo("Registrar", REGISTRAR);
        btnCancelar = crearBotonPrototipo("Cancelar", CANCELAR);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);

        lblEstado = new JLabel(" ", SwingConstants.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(180, 40, 40));
        lblEstado.setAlignmentX(CENTER_ALIGNMENT);

        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(panelBotones);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblEstado);

        return tarjeta;
    }

    private JPanel crearFilaCampo(String textoEtiqueta, java.awt.Component campo) {
        JPanel panelFila = new JPanel();
        panelFila.setLayout(new BoxLayout(panelFila, BoxLayout.X_AXIS));
        panelFila.setBackground(Color.WHITE);
        
        JLabel etiqueta = new JLabel(textoEtiqueta);
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        etiqueta.setForeground(Color.BLACK);
        etiqueta.setPreferredSize(new Dimension(120, 25));
        etiqueta.setMaximumSize(new Dimension(120, 25));

        panelFila.add(etiqueta);
        panelFila.add(Box.createHorizontalStrut(10));
        panelFila.add(campo);
        
        panelFila.setAlignmentX(CENTER_ALIGNMENT);
        return panelFila;
    }

    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField();
        configurarEstiloCampo(campo, placeholder);
        return campo;
    }

    private void configurarEstiloCampo(JTextField campo, String placeholder) {
        campo.setPreferredSize(new Dimension(220, 28));
        campo.setMaximumSize(new Dimension(220, 28));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);

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
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return boton;
    }

    public void registrarEvento(ActionListener listener) {
        if (btnRegistrar != null) btnRegistrar.addActionListener(listener);
        if (btnCancelar != null) btnCancelar.addActionListener(listener);
    }

    /* GETTERS */
    public String getNombreUsuario() {
        return txtNombreUsuario.getText().trim();
    }

    public String getCorreo() {
        return txtCorreo.getText().trim();
    }

    public String getCelular() {
        return txtCelular.getText().trim();
    }

    public String getIdentificacion() {
        return txtIdentificacion.getText().trim();
    }

    public String getDireccion() {
        return txtDireccion.getText().trim();
    }

    public String getContrasenia() {
        return new String(txtContrasenia.getPassword());
    }

    public String getTipoUsuario() {
        return (String) comboTipoUsuario.getSelectedItem();
    }

    public void mostrarEstado(String mensaje) {
        lblEstado.setText(mensaje == null ? " " : mensaje);
    }

    public void limpiarCampos() {
        txtNombreUsuario.setText("Input");
        txtNombreUsuario.setForeground(Color.GRAY);
        txtCorreo.setText("Input");
        txtCorreo.setForeground(Color.GRAY);
        txtCelular.setText("Input");
        txtCelular.setForeground(Color.GRAY);
        txtIdentificacion.setText("Input");
        txtIdentificacion.setForeground(Color.GRAY);
        txtDireccion.setText("Input");
        txtDireccion.setForeground(Color.GRAY);
        txtContrasenia.setText("Input");
        txtContrasenia.setForeground(Color.GRAY);
        txtContrasenia.setEchoChar((char) 0);
        lblEstado.setText(" ");
    }
}