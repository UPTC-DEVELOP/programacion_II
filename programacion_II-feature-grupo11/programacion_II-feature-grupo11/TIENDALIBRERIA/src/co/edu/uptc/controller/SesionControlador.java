package co.edu.uptc.controller;

import javax.swing.JOptionPane;

import co.edu.uptc.gui.VentanaPrincipal;
import co.edu.uptc.util.Comandos;

public class SesionControlador {

    private final VentanaPrincipal ventana;
    private final Runnable alIniciarSesion;

    public SesionControlador(VentanaPrincipal ventana, Evento evento, Runnable alIniciarSesion) {
        this.ventana = ventana;
        this.alIniciarSesion = alIniciarSesion;

        evento.registrar(Comandos.LOGIN, this::loguear);
        evento.registrar(Comandos.CANCELAR, this::cancelar);
    }

    public void loguear() {
        String usuario = ventana.getPanelLogin().getUsuario();
        String contrasenia = ventana.getPanelLogin().getContrasenia();

        // Validación básica (Idealmente delegada al negocio)
        if ("a".equals(usuario) && "1".equals(contrasenia)) {
            ventana.mostrarVistaCentral();
            alIniciarSesion.run();
        } else {
            JOptionPane.showMessageDialog(ventana, "Usuario o contraseña invalidos");
        }
    }

    private void cancelar() {
        JOptionPane.showMessageDialog(null, "Operación cancelada");
    }
}
