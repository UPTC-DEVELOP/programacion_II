package co.edu.uptc.controller;

import co.edu.uptc.exception.TiendaException;
import co.edu.uptc.model.Pedido;
import co.edu.uptc.service.CompraService;
import co.edu.uptc.view.Mensajes;
import co.edu.uptc.view.PanelHistorial;
import java.awt.event.*;
import javax.swing.event.*;

// HistorialController sincroniza la tabla y la cancelación con CompraService.
public class HistorialController implements ListSelectionListener, ActionListener {
    private final PanelHistorial vista; private final CompraService compras; private final boolean historialGlobal;
    public HistorialController(PanelHistorial vista,CompraService compras){this(vista,compras,false);}
    public HistorialController(PanelHistorial vista,CompraService compras,boolean historialGlobal){this.vista=vista;this.compras=compras;this.historialGlobal=historialGlobal;vista.getBtnCancelar().addActionListener(this);vista.getTabla().getSelectionModel().addListSelectionListener(this);refrescar();}
    public void refrescar(){vista.pintar(historialGlobal ? compras.historialGlobal() : compras.historialCliente());}
    @Override public void valueChanged(ListSelectionEvent e){vista.getBtnCancelar().setEnabled(vista.getTabla().getSelectedRow()>=0);}
    @Override public void actionPerformed(ActionEvent e){try{int fila=vista.getTabla().getSelectedRow();if(fila<0)return;long id=((Number)vista.getTabla().getValueAt(fila,0)).longValue();if(Mensajes.confirmar(vista,"¿Desea cancelar el pedido seleccionado?")){compras.cancelarPedido(id);refrescar();}}catch(TiendaException ex){Mensajes.error(vista,ex.getMessage());}}
}
