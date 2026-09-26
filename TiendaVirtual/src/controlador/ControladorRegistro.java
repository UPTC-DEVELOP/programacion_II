package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import excepciones.ExcepcionValidacion;
import modelo.RepositorioUsuarios;
import modelo.Usuario;
import vista.VistaLogin;
import vista.VistaRegistro;

public class ControladorRegistro implements ActionListener {
    private VistaRegistro vista;
    private VistaLogin login;
    private RepositorioUsuarios repositorio;

    public ControladorRegistro(VistaRegistro vista, VistaLogin login,
            RepositorioUsuarios repositorio) {
        this.vista = vista;
        this.login = login;
        this.repositorio = repositorio;
        vista.getBotonRegistrar().addActionListener(this);
        vista.getBotonVolver().addActionListener(this);
        vista.getBotonLimpiar().addActionListener(this);
        vista.getCasillaMostrarContrasenas().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        Object origen = evento.getSource();

        if (origen == vista.getBotonRegistrar()) {
            registrar();
        } else if (origen == vista.getBotonVolver()) {
            vista.dispose();
        } else if (origen == vista.getBotonLimpiar()) {
            vista.limpiarCampos();
        } else if (origen == vista.getCasillaMostrarContrasenas()) {
            vista.mostrarContrasenas(vista.getCasillaMostrarContrasenas().isSelected());
        }
    }

    private void registrar() {
        try {
            if (!vista.getContrasena().equals(vista.getConfirmacion())) {
                throw new ExcepcionValidacion("Las contraseñas no coinciden.");
            }

            Usuario usuario = new Usuario(
                    vista.getNombre(),
                    vista.getCorreo(),
                    vista.getDireccion(),
                    vista.getTelefono(),
                    vista.getTipoCliente(),
                    vista.getContrasena());

            repositorio.registrar(usuario);
            login.setCorreo(usuario.getCorreo());

            JOptionPane.showMessageDialog(vista,
                    "Usuario registrado correctamente. Ya puede iniciar sesión.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            vista.dispose();
        } catch (ExcepcionValidacion e) {
            JOptionPane.showMessageDialog(vista, e.getMessage(),
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
        }
    }
}
