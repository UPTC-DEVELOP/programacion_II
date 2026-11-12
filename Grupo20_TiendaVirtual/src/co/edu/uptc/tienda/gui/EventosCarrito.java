package co.edu.uptc.tienda.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCarrito;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Libro;

public class EventosCarrito implements ActionListener {

    public final static String AGREGAR = "Agregar";
    public final static String CAMBIAR = "Cambiar cantidad";
    public final static String ELIMINAR = "Eliminar";
    public final static String VACIAR = "Vaciar carrito";
    public final static String CLIENTE = "Cliente";

    private final PanelCarrito panel;
    private final IGestionCarrito gestion;

    public EventosCarrito(PanelCarrito panel, IGestionCarrito gestion) {
        this.panel = panel;
        this.gestion = gestion;
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
            avisos = gestion.sincronizarConCatalogo(catalogo);
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
        double porcentaje = cliente == null ? 0 : cliente.obtenerPorcentajeDescuento();
        double total = gestion.calcularTotal(porcentaje);
        panel.mostrarCarrito(gestion.obtenerItems(), gestion.calcularSubtotal(),
                gestion.calcularImpuestos(), gestion.calcularTotal() - total, porcentaje, total);
    }
}
