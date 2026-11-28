package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;
import co.edu.uptc.negocio.Pedido;
import co.edu.uptc.negocio.PedidoService;
import co.edu.uptc.negocio.PersistenciaException;
import co.edu.uptc.negocio.ValidacionException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorPedido implements ActionListener {

    private final VentanaPrincipal vista;
    private final VentanaPedido ventanaPedido;
    private final PanelProducto panelProducto;
    private final PanelHistorial panelHistorial;
    private final PedidoService servicio;

    private Runnable alModificarInventario = () -> { };

    public ControladorPedido(VentanaPrincipal vista, VentanaPedido ventanaPedido,
                             PedidoService servicio) {
        this.vista = vista;
        this.ventanaPedido = ventanaPedido;
        this.panelProducto = ventanaPedido.getPanelProducto();
        this.panelHistorial = ventanaPedido.getPanelHistorial();
        this.servicio = servicio;
        registrarEventos();
    }

    private void registrarEventos() {
        vista.agregarListenerMenu(this);          
        panelProducto.agregarListener(this);      
        panelHistorial.agregarListener(this);    
    }

    public void setAlModificarInventario(Runnable accion) {
        this.alModificarInventario = accion;
    }

    public void iniciar() {
        recargarProductos();
        panelHistorial.mostrarPedidos(servicio.listarHistorial());
    }

    public void recargarProductos() {
        panelProducto.cargarProductos(servicio.listarProductos());
        actualizarVistaProducto();
    }

    // ===================== Manejo de eventos =====================

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Comandos.SELECCION_PRODUCTO:
                
                actualizarVistaProducto();
                break;
            case Comandos.REGISTRAR_PEDIDO:
                registrarPedido();
                break;
            case Comandos.CANCELAR_PEDIDO:
                cancelarPedido();
                break;
            case Comandos.MENU_GESTIONAR_PEDIDOS:
                recargarProductos();
                vista.mostrarVentanaInterna(ventanaPedido);
                break;
            default:
                break; 
        }
    }

    // ===================== Casos de uso =====================

    private void actualizarVistaProducto() {
        panelProducto.mostrarProducto(panelProducto.getProductoSeleccionado());
    }

    private void registrarPedido() {
        Libro producto = panelProducto.getProductoSeleccionado();
        if (producto == null) {
            UtilidadesGUI.mostrarAdvertencia(vista, "Seleccione un producto para registrar el pedido.");
            return;
        }
        try {
            
            int cantidad = Integer.parseInt(panelProducto.getCantidad());

       
            Pedido pedido = servicio.registrarPedido(producto.getIsbn(), cantidad);

            panelHistorial.agregarPedido(pedido);       // nueva fila en el historial
            panelProducto.mostrarTotal(pedido.getTotal());
            actualizarVistaProducto();                   // el stock mostrado ya está descontado
            panelProducto.limpiarCantidad();
            alModificarInventario.run();
            UtilidadesGUI.mostrarInformacion(vista, "Pedido #" + pedido.getId()
                    + " registrado.\nTotal a pagar: " + UtilidadesGUI.formatearMoneda(pedido.getTotal()));

        } catch (NumberFormatException ex) {
            
            UtilidadesGUI.mostrarAdvertencia(vista, "La cantidad debe ser un número entero válido.");
        } catch (ValidacionException ex) {
            
            UtilidadesGUI.mostrarAdvertencia(vista, ex.getMessage());
        } catch (PersistenciaException ex) {
            UtilidadesGUI.mostrarError(vista, ex.getMessage());
        }
    }

    private void cancelarPedido() {
        
        long idPedido = panelHistorial.getIdPedidoSeleccionado();
        if (idPedido < 0) {
            UtilidadesGUI.mostrarAdvertencia(vista,
                    "Seleccione en la tabla el pedido que desea cancelar.");
            return;
        }
        try {
            
            Pedido pedido = servicio.cancelarPedido(idPedido);

            panelHistorial.eliminarPedido(idPedido);    
            actualizarVistaProducto();                  
            alModificarInventario.run();
            UtilidadesGUI.mostrarInformacion(vista, "Pedido #" + idPedido + " cancelado.\n"
                    + pedido.getCantidad() + " unidad(es) regresaron al stock.");

        } catch (ValidacionException ex) {
            UtilidadesGUI.mostrarAdvertencia(vista, ex.getMessage());
        } catch (PersistenciaException ex) {
            UtilidadesGUI.mostrarError(vista, ex.getMessage());
        }
    }
}
