package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import util.Estilos;

public class VistaLogin extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField campoCorreo;
    private JPasswordField campoContrasena;
    private JButton botonIngresar;
    private JButton botonRegistrar;
    private JButton botonRecuperar;
    private JButton botonLimpiar;
    private JButton botonMostrarContrasena;
    private char caracterOculto;
    private boolean contrasenaVisible;

    public VistaLogin() {
        setTitle("Tienda Virtual de Libros");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(Estilos.CELESTE);
        fondo.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JPanel tarjeta = new JPanel(new BorderLayout(0, 18));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createLineBorder(Estilos.AZUL_VIVO, 2, true));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezado.setBackground(Estilos.AZUL_MARINO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(20, 20, 18, 20));

        JLabel titulo = new JLabel("TIENDA VIRTUAL DE LIBROS", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 21));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Acceso a la librería digital", JLabel.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(Estilos.CELESTE);

        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(BorderFactory.createEmptyBorder(8, 28, 12, 28));

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 12));
        formulario.setBackground(Color.WHITE);

        JLabel etiquetaCorreo = new JLabel("Correo electrónico:");
        JLabel etiquetaContrasena = new JLabel("Contraseña:");
        etiquetaCorreo.setForeground(Estilos.TEXTO);
        etiquetaContrasena.setForeground(Estilos.TEXTO);

        campoCorreo = new JTextField(20);
        campoContrasena = new JPasswordField(20);
        Estilos.campoTexto(campoCorreo);
        Estilos.campoTexto(campoContrasena);
        caracterOculto = campoContrasena.getEchoChar();

        formulario.add(etiquetaCorreo);
        formulario.add(campoCorreo);
        formulario.add(etiquetaContrasena);
        formulario.add(campoContrasena);

        botonMostrarContrasena = new JButton("Mostrar contraseña");
        Estilos.botonClaro(botonMostrarContrasena);

        JPanel panelMostrar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelMostrar.setBackground(Color.WHITE);
        panelMostrar.add(botonMostrarContrasena);

        botonIngresar = new JButton("Iniciar sesión");
        botonLimpiar = new JButton("Limpiar");
        Estilos.botonPrincipal(botonIngresar);
        Estilos.botonSecundario(botonLimpiar);
        getRootPane().setDefaultButton(botonIngresar);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        botones.setBackground(Color.WHITE);
        botones.add(botonIngresar);
        botones.add(botonLimpiar);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setBackground(Color.WHITE);
        centro.add(formulario, BorderLayout.NORTH);
        centro.add(panelMostrar, BorderLayout.CENTER);
        centro.add(botones, BorderLayout.SOUTH);

        botonRegistrar = new JButton("Registrarse");
        Estilos.botonSecundario(botonRegistrar);

        botonRecuperar = new JButton("¿Olvidó su contraseña?");
        botonRecuperar.setBorderPainted(false);
        botonRecuperar.setContentAreaFilled(false);
        botonRecuperar.setForeground(Color.BLACK);
        botonRecuperar.setFocusPainted(false);
        botonRecuperar.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel registro = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        registro.setBackground(Color.WHITE);
        JLabel textoRegistro = new JLabel("¿No tiene cuenta?");
        textoRegistro.setForeground(Estilos.TEXTO);
        registro.add(textoRegistro);
        registro.add(botonRegistrar);

        JPanel inferior = new JPanel(new GridLayout(2, 1, 0, 6));
        inferior.setBackground(Color.WHITE);
        inferior.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        inferior.add(registro);
        inferior.add(botonRecuperar);

        contenido.add(centro, BorderLayout.CENTER);
        contenido.add(inferior, BorderLayout.SOUTH);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(contenido, BorderLayout.CENTER);
        fondo.add(tarjeta, BorderLayout.CENTER);
        add(fondo);
    }

    public String getCorreo() {
        return campoCorreo.getText().trim();
    }

    public String getContrasena() {
        return new String(campoContrasena.getPassword());
    }

    public JButton getBotonIngresar() {
        return botonIngresar;
    }

    public JButton getBotonRegistrar() {
        return botonRegistrar;
    }

    public JButton getBotonRecuperar() {
        return botonRecuperar;
    }

    public JButton getBotonLimpiar() {
        return botonLimpiar;
    }

    public JButton getBotonMostrarContrasena() {
        return botonMostrarContrasena;
    }

    public void alternarContrasena() {
        contrasenaVisible = !contrasenaVisible;
        campoContrasena.setEchoChar(contrasenaVisible ? (char) 0 : caracterOculto);
        botonMostrarContrasena.setText(contrasenaVisible ? "Ocultar contraseña" : "Mostrar contraseña");
    }

    public void setCorreo(String correo) {
        campoCorreo.setText(correo);
    }

    public void limpiarContrasena() {
        campoContrasena.setText("");
        if (contrasenaVisible) {
            alternarContrasena();
        }
    }

    public void limpiarCampos() {
        campoCorreo.setText("");
        campoContrasena.setText("");
        if (contrasenaVisible) {
            alternarContrasena();
        }
        campoCorreo.requestFocusInWindow();
    }
}
