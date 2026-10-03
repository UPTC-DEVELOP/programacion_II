package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import excepciones.ExcepcionValidacion;
import modelo.ItemCarrito;
import modelo.Libro;
import modelo.RepositorioCarrito;
import vista.VistaCarrito;

public class ControladorCarrito implements ActionListener {
    private VistaCarrito vista;
    private RepositorioCarrito repositorio;

    public ControladorCarrito(VistaCarrito vista, RepositorioCarrito repositorio,
            Iterable<Libro> librosDisponibles) {
        this.vista = vista;
        this.repositorio = repositorio;

        vista.cargarLibros(librosDisponibles);
        vista.getBotonAgregar().addActionListener(this);
        vista.getBotonActualizar().addActionListener(this);
        vista.getBotonEliminar().addActionListener(this);
        vista.getBotonLimpiar().addActionListener(this);
        vista.getBotonCerrar().addActionListener(this);

        vista.getTablaCarrito().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                vista.cargarCantidadSeleccionada();
            }
        });

        actualizarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        Object origen = evento.getSource();

        if (origen == vista.getBotonAgregar()) {
            agregar();
        } else if (origen == vista.getBotonActualizar()) {
            actualizar();
        } else if (origen == vista.getBotonEliminar()) {
            eliminar();
        } else if (origen == vista.getBotonLimpiar()) {
            vista.limpiarCampos();
        } else if (origen == vista.getBotonCerrar()) {
            vista.dispose();
        }
    }

    private void agregar() {
        try {
            Libro libro = vista.getLibroSeleccionado();
            int cantidad = leerCantidad();
            repositorio.agregar(libro, cantidad);
            actualizarTabla();
            vista.limpiarCampos();

            JOptionPane.showMessageDialog(
                    vista,
                    "Libro agregado al carrito.",
                    "Carrito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            mostrarAdvertencia("La cantidad debe ser un número entero.");
        } catch (ExcepcionValidacion e) {
            mostrarAdvertencia(e.getMessage());
        }
    }

    private void actualizar() {
        try {
            int fila = vista.getFilaSeleccionada();
            if (fila == -1) {
                throw new ExcepcionValidacion("Seleccione primero un elemento del carrito.");
            }

            int cantidad = leerCantidad();
            repositorio.actualizarCantidad(fila, cantidad);
            actualizarTabla();
            vista.limpiarCampos();

            JOptionPane.showMessageDialog(
                    vista,
                    "Cantidad actualizada correctamente.",
                    "Carrito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            mostrarAdvertencia("La cantidad debe ser un número entero.");
        } catch (ExcepcionValidacion e) {
            mostrarAdvertencia(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            int fila = vista.getFilaSeleccionada();
            if (fila == -1) {
                throw new ExcepcionValidacion("Seleccione primero el elemento que desea quitar.");
            }

            int opcion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Desea quitar el libro seleccionado del carrito?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                repositorio.eliminar(fila);
                actualizarTabla();
                vista.limpiarCampos();
            }
        } catch (ExcepcionValidacion e) {
            mostrarAdvertencia(e.getMessage());
        }
    }

    private int leerCantidad() throws ExcepcionValidacion, NumberFormatException {
        String texto = vista.getCantidadTexto();

        if (texto.isEmpty()) {
            throw new ExcepcionValidacion("Ingrese la cantidad.");
        }

        return Integer.parseInt(texto);
    }

    private void actualizarTabla() {
        vista.limpiarTabla();
        for (ItemCarrito item : repositorio.listar()) {
            vista.mostrarItem(item);
        }
        vista.mostrarTotal(repositorio.calcularTotal());
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                vista,
                mensaje,
                "Revise los datos",
                JOptionPane.WARNING_MESSAGE);
    }
}
