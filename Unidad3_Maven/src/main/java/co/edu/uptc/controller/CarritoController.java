package co.edu.uptc.controller;

import co.edu.uptc.exception.TiendaException;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.MetodoPago;
import co.edu.uptc.model.Pedido;
import co.edu.uptc.service.CarritoService;
import co.edu.uptc.service.CompraService;
import co.edu.uptc.view.Mensajes;
import co.edu.uptc.view.PanelCarrito;
import co.edu.uptc.view.ReciboDialog;
import co.edu.uptc.view.ResumenPedidoDialog;
import java.awt.event.*;

// CarritoController conecta botones de la tabla con CarritoService y CompraService.
public class CarritoController implements ActionListener {
    private final PanelCarrito vista;private final CarritoService carritoService;private final CompraService compraService;private final Cliente cliente;private Carrito carrito;
    public CarritoController(PanelCarrito vista,CarritoService carritoService,CompraService compraService,Cliente cliente){this.vista=vista;this.carritoService=carritoService;this.compraService=compraService;this.cliente=cliente;vista.getBtnAumentar().addActionListener(this);vista.getBtnDisminuir().addActionListener(this);vista.getBtnQuitar().addActionListener(this);vista.getBtnFinalizar().addActionListener(this);refrescar();}
    private void refrescar(){carrito=carritoService.cargar(cliente.getCorreo());vista.pintar(carrito,0);}
    @Override public void actionPerformed(ActionEvent e){try{int fila=vista.getTabla().getSelectedRow();if((Comandos.AUMENTAR.equals(e.getActionCommand())||Comandos.DISMINUIR.equals(e.getActionCommand())||Comandos.QUITAR.equals(e.getActionCommand()))&&fila<0){Mensajes.advertencia(vista,"Seleccione un libro.");return;}if(Comandos.FINALIZAR_COMPRA.equals(e.getActionCommand())){ResumenPedidoDialog resumen=new ResumenPedidoDialog(null,carrito,0);resumen.setVisible(true);if(resumen.isConfirmado()){Pedido pedido=compraService.finalizarCompra(carrito,MetodoPago.EFECTIVO);new ReciboDialog(null,"Pedido #"+pedido.getId()+" registrado correctamente.").setVisible(true);refrescar();}return;}String isbn=(String)vista.getTabla().getValueAt(fila,0);if(Comandos.AUMENTAR.equals(e.getActionCommand()))carritoService.aumentar(carrito,isbn);else if(Comandos.DISMINUIR.equals(e.getActionCommand()))carritoService.disminuir(carrito,isbn);else carritoService.eliminar(carrito,isbn);refrescar();}catch(TiendaException ex){Mensajes.error(vista,ex.getMessage());}}
}
