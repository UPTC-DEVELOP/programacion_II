package view;

import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import model.CarritoCompras;
import model.Libro;
import model.MetodoPago;
import service.IServicioCarrito;
import service.IServicioCompras;
import service.ResultadoCompra;

public class EventosCompras extends Eventos {

    private final VentanaCompras vista;
    private final IServicioCompras compras;
    private final IServicioCarrito carritoService;

    public EventosCompras(VentanaCompras vista, IServicioCompras compras,
            IServicioCarrito carritoService) {
        super(vista);
        this.vista = vista;
        this.compras = compras;
        this.carritoService = carritoService;
    }

    public void registrarEventos() {
        vista.getBtnAgregar().addActionListener(e -> agregarAlCarrito());
        vista.getBtnModificar().addActionListener(e -> modificarCantidad());
        vista.getBtnEliminar().addActionListener(e -> eliminarDelCarrito());
        vista.getBtnVaciar().addActionListener(e -> vaciarCarrito());
        vista.getBtnComprar().addActionListener(e -> finalizarCompra());
        vista.getBtnBuscarLibro().addActionListener(e -> buscarLibros());
        vista.getTablaCarrito().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    private void buscarLibros() {
        String criterio = vista.getTxtBuscarLibro().getText().trim();
        List<Libro> libros = criterio.isEmpty()
                ? vista.getControladorLibro().listarUltimos(10)
                : vista.getControladorLibro().buscarPorCriterio(criterio);

        if (libros.isEmpty()) {
            mostrarInfo("No se encontraron libros.");
            return;
        }
        vista.cargarLibrosEnCombo(libros);
    }

    private void agregarAlCarrito() {
        Libro libro = (Libro) vista.getCmbLibro().getSelectedItem();
        int cantidad;
        try {
            cantidad = Integer.parseInt(vista.getTxtCantidad().getText());
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un numero entero.");
            return;
        }

        CarritoCompras carrito = vista.getCarrito();
        String error = carrito.validarAgregar(libro, cantidad);
        if (error != null) {
            mostrarError(error);
            return;
        }

        carrito.agregarLibro(libro, cantidad);
        actualizarTabla();
        persistirCarrito();
    }

    private void modificarCantidad() {
        int filaSeleccionada = vista.getTablaCarrito().getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione un libro de la tabla.");
            return;
        }

        Libro libro = (Libro) vista.getModeloTabla().getValueAt(filaSeleccionada, 0);
        int cantidad;
        try {
            cantidad = Integer.parseInt(vista.getTxtCantidad().getText());
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un numero entero.");
            return;
        }

        String error = vista.getCarrito().validarModificar(libro, cantidad);
        if (error != null) {
            mostrarError(error);
            return;
        }

        vista.getCarrito().modificarCantidad(libro, cantidad);
        actualizarTabla();
        persistirCarrito();
    }

    private void eliminarDelCarrito() {
        int filaSeleccionada = vista.getTablaCarrito().getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione un libro de la tabla.");
            return;
        }

        Libro libro = (Libro) vista.getModeloTabla().getValueAt(filaSeleccionada, 0);
        vista.getCarrito().eliminarLibro(libro);
        actualizarTabla();
        persistirCarrito();
    }

    private void vaciarCarrito() {
        vista.getCarrito().vaciar();
        actualizarTabla();
        persistirCarrito();
    }

    private void cargarSeleccion() {
        int filaSeleccionada = vista.getTablaCarrito().getSelectedRow();
        if (filaSeleccionada < 0) {
            return;
        }

        Libro libro = (Libro) vista.getModeloTabla().getValueAt(filaSeleccionada, 0);
        int cantidad = (Integer) vista.getModeloTabla().getValueAt(filaSeleccionada, 1);
        vista.getCmbLibro().setSelectedItem(libro);
        vista.getTxtCantidad().setText(String.valueOf(cantidad));
    }

    private void actualizarTabla() {
        DefaultTableModel modeloTabla = vista.getModeloTabla();
        modeloTabla.setRowCount(0);

        for (Map.Entry<Libro, Integer> entrada : vista.getCarrito().getItems().entrySet()) {
            Libro libro = entrada.getKey();
            int cantidad = entrada.getValue();
            double subtotal = libro.getPrecio() * cantidad;
            modeloTabla.addRow(new Object[] {
                    libro,
                    cantidad,
                    libro.getPrecio(),
                    subtotal
            });
        }

        vista.getLblSubtotal().setText(
                String.format("$%,.2f", vista.getCarrito().calcularSubtotal()));
        vista.getLblIva19().setText(
                String.format("$%,.2f", vista.getCarrito().calcularIVA19()));
        vista.getLblIva5().setText(
                String.format("$%,.2f", vista.getCarrito().calcularIVA5()));
        vista.getLblOtrosIva().setText(
                String.format("$%,.2f", vista.getCarrito().calcularOtrosIva()));

        double descuento = vista.getCliente() != null
                ? vista.getCliente().calcularDescuento() : 0.0;
        double valorDescuento = vista.getCarrito().calcularSubtotal() * descuento;
        vista.getLblDescuentoPremium().setText(
                String.format("$%,.2f", valorDescuento));
        vista.getLblTotal().setText(
                String.format("$%,.2f", vista.getCarrito().calcularTotal(descuento)));
    }

    private void finalizarCompra() {
        MetodoPago metodoPago = (MetodoPago) vista.getCmbMetodoPago().getSelectedItem();
        String nombreMetodo = metodoPago != null ? metodoPago.getNombre() : null;

        if (!confirmar(construirResumen(nombreMetodo))) {
            return;
        }

        ResultadoCompra resultado = compras.finalizarCompra(
                vista.getCliente(), vista.getCarrito(), nombreMetodo);

        if (resultado.isExitoso()) {
            mostrarInfo(resultado.getMensaje());
            if (carritoService != null && vista.getCliente() != null) {
                carritoService.vaciar(vista.getCliente().getCorreo());
            }
            if (resultado.getCompra() != null) {
                new VentanaRecibo(vista.getCliente(), resultado.getCompra(), vista)
                        .setVisible(true);
            }
        } else {
            mostrarError(resultado.getMensaje());
        }

        actualizarTabla();
    }

    private String construirResumen(String metodoPago) {
        CarritoCompras carrito = vista.getCarrito();
        double subtotal = carrito.calcularSubtotal();
        double iva = carrito.calcularIVA();
        double descuento = vista.getCliente() != null
                ? vista.getCliente().calcularDescuento() : 0.0;
        double total = carrito.calcularTotal(descuento);

        return "Resumen de compra\n\n"
                + "Cliente: " + (vista.getCliente() != null
                        ? vista.getCliente().getNombreCompleto() : "")
                + "\nProductos: " + carrito.getItems().size()
                + "\nMetodo de pago: " + (metodoPago != null ? metodoPago : "No seleccionado")
                + "\n\nSubtotal: " + String.format("$%,.2f", subtotal)
                + "\nIVA: " + String.format("$%,.2f", iva)
                + "\nDescuento: " + String.format("$%,.2f", subtotal * descuento)
                + "\nTOTAL: " + String.format("$%,.2f", total)
                + "\n\n¿Desea confirmar la compra?";
    }

    private void persistirCarrito() {
        if (carritoService != null && vista.getCliente() != null) {
            carritoService.guardar(vista.getCarrito(), vista.getCliente().getCorreo());
        }
    }
}
