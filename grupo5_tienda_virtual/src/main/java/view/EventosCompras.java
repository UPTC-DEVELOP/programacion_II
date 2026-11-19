package view;

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
        vista.getTablaCarrito().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    private void agregarAlCarrito() {
        Libro libro = (Libro) vista.getCmbLibro().getSelectedItem();
        if (libro == null) {
            mostrarError("Seleccione un libro.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(vista.getTxtCantidad().getText());
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un numero entero.");
            return;
        }

        if (cantidad <= 0) {
            mostrarError("La cantidad debe ser mayor que cero.");
            return;
        }

        CarritoCompras carrito = vista.getCarrito();
        Integer cantidadActual = carrito.getItems().get(libro);
        if (cantidadActual == null) {
            cantidadActual = 0;
        }

        int cantidadSolicitada = cantidadActual + cantidad;
        if (cantidadSolicitada > libro.getCantidadDisponible()) {
            int disponible = libro.getCantidadDisponible() - cantidadActual;
            mostrarError("No hay suficiente stock.\n"
                    + "Unidades disponibles para agregar: " + disponible);
            return;
        }

        carrito.agregarLibro(libro, cantidad);
        vista.actualizarTabla();
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

        if (cantidad < 0) {
            mostrarError("La cantidad no puede ser negativa.");
            return;
        }
        if (cantidad > libro.getCantidadDisponible()) {
            mostrarError("No hay suficiente stock.\n"
                    + "Stock disponible: " + libro.getCantidadDisponible());
            return;
        }

        vista.getCarrito().modificarCantidad(libro, cantidad);
        vista.actualizarTabla();
    }

    private void eliminarDelCarrito() {
        int filaSeleccionada = vista.getTablaCarrito().getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione un libro de la tabla.");
            return;
        }

        Libro libro = (Libro) vista.getModeloTabla().getValueAt(filaSeleccionada, 0);
        vista.getCarrito().eliminarLibro(libro);
        vista.actualizarTabla();
    }

    private void vaciarCarrito() {
        vista.getCarrito().vaciar();
        vista.actualizarTabla();
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

    private void finalizarCompra() {
        MetodoPago metodoPago = (MetodoPago) vista.getCmbMetodoPago().getSelectedItem();
        String nombreMetodo = metodoPago != null ? metodoPago.getNombre() : null;

        ResultadoCompra resultado = compras.finalizarCompra(
                vista.getCliente(), vista.getCarrito(), nombreMetodo);

        if (resultado.isExitoso()) {
            mostrarInfo(resultado.getMensaje());
        } else {
            mostrarError(resultado.getMensaje());
        }

        vista.actualizarTabla();
    }
}
