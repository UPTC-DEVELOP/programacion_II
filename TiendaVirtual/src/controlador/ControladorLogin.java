package controlador;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import excepciones.ExcepcionValidacion;
import modelo.RepositorioLibros;
import modelo.RepositorioUsuarios;
import modelo.Usuario;
import vista.VistaInicio;
import vista.VistaLogin;
import vista.VistaRegistro;

public class ControladorLogin implements ActionListener {
    private VistaLogin vista;
    private RepositorioUsuarios repositorioUsuarios;
    private RepositorioLibros repositorioLibros;

    public ControladorLogin(VistaLogin vista, RepositorioUsuarios repositorioUsuarios,
            RepositorioLibros repositorioLibros) {
        this.vista = vista;
        this.repositorioUsuarios = repositorioUsuarios;
        this.repositorioLibros = repositorioLibros;
        vista.getBotonIngresar().addActionListener(this);
        vista.getBotonRegistrar().addActionListener(this);
        vista.getBotonRecuperar().addActionListener(this);
        vista.getBotonLimpiar().addActionListener(this);
        vista.getBotonMostrarContrasena().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        Object origen = evento.getSource();

        if (origen == vista.getBotonIngresar()) {
            iniciarSesion();
        } else if (origen == vista.getBotonRegistrar()) {
            abrirRegistro();
        } else if (origen == vista.getBotonRecuperar()) {
            recuperarContrasena();
        } else if (origen == vista.getBotonLimpiar()) {
            vista.limpiarCampos();
        } else if (origen == vista.getBotonMostrarContrasena()) {
            vista.alternarContrasena();
        }
    }

    private void iniciarSesion() {
        try {
            Usuario usuario = repositorioUsuarios.autenticar(vista.getCorreo(), vista.getContrasena());
            VistaInicio inicio = new VistaInicio(usuario);
            new ControladorInicio(inicio, vista, repositorioLibros);
            vista.setVisible(false);
            inicio.setVisible(true);
        } catch (ExcepcionValidacion e) {
            vista.limpiarContrasena();
            JOptionPane.showMessageDialog(vista, e.getMessage(),
                    "Datos de acceso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirRegistro() {
        VistaRegistro registro = new VistaRegistro();
        new ControladorRegistro(registro, vista, repositorioUsuarios);
        registro.setVisible(true);
    }

    private void recuperarContrasena() {
        JTextField campoCorreo = new JTextField(vista.getCorreo(), 20);
        JPasswordField campoNuevaContrasena = new JPasswordField(20);
        JPasswordField campoConfirmacion = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Correo:"));
        panel.add(campoCorreo);
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(campoNuevaContrasena);
        panel.add(new JLabel("Confirmar contraseña:"));
        panel.add(campoConfirmacion);

        int opcion = JOptionPane.showConfirmDialog(vista, panel,
                "Recuperar contraseña", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        String nuevaContrasena = new String(campoNuevaContrasena.getPassword());
        String confirmacion = new String(campoConfirmacion.getPassword());

        if (!nuevaContrasena.equals(confirmacion)) {
            JOptionPane.showMessageDialog(vista, "Las contraseñas no coinciden.",
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            repositorioUsuarios.cambiarContrasena(campoCorreo.getText().trim(), nuevaContrasena);
            vista.setCorreo(campoCorreo.getText().trim());
            vista.limpiarContrasena();
            JOptionPane.showMessageDialog(vista,
                    "La contraseña fue actualizada. Ya puede iniciar sesión.",
                    "Contraseña actualizada", JOptionPane.INFORMATION_MESSAGE);
        } catch (ExcepcionValidacion e) {
            JOptionPane.showMessageDialog(vista, e.getMessage(),
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
        }
    }
}
