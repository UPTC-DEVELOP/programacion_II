package co.edu.uptc.tienda.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCarrito;

public class EventosCarrito implements ActionListener {

    private interface AccionCarrito {
        void ejecutar() throws CarritoExcepcion;
    }

    private final PanelCarrito panel;
    private final IGestionCarrito gestion;
    private final Map<String, AccionCarrito> acciones = new HashMap<>();

    public EventosCarrito(PanelCarrito panel, IGestionCarrito gestion) {
        this.panel = panel;
        this.gestion = gestion;
        acciones.put(PanelCarrito.ACCION_AGREGAR, new AccionCarrito() {
            @Override
            public void ejecutar() throws CarritoExcepcion {
                agregar();
            }
        });
        acciones.put(PanelCarrito.ACCION_CAMBIAR, new AccionCarrito() {
            @Override
            public void ejecutar() throws CarritoExcepcion {
                cambiarCantidad();
            }
        });
        acciones.put(PanelCarrito.ACCION_ELIMINAR, new AccionCarrito() {
            @Override
            public void ejecutar() throws CarritoExcepcion {
                eliminar();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AccionCarrito accion = acciones.get(e.getActionCommand());
        try {
            if (accion != null) {
                accion.ejecutar();
            }
            actualizarVista();
        } catch (CarritoExcepcion ex) {
            panel.mostrarAlerta("Carrito", ex.getMessage());
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
        if (panel.confirmar("Desea quitar el libro del carrito?")) {
            gestion.eliminarLibro(isbn);
        }
    }

    private void actualizarVista() {
        panel.mostrarCarrito(gestion.obtenerItems(), gestion.calcularSubtotal(),
                gestion.calcularImpuestos(), gestion.calcularTotal());
    }
}
