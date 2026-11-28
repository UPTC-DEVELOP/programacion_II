package co.edu.uptc.tienda.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCarrito;
import co.edu.uptc.tienda.interfaz.IGestionCompra;
import co.edu.uptc.tienda.interfaz.ISesionCliente;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Compra;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;

public class EventosCarrito implements ActionListener {

    public final static String AGREGAR = "Agregar";
    public final static String CAMBIAR = "Cambiar cantidad";
    public final static String ELIMINAR = "Eliminar";
    public final static String VACIAR = "Vaciar carrito";
    public final static String CLIENTE = "Cliente";
    public final static String FINALIZAR = "Finalizar compra";
    public final static String[] METODOS_PAGO = { "Tarjeta de crédito", "Tarjeta débito", "PSE",
            "Efectivo contra entrega" };

    private final PanelCarrito panel;
    private final IGestionCarrito gestion;
    private final IGestionCompra gestionCompra;
    private final ISesionCliente sesion;

    public EventosCarrito(PanelCarrito panel, IGestionCarrito gestion, IGestionCompra gestionCompra,
            ISesionCliente sesion) {
        this.panel = panel;
        this.gestion = gestion;
        this.gestionCompra = gestionCompra;
        this.sesion = sesion;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String evento = e.getActionCommand();
        String error = null;
        try {
            if (evento.equals(AGREGAR)) {
                agregar();
            } else if (evento.equals(CAMBIAR)) {
                cambiarCantidad();
            } else if (evento.equals(ELIMINAR)) {
                eliminar();
            } else if (evento.equals(VACIAR)) {
                vaciar();
            } else if (evento.equals(CLIENTE)) {
                asignarCliente();
            } else if (evento.equals(FINALIZAR)) {
                finalizarCompra();
            }
        } catch (CarritoExcepcion ex) {
            error = ex.getMessage();
        }
        actualizarVista();
        if (error != null) {
            panel.mostrarAlerta("Carrito", error);
        }
    }

    public void actualizarDatos(List<Libro> catalogo, List<Cliente> clientes) {
        List<String> avisos = new ArrayList<>();
        try {
            avisos = gestion.sincronizarConCatalogo(catalogo, clientes);
        } catch (CarritoExcepcion ex) {
            avisos.add(ex.getMessage());
        }
        panel.cargarCatalogo(catalogo);
        panel.cargarClientes(clientes, gestion.obtenerIdCliente());
        actualizarVista();
        if (!avisos.isEmpty()) {
            panel.mostrarAlerta("Carrito", String.join("\n", avisos));
        }
    }

    private void agregar() throws CarritoExcepcion {
        gestion.agregarLibro(panel.getLibroSeleccionado(), panel.getCantidad());
    }

    private void cambiarCantidad() throws CarritoExcepcion {
        String isbn = panel.getIsbnSeleccionado();
        if (isbn == null) {
            throw new CarritoExcepcion("Seleccione un libro de la tabla.");
        }
        gestion.cambiarCantidad(isbn, panel.getCantidad());
    }

    private void eliminar() throws CarritoExcepcion {
        String isbn = panel.getIsbnSeleccionado();
        if (isbn == null) {
            throw new CarritoExcepcion("Seleccione un libro de la tabla.");
        }
        if (panel.confirmar("¿Desea quitar el libro del carrito?")) {
            gestion.eliminarLibro(isbn);
        }
    }

    private void vaciar() throws CarritoExcepcion {
        if (gestion.obtenerItems().isEmpty()) {
            throw new CarritoExcepcion("El carrito ya está vacío.");
        }
        if (panel.confirmar("¿Desea vaciar el carrito?")) {
            gestion.vaciar();
        }
    }

    private void finalizarCompra() throws CarritoExcepcion {
        if (gestion.obtenerItems().isEmpty()) {
            throw new CarritoExcepcion("El carrito está vacío.");
        }
        Cliente cliente = panel.getClienteSeleccionado();
        if (cliente == null) {
            throw new CarritoExcepcion("Debe seleccionar un cliente para finalizar la compra.");
        }
        Cliente conSesion = sesion.getClienteLogueado();
        if (conSesion == null || !conSesion.getId().equals(cliente.getId())) {
            String correo = panel.pedirCorreo(cliente.getNombreCompleto());
            if (correo == null) {
                return;
            }
            if (!correo.trim().equalsIgnoreCase(cliente.getCorreoElectronico())
                    || !sesion.iniciarSesion(correo.trim())) {
                throw new CarritoExcepcion("El correo no corresponde a " + cliente.getNombreCompleto()
                        + ". No se inició sesión.");
            }
            conSesion = sesion.getClienteLogueado();
        }
        String metodoPago = panel.pedirMetodoPago(armarResumen(conSesion), METODOS_PAGO);
        if (metodoPago == null) {
            return;
        }
        Compra compra = gestion.finalizarCompra(conSesion, metodoPago);
        panel.mostrarRecibo(gestionCompra.generarRecibo(compra));
        if (gestion.obtenerAvisoCompra() != null) {
            panel.mostrarAlerta("Compra", gestion.obtenerAvisoCompra());
        }
    }

    private String armarResumen(Cliente cliente) {
        StringBuilder resumen = new StringBuilder();
        resumen.append("Cliente: ").append(cliente.getNombreCompleto()).append(" (").append(cliente.getTipoCliente())
                .append(")\n\n");
        for (ItemCarrito item : gestion.obtenerItems()) {
            resumen.append(item.getCantidad()).append(" x ").append(item.getLibro().getTitulo()).append("  ")
                    .append(formatear(item.obtenerSubtotalItem())).append("\n");
        }
        double premium = cliente.obtenerPorcentajeDescuento();
        double porcentaje = gestion.calcularPorcentajeDescuento(cliente);
        resumen.append("\nSubtotal: ").append(formatear(gestion.calcularSubtotal())).append("\n");
        resumen.append("Impuestos (IVA): ").append(formatear(gestion.calcularImpuestos())).append("\n");
        if (premium > 0) {
            resumen.append("Descuento Premium: ").append(String.format("%.0f", premium)).append(" %\n");
        }
        if (porcentaje - premium > 0) {
            resumen.append("Descuento por cliente frecuente: ").append(String.format("%.0f", porcentaje - premium))
                    .append(" %\n");
        }
        resumen.append("Descuento: -").append(formatear(gestion.calcularTotal() - gestion.calcularTotal(porcentaje)))
                .append("\n");
        resumen.append("Total a pagar: ").append(formatear(gestion.calcularTotal(porcentaje)));
        return resumen.toString();
    }

    private String formatear(double valor) {
        return String.format("$%,.2f", valor);
    }

    private void asignarCliente() throws CarritoExcepcion {
        Cliente cliente = panel.getClienteSeleccionado();
        if (cliente == null) {
            gestion.asignarCliente(null);
        } else {
            gestion.asignarCliente(cliente.getId());
        }
    }

    private void actualizarVista() {
        Cliente cliente = panel.getClienteSeleccionado();
        double porcentaje = gestion.calcularPorcentajeDescuento(cliente);
        double total = gestion.calcularTotal(porcentaje);
        panel.mostrarCarrito(gestion.obtenerItems(), gestion.calcularSubtotal(),
                gestion.calcularImpuestos(), gestion.calcularTotal() - total, porcentaje, total);
    }
}
