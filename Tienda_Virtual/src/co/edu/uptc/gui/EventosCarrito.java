package co.edu.uptc.gui;

import co.edu.uptc.model.Libro;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class EventosCarrito implements ActionListener {
    private VentanaCarrito ventana;

    public EventosCarrito(VentanaCarrito ventana) {
        this.ventana = ventana;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "AGREGAR_CARRITO":
                agregarAlCarrito();
                break;
            case "ACTUALIZAR_CARRITO":
                actualizarCantidad();
                break;
            case "ELIMINAR_CARRITO":
                eliminarDelCarrito();
                break;
            case "VACIAR_CARRITO":
                vaciarCarrito();
                break;
        }
    }

    private void agregarAlCarrito() {
        Libro libro = ventana.getPanelCarrito().getLibroSeleccionado();
        if (libro == null) {
            JOptionPane.showMessageDialog(ventana, "Seleccione un libro del catálogo.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int cantidad = ventana.getPanelCarrito().getCantidad();
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(ventana, "La cantidad debe ser mayor a cero.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean exito = ventana.getController().agregarAlCarrito(libro.getIsbn(), cantidad);
            if (exito) {
                JOptionPane.showMessageDialog(ventana, "Libro agregado al carrito.");
                ventana.refrescarCarrito();
                ventana.getPanelCarrito().limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(ventana, "No se pudo agregar: la cantidad (sumada a la que ya tiene en el carrito) supera el stock disponible (" + libro.getCantidadInventario() + ").", "Error de Stock", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ventana, "Ingrese una cantidad numérica válida.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarCantidad() {
        String isbn = ventana.getPanelCarrito().getIsbnItemSeleccionado();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Seleccione un libro de la tabla del carrito.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int cantidad = ventana.getPanelCarrito().getCantidad();
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(ventana, "La cantidad debe ser mayor a cero. Para quitar el libro use 'Eliminar'.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean exito = ventana.getController().actualizarCantidadCarrito(isbn, cantidad);
            if (exito) {
                JOptionPane.showMessageDialog(ventana, "Cantidad actualizada.");
                ventana.refrescarCarrito();
                ventana.getPanelCarrito().limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(ventana, "No se pudo actualizar: la cantidad supera el stock disponible.", "Error de Stock", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ventana, "Ingrese una cantidad numérica válida.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarDelCarrito() {
        String isbn = ventana.getPanelCarrito().getIsbnItemSeleccionado();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Seleccione un libro de la tabla del carrito.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean exito = ventana.getController().eliminarDelCarrito(isbn);
        if (exito) {
            JOptionPane.showMessageDialog(ventana, "Libro eliminado del carrito.");
            ventana.refrescarCarrito();
            ventana.getPanelCarrito().limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(ventana, "El libro no se encuentra en el carrito.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void vaciarCarrito() {
        if (ventana.getController().obtenerCarrito().getItems().isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "El carrito ya está vacío.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(ventana, "¿Desea vaciar todo el carrito?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            ventana.getController().vaciarCarrito();
            ventana.refrescarCarrito();
            ventana.getPanelCarrito().limpiarCampos();
        }
    }
}