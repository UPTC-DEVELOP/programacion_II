package co.edu.uptc.controller;

import co.edu.uptc.exception.TiendaException;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.persistence.ResenaRepository;
import co.edu.uptc.service.ResenaService;
import co.edu.uptc.view.Mensajes;
import co.edu.uptc.view.PanelResenas;
import java.awt.event.*;

// ResenaController atiende el formulario y deja las reglas de compra previa al servicio.
public class ResenaController implements ActionListener {
    private final PanelResenas vista; private final ResenaService servicio; private final ResenaRepository repositorio; private final Cliente cliente;
    public ResenaController(PanelResenas vista,ResenaService servicio,ResenaRepository repositorio,Cliente cliente){this.vista=vista;this.servicio=servicio;this.repositorio=repositorio;this.cliente=cliente;vista.getBtnGuardar().addActionListener(this);refrescar();}
    public void refrescar(){vista.pintar(repositorio.listar());}
    @Override public void actionPerformed(ActionEvent e){try{servicio.registrar(vista.getTxtIsbn().getText(),cliente.getCorreo(),(Integer)vista.getCmbCalificacion().getSelectedItem(),vista.getTxtComentario().getText());vista.getTxtComentario().setText("");refrescar();Mensajes.informacion(vista,"Reseña guardada.");}catch(TiendaException ex){Mensajes.error(vista,ex.getMessage());}}
}
