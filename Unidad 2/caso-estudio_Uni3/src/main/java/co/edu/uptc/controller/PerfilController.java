package co.edu.uptc.controller;

import co.edu.uptc.exception.TiendaException;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.service.AutenticacionService;
import co.edu.uptc.view.Mensajes;
import co.edu.uptc.view.PanelPerfil;
import java.awt.event.*;

// PerfilController envía los campos editables al servicio sin permitir cambiar correo ni tipo.
public class PerfilController implements ActionListener {
    private final PanelPerfil vista; private final AutenticacionService autenticacion; private final Cliente cliente;
    public PerfilController(PanelPerfil vista,AutenticacionService autenticacion,Cliente cliente){this.vista=vista;this.autenticacion=autenticacion;this.cliente=cliente;vista.getBtnGuardar().addActionListener(this);vista.getLblCorreo().setText(cliente.getCorreo());vista.getLblTipo().setText(cliente.getClass().getSimpleName());vista.getTxtNombre().setText(cliente.getNombreCompleto());vista.getTxtDireccion().setText(cliente.getDireccionEnvio());vista.getTxtTelefono().setText(cliente.getTelefono());}
    @Override public void actionPerformed(ActionEvent e){try{autenticacion.actualizarCliente(cliente,vista.getTxtNombre().getText(),vista.getTxtDireccion().getText(),vista.getTxtTelefono().getText(),new String(vista.getTxtContrasena().getPassword()));Mensajes.informacion(vista,"Datos actualizados.");}catch(TiendaException ex){Mensajes.error(vista,ex.getMessage());}}
}
