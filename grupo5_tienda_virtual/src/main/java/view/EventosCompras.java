package view;

import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import model.CarritoCompras;
import model.Libro;
import model.MetodoPago;
import service.IServicioCompras;
import service.ResultadoCompra;

public class EventosCompras extends Eventos {

    private final VentanaCompras vista;
    private final IServicioCompras compras;

    public EventosCompras(VentanaCompras vista, IServicioCompras compras) {
        super(vista);
        this.vista = vista;
        this.compras = compras;
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
    }

    private void vaciarCarrito() {
        vista.getCarrito().vaciar();
        actualizarTabla();
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

        ResultadoCompra resultado = compras.finalizarCompra(
                vista.getCliente(), vista.getCarrito(), nombreMetodo);

        if (resultado.isExitoso()) {
            mostrarInfo(resultado.getMensaje());
            if (resultado.getCompra() != null) {
                new VentanaRecibo(vista.getCliente(), resultado.getCompra(), vista)
                        .setVisible(true);
            }
        } else {
            mostrarError(resultado.getMensaje());
        }

        actualizarTabla();
    }
}
