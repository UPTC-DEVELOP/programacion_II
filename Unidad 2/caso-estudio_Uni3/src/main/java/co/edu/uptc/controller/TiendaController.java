package co.edu.uptc.controller;

import co.edu.uptc.exception.TiendaException;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.Libro;
import co.edu.uptc.model.MetodoPago;
import co.edu.uptc.model.Pedido;
import co.edu.uptc.service.CarritoService;
import co.edu.uptc.service.CompraService;
import co.edu.uptc.view.Mensajes;
import co.edu.uptc.view.PanelTienda;
import co.edu.uptc.view.ReciboDialog;
import java.awt.event.*;

// TiendaController atiende la selección y las compras rápidas sin poner reglas en el panel.
public class TiendaController implements ActionListener, ItemListener {
    private final PanelTienda vista; private final CarritoService carritoService; private final CompraService compraService; private final Cliente cliente;
    public TiendaController(PanelTienda vista,CarritoService carritoService,CompraService compraService,Cliente cliente){this.vista=vista;this.carritoService=carritoService;this.compraService=compraService;this.cliente=cliente;vista.getBtnAgregar().addActionListener(this);vista.getBtnRegistrar().addActionListener(this);vista.getCmbLibros().addItemListener(this);}
    public void cargarLibros(java.util.List<Libro> libros){vista.getCmbLibros().removeAllItems();for(Libro l:libros)vista.getCmbLibros().addItem(l);if(vista.getCmbLibros().getItemCount()>0)vista.pintarLibro((Libro)vista.getCmbLibros().getItemAt(0));}
    @Override public void itemStateChanged(ItemEvent e){if(e.getStateChange()==ItemEvent.SELECTED)vista.pintarLibro((Libro)e.getItem());}
    @Override public void actionPerformed(ActionEvent e){try{Libro libro=(Libro)vista.getCmbLibros().getSelectedItem();if(libro==null)return;int cantidad=Integer.parseInt(vista.getTxtCantidad().getText());MetodoPago pago=MetodoPago.valueOf((String)vista.getCmbPago().getSelectedItem());if(Comandos.AGREGAR_CARRITO.equals(e.getActionCommand())){co.edu.uptc.model.Carrito carrito=carritoService.cargar(cliente.getCorreo());carritoService.agregar(carrito,libro.getIsbn(),cantidad);Mensajes.informacion(vista,"Libro agregado al carrito.");}else if(Comandos.REGISTRAR_PEDIDO.equals(e.getActionCommand())){Pedido pedido=compraService.registrarPedidoRapido(libro.getIsbn(),cantidad,pago);Mensajes.informacion(vista,"Pedido registrado #"+pedido.getId());}}catch(TiendaException|NumberFormatException ex){Mensajes.error(vista,ex.getMessage());}}
}
