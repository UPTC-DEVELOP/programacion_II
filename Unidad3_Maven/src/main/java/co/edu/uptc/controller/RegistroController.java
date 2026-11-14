package co.edu.uptc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import co.edu.uptc.exception.TiendaException;
import co.edu.uptc.service.AutenticacionService;
import co.edu.uptc.view.Mensajes;
import co.edu.uptc.view.RegistroDialog;

// RegistroController recoge campos del diálogo y delega todas las reglas al servicio.
public class RegistroController implements ActionListener {
    private final RegistroDialog vista; private final AutenticacionService autenticacion;
    public RegistroController(RegistroDialog vista,AutenticacionService autenticacion){this.vista=vista;this.autenticacion=autenticacion;vista.getBtnRegistrar().addActionListener(this);vista.getBtnCancelar().addActionListener(e->vista.dispose());}
    @Override public void actionPerformed(ActionEvent evento){try{boolean premium="Premium".equals(vista.getCmbTipo().getSelectedItem());autenticacion.registrarCliente(vista.getTxtNombre().getText(),vista.getTxtCorreo().getText(),new String(vista.getTxtContrasena().getPassword()),vista.getTxtDireccion().getText(),vista.getTxtTelefono().getText(),premium);Mensajes.informacion(vista,"Cliente registrado correctamente.");vista.dispose();}catch(TiendaException e){Mensajes.error(vista,e.getMessage());}}
}
