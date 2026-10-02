package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import util.Estilos;

public class VistaRegistro extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JTextField campoDireccion;
    private JTextField campoTelefono;
    private JComboBox<String> comboTipoCliente;
    private JPasswordField campoContrasena;
    private JPasswordField campoConfirmacion;
    private JButton botonRegistrar;
    private JButton botonVolver;
    private JButton botonLimpiar;
    private JCheckBox casillaMostrarContrasenas;
    private char caracterOculto;

    public VistaRegistro() {
        setTitle("Registro de usuario");
        setSize(590, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(Estilos.CELESTE);
        fondo.setBorder(BorderFactory.createEmptyBorder(24, 34, 24, 34));

        JPanel tarjeta = new JPanel(new BorderLayout(0, 16));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createLineBorder(Estilos.AZUL_VIVO, 2, true));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezado.setBackground(Estilos.AZUL_MARINO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(18, 20, 16, 20));

        JLabel titulo = new JLabel("REGISTRO DE USUARIO", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 21));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Complete los datos para crear su cuenta", JLabel.CENTER);
        subtitulo.setForeground(Estilos.CELESTE);

        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JPanel formulario = new JPanel(new GridLayout(7, 2, 10, 10));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createEmptyBorder(6, 28, 0, 28));

        campoNombre = new JTextField(20);
        campoCorreo = new JTextField(20);
        campoDireccion = new JTextField(20);
        campoTelefono = new JTextField(20);
        comboTipoCliente = new JComboBox<String>(new String[] {"Regular", "Premium"});
        campoContrasena = new JPasswordField(20);
        campoConfirmacion = new JPasswordField(20);

        Estilos.campoTexto(campoNombre);
        Estilos.campoTexto(campoCorreo);
        Estilos.campoTexto(campoDireccion);
        Estilos.campoTexto(campoTelefono);
        Estilos.campoTexto(campoContrasena);
        Estilos.campoTexto(campoConfirmacion);
        Estilos.combo(comboTipoCliente);
        caracterOculto = campoContrasena.getEchoChar();

        agregarCampo(formulario, "Nombre completo:", campoNombre);
        agregarCampo(formulario, "Correo electrónico:", campoCorreo);
        agregarCampo(formulario, "Dirección:", campoDireccion);
        agregarCampo(formulario, "Teléfono:", campoTelefono);

        JLabel etiquetaTipo = new JLabel("Tipo de cliente:");
        etiquetaTipo.setForeground(Estilos.TEXTO);
        formulario.add(etiquetaTipo);
        formulario.add(comboTipoCliente);

        agregarCampo(formulario, "Contraseña:", campoContrasena);
        agregarCampo(formulario, "Confirmar contraseña:", campoConfirmacion);

        casillaMostrarContrasenas = new JCheckBox("Mostrar contraseñas");
        casillaMostrarContrasenas.setBackground(Color.WHITE);
        casillaMostrarContrasenas.setForeground(Estilos.TEXTO);
        casillaMostrarContrasenas.setFocusPainted(false);

        JPanel opcion = new JPanel(new FlowLayout(FlowLayout.LEFT, 28, 0));
        opcion.setBackground(Color.WHITE);
        opcion.add(casillaMostrarContrasenas);

        botonRegistrar = new JButton("Registrar");
        botonLimpiar = new JButton("Limpiar");
        botonVolver = new JButton("Volver");
        Estilos.botonPrincipal(botonRegistrar);
        Estilos.botonSecundario(botonLimpiar);
        Estilos.botonSecundario(botonVolver);
        getRootPane().setDefaultButton(botonRegistrar);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        botones.setBackground(Color.WHITE);
        botones.add(botonVolver);
        botones.add(botonLimpiar);
        botones.add(botonRegistrar);

        JPanel inferior = new JPanel(new BorderLayout());
        inferior.setBackground(Color.WHITE);
        inferior.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        inferior.add(opcion, BorderLayout.NORTH);
        inferior.add(botones, BorderLayout.SOUTH);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(formulario, BorderLayout.CENTER);
        tarjeta.add(inferior, BorderLayout.SOUTH);
        fondo.add(tarjeta, BorderLayout.CENTER);
        add(fondo);
    }

    private void agregarCampo(JPanel panel, String texto, JTextField campo) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(Estilos.TEXTO);
        panel.add(etiqueta);
        panel.add(campo);
    }

    public String getNombre() {
        return campoNombre.getText().trim();
    }

    public String getCorreo() {
        return campoCorreo.getText().trim();
    }

    public String getDireccion() {
        return campoDireccion.getText().trim();
    }

    public String getTelefono() {
        return campoTelefono.getText().trim();
    }

    public String getTipoCliente() {
        return comboTipoCliente.getSelectedItem().toString();
    }

    public String getContrasena() {
        return new String(campoContrasena.getPassword());
    }

    public String getConfirmacion() {
        return new String(campoConfirmacion.getPassword());
    }

    public JButton getBotonRegistrar() {
        return botonRegistrar;
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }

    public JButton getBotonLimpiar() {
        return botonLimpiar;
    }

    public JCheckBox getCasillaMostrarContrasenas() {
        return casillaMostrarContrasenas;
    }

    public void mostrarContrasenas(boolean mostrar) {
        char caracter = mostrar ? (char) 0 : caracterOculto;
        campoContrasena.setEchoChar(caracter);
        campoConfirmacion.setEchoChar(caracter);
    }

    public void limpiarCampos() {
        campoNombre.setText("");
        campoCorreo.setText("");
        campoDireccion.setText("");
        campoTelefono.setText("");
        comboTipoCliente.setSelectedIndex(0);
        campoContrasena.setText("");
        campoConfirmacion.setText("");
        casillaMostrarContrasenas.setSelected(false);
        mostrarContrasenas(false);
        campoNombre.requestFocusInWindow();
    }
}
