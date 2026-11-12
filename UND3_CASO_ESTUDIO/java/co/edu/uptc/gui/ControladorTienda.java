package co.edu.uptc.gui;

import co.edu.uptc.negocio.Carrito;
import co.edu.uptc.negocio.CarritoService;
import co.edu.uptc.negocio.CatalogoService;
import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.Compra;
import co.edu.uptc.negocio.CompraService;
import co.edu.uptc.negocio.DetalleCompra;
import co.edu.uptc.negocio.MetodoPago;
import co.edu.uptc.negocio.PersistenciaException;
import co.edu.uptc.negocio.SesionService;
import co.edu.uptc.negocio.ValidacionException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class ControladorTienda implements ActionListener {

    private final VentanaPrincipal vista;
    private final VentanaTienda ventanaTienda;
    private final VentanaHistorialCompras ventanaHistorial;
    private final PanelTienda panelTienda;
    private final CatalogoService catalogoService;
    private final CarritoService carritoService;
    private final CompraService compraService;
    private final SesionService sesion;
    private Runnable alSolicitarClientes = () -> { };
    private Runnable alModificarInventario = () -> { };
    private Carrito carrito;

    public ControladorTienda(VentanaPrincipal vista, VentanaTienda ventanaTienda,
                             VentanaHistorialCompras ventanaHistorial, CatalogoService catalogoService,
                             CarritoService carritoService, CompraService compraService,
                             SesionService sesion) {
        this.vista = vista;
        this.ventanaTienda = ventanaTienda;
        this.ventanaHistorial = ventanaHistorial;
        this.panelTienda = ventanaTienda.getPanelTienda();
        this.catalogoService = catalogoService;
        this.carritoService = carritoService;
        this.compraService = compraService;
        this.sesion = sesion;
        panelTienda.agregarListener(this);
        actualizarSesion();
    }

    public void setAlSolicitarClientes(Runnable accion) {
        this.alSolicitarClientes = accion == null ? () -> { } : accion;
    }

    public void setAlModificarInventario(Runnable accion) {
        this.alModificarInventario = accion == null ? () -> { } : accion;
    }

    public void iniciar() {
        actualizarSesion();
        cargarCatalogo();
    }

    public void mostrarVentana() {
        actualizarSesion();
        cargarCatalogo();
        vista.mostrarVentanaInterna(ventanaTienda);
    }

    public void actualizarSesion() {
        Cliente cliente = sesion.getClienteAutenticado();
        panelTienda.actualizarEstadoSesion(cliente);
        if (cliente == null) {
            carrito = null;
        } else {
            carrito = carritoService.obtenerCarrito(cliente);
            try {
                carritoService.refrescarPrecios(carrito);
            } catch (ValidacionException ex) {
                // El carrito se conserva; el error se informa cuando el usuario intenta comprar.
            }
        }
        refrescarCarrito();
    }

    public void refrescarCatalogo() {
        cargarCatalogo();
        refrescarCarrito();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            switch (e.getActionCommand()) {
                case Comandos.MENU_TIENDA:
                    mostrarVentana();
                    break;
                case Comandos.MENU_GESTIONAR_CLIENTES:
                    alSolicitarClientes.run();
                    break;
                case Comandos.BUSCAR_TIENDA:
                    buscarCatalogo();
                    break;
                case Comandos.MOSTRAR_TODOS:
                    cargarCatalogo();
                    break;
                case Comandos.AGREGAR_CARRITO:
                    agregarAlCarrito();
                    break;
                case Comandos.AUMENTAR_CARRITO:
                    aumentarItem();
                    break;
                case Comandos.DISMINUIR_CARRITO:
                    disminuirItem();
                    break;
                case Comandos.ELIMINAR_CARRITO:
                    eliminarItem();
                    break;
                case Comandos.VACIAR_CARRITO:
                    vaciarCarrito();
                    break;
                case Comandos.FINALIZAR_COMPRA:
                    finalizarCompra();
                    break;
                case Comandos.HISTORIAL_COMPRAS:
                    mostrarHistorial();
                    break;
                default:
                    break;
            }
        } catch (ValidacionException ex) {
            UtilidadesGUI.mostrarAdvertencia(vista, ex.getMessage());
        } catch (PersistenciaException ex) {
            UtilidadesGUI.mostrarError(vista, ex.getMessage());
        }
    }

    private void cargarCatalogo() {
        panelTienda.mostrarCatalogo(catalogoService.listarLibros());
    }

    private void buscarCatalogo() throws ValidacionException {
        panelTienda.mostrarCatalogo(catalogoService.buscarLibros(panelTienda.getCriterioBusqueda()));
    }

    private void exigirSesion() throws ValidacionException {
        sesion.exigirAutenticacion();
        if (carrito == null) {
            carrito = carritoService.obtenerCarrito(sesion.getClienteAutenticado());
        }
    }

    private void agregarAlCarrito() throws ValidacionException {
        exigirSesion();
        String isbn = panelTienda.getIsbnCatalogoSeleccionado();
        if (isbn == null) {
            throw new ValidacionException("Seleccione un libro del catálogo.");
        }
        carritoService.agregar(carrito, isbn, panelTienda.getCantidadAgregar());
        refrescarCarrito();
        UtilidadesGUI.mostrarInformacion(vista, "Libro agregado al carrito.");
    }

    private void aumentarItem() throws ValidacionException {
        exigirSesion();
        String isbn = panelTienda.getIsbnCarritoSeleccionado();
        if (isbn == null) throw new ValidacionException("Seleccione un libro del carrito.");
        carritoService.aumentar(carrito, isbn);
        refrescarCarrito();
    }

    private void disminuirItem() throws ValidacionException {
        exigirSesion();
        String isbn = panelTienda.getIsbnCarritoSeleccionado();
        if (isbn == null) throw new ValidacionException("Seleccione un libro del carrito.");
        carritoService.disminuir(carrito, isbn);
        refrescarCarrito();
    }

    private void eliminarItem() throws ValidacionException {
        exigirSesion();
        String isbn = panelTienda.getIsbnCarritoSeleccionado();
        if (isbn == null) throw new ValidacionException("Seleccione un libro del carrito.");
        carritoService.eliminar(carrito, isbn);
        refrescarCarrito();
    }

    private void vaciarCarrito() throws ValidacionException {
        exigirSesion();
        if (carrito.getItems().isEmpty()) {
            return;
        }
        if (!UtilidadesGUI.confirmar(vista, "¿Desea eliminar todos los libros del carrito?")) {
            return;
        }
        carritoService.vaciar(carrito);
        refrescarCarrito();
    }

    private void finalizarCompra() throws ValidacionException {
        exigirSesion();
        carritoService.refrescarPrecios(carrito);
        refrescarCarrito();
        if (carrito.getItems().isEmpty()) {
            throw new ValidacionException("El carrito está vacío.");
        }
        Cliente cliente = sesion.getClienteAutenticado();
        String resumen = construirResumen(carrito, cliente);
        JTextArea area = new JTextArea(resumen, 18, 60);
        area.setEditable(false);
        int confirmar = JOptionPane.showConfirmDialog(vista, new JScrollPane(area),
                "Resumen del pedido", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (confirmar != JOptionPane.OK_OPTION) {
            return;
        }

        MetodoPago metodo = (MetodoPago) JOptionPane.showInputDialog(vista,
                "Seleccione el método de pago (simulado):", "Método de pago",
                JOptionPane.QUESTION_MESSAGE, null, MetodoPago.values(), MetodoPago.TARJETA);
        if (metodo == null) {
            return;
        }

        Compra compra = compraService.confirmarCompra(cliente, carrito, metodo);
        cargarCatalogo();
        refrescarCarrito();
        alModificarInventario.run();

        String recibo = construirResumenCompra(compra);
        JTextArea areaRecibo = new JTextArea(recibo, 20, 65);
        areaRecibo.setEditable(false);
        JOptionPane.showMessageDialog(vista, new JScrollPane(areaRecibo),
                "Compra confirmada - Recibo #" + compra.getId(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarHistorial() throws ValidacionException {
        exigirSesion();
        List<Compra> compras = compraService.listarPorCliente(sesion.getClienteAutenticado().getCorreoElectronico());
        ventanaHistorial.getPanelHistorial().mostrarCompras(compras);
        vista.mostrarVentanaInterna(ventanaHistorial);
    }

    private void refrescarCarrito() {
        if (carrito == null) {
            panelTienda.mostrarCarrito(java.util.Collections.emptyList());
            panelTienda.mostrarTotales(0, 0, 0, 0, 0);
            return;
        }
        double subtotal = carrito.getSubtotal();
        double impuestos = carrito.getImpuestos();
        double antes = carrito.getTotalAntesDescuentoPremium();
        double descuento = sesion.estaAutenticado() && sesion.getClienteAutenticado().esPremium()
                ? redondear(antes * co.edu.uptc.negocio.Cliente.DESCUENTO_PREMIUM / 100.0) : 0;
        double total = redondear(antes - descuento);
        panelTienda.mostrarCarrito(carrito.getItems());
        panelTienda.mostrarTotales(carrito.getCantidadTotal(), subtotal, impuestos, descuento, total);
    }

    private String construirResumen(Carrito carrito, Cliente cliente) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cliente: ").append(cliente.getNombreCompleto()).append("\n");
        sb.append("Tipo: ").append(cliente.getTipoCliente().getEtiqueta()).append("\n\n");
        for (co.edu.uptc.negocio.CarritoItem item : carrito.getItems()) {
            sb.append(item.getTitulo()).append(" | ")
                    .append(item.getCantidad()).append(" unidad(es) | ")
                    .append("Subtotal: ").append(UtilidadesGUI.formatearMoneda(item.getSubtotal()))
                    .append(" | IVA: ").append(UtilidadesGUI.formatearMoneda(item.getImpuestoTotal())).append("\n");
        }
        double subtotal = carrito.getSubtotal();
        double impuestos = carrito.getImpuestos();
        double antes = carrito.getTotalAntesDescuentoPremium();
        double descuento = cliente.esPremium()
                ? redondear(antes * co.edu.uptc.negocio.Cliente.DESCUENTO_PREMIUM / 100.0) : 0;
        sb.append("\nSubtotal: ").append(UtilidadesGUI.formatearMoneda(subtotal));
        sb.append("\nImpuestos: ").append(UtilidadesGUI.formatearMoneda(impuestos));
        sb.append("\nDescuento Premium: ").append(UtilidadesGUI.formatearMoneda(descuento));
        sb.append("\nTOTAL: ").append(UtilidadesGUI.formatearMoneda(redondear(antes - descuento)));
        return sb.toString();
    }

    private String construirResumenCompra(Compra compra) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tienda Virtual de Libros - UPTC\n");
        sb.append("Recibo #").append(compra.getId()).append("\n");
        sb.append("Cliente: ").append(compra.getNombreCliente()).append("\n");
        sb.append("Fecha: ").append(UtilidadesGUI.formatearFecha(compra.getFecha())).append("\n");
        sb.append("Método de pago: ").append(compra.getMetodoPago().getEtiqueta()).append("\n\n");
        for (DetalleCompra detalle : compra.getDetalles()) {
            sb.append(detalle.getTitulo()).append(" x ").append(detalle.getCantidad())
                    .append(" | Subtotal ").append(UtilidadesGUI.formatearMoneda(detalle.getSubtotal()))
                    .append(" | IVA ").append(UtilidadesGUI.formatearMoneda(detalle.getImpuesto()))
                    .append(" | Total ").append(UtilidadesGUI.formatearMoneda(detalle.getTotal())).append("\n");
        }
        sb.append("\nSubtotal: ").append(UtilidadesGUI.formatearMoneda(compra.getSubtotal()));
        sb.append("\nImpuestos: ").append(UtilidadesGUI.formatearMoneda(compra.getImpuestos()));
        sb.append("\nDescuento Premium: ").append(UtilidadesGUI.formatearMoneda(compra.getDescuentoPremium()));
        sb.append("\nTOTAL A PAGAR: ").append(UtilidadesGUI.formatearMoneda(compra.getTotal()));
        return sb.toString();
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
