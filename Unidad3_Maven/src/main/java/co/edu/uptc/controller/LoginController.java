package co.edu.uptc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import co.edu.uptc.exception.TiendaException;
import co.edu.uptc.model.Usuario;
import co.edu.uptc.service.AutenticacionService;
import co.edu.uptc.service.Sesion;
import co.edu.uptc.view.LoginFrame;
import co.edu.uptc.view.Mensajes;
import co.edu.uptc.view.RegistroDialog;

// LoginController traduce los botones de acceso a llamadas de AutenticacionService.
public class LoginController implements ActionListener {
    private final LoginFrame vista; private final AutenticacionService autenticacion; private final Sesion sesion; private final Runnable alIniciar;
    public LoginController(LoginFrame vista, AutenticacionService autenticacion, Sesion sesion, Runnable alIniciar){this.vista=vista;this.autenticacion=autenticacion;this.sesion=sesion;this.alIniciar=alIniciar;vista.getBtnIniciar().addActionListener(this);vista.getBtnRegistro().addActionListener(this);}
    @Override public void actionPerformed(ActionEvent evento){try{if(Comandos.INICIAR_SESION.equals(evento.getActionCommand())) {
		iniciar();
	} else if(Comandos.ABRIR_REGISTRO.equals(evento.getActionCommand())) {
		registrar();
	}}catch(TiendaException e){Mensajes.error(vista,e.getMessage());}catch(Exception e){Mensajes.error(vista,"Ocurrió un error inesperado.");}}
    private void iniciar(){Usuario usuario=autenticacion.iniciarSesion(vista.getTxtCorreo().getText(),new String(vista.getTxtContrasena().getPassword()));sesion.iniciar(usuario);vista.setVisible(false);alIniciar.run();}
    private void registrar(){RegistroDialog dialogo=new RegistroDialog(vista);new RegistroController(dialogo,autenticacion);dialogo.setVisible(true);}
}
