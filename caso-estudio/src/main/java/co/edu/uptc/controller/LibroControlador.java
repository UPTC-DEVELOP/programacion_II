package co.edu.uptc.controller;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import co.edu.uptc.exceptions.LibreriaException;
import co.edu.uptc.gui.DialogoCentralLibro;
import co.edu.uptc.gui.DialogoCrearLibro;
import co.edu.uptc.gui.DialogoEditarLibro;
import co.edu.uptc.gui.PanelPadreLibro;
import co.edu.uptc.interfaces.ILibreria;
import co.edu.uptc.model.Libro;
import co.edu.uptc.util.Comandos;

public class LibroControlador {

    private final ILibreria libreria;
    private final PanelPadreLibro vista;
    private final Component padre;
    private final ActionListener evento;
    private DialogoCentralLibro dialogoLibro;

    public LibroControlador(ILibreria libreria, PanelPadreLibro vista, Component padre, Evento evento) {
        this.libreria = libreria;
        this.vista = vista;
        this.padre = padre;
        this.evento = evento;

        evento.registrar(Comandos.CREAR_LIBRO, this::lanzarDialogoLibro);
        evento.registrar(Comandos.CANCELAR_CREACION_LIBRO, this::cerrarDialogoLibro);
        evento.registrar(Comandos.CANCELAR_LIBRO, this::cerrarDialogoLibro);
        evento.registrar(Comandos.GUARDAR_LIBRO, this::crearLibro);
        evento.registrar(Comandos.ELIMINAR_LIBRO, this::eliminarLibro);
        evento.registrar(Comandos.ACTUALIZAR_LIBRO, this::actualizarLibro);
        evento.registrar(Comandos.VER_LIBRO, this::verLibro);
        evento.registrar(Comandos.BUSCAR_LIBRO, this::buscarLibro);
        evento.registrar(Comandos.LIMPIAR_LIBRO, this::limpiarTablaLibros);
        evento.registrar(Comandos.GUARDAR_ACTUALIZACION, this::guardarActualizacionLibro);
    }

    public void lanzarDialogoLibro() {
        dialogoLibro = new DialogoCrearLibro(evento, "Crear Libro", true);
        dialogoLibro.setVisible(true);
    }

    public void cerrarDialogoLibro() {
        if (dialogoLibro != null) {
            dialogoLibro.dispose();
            dialogoLibro = null;
        }
    }

    public void crearLibro() {
        if (dialogoLibro != null) {
            try {
                libreria.agregarLibro(dialogoLibro.capturarDatos());
                cerrarDialogoLibro();
                refrescarTabla();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(padre, "Error al crear el libro: Verifique los datos numéricos.");
            } catch (IllegalArgumentException | LibreriaException e) {
                JOptionPane.showMessageDialog(padre, "Error al crear el libro: " + e.getMessage());
            }
        }
    }

    public void eliminarLibro() {
        String isbn = vista.getLibroSeleccionadoIsbn();
        if (isbn != null) {
            libreria.eliminarLibro(isbn);
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(padre, "Seleccione un libro para eliminar");
        }
    }

    public void actualizarLibro() {
        String isbn = JOptionPane.showInputDialog(padre, "Ingrese el ISBN del libro que desea actualizar:");

        if (isbn != null && !isbn.trim().isEmpty()) {
            Libro libroEncontrado = libreria.buscarLibro(isbn.trim());

            if (libroEncontrado != null) {
                DialogoEditarLibro editarDialogo = new DialogoEditarLibro(evento, "Actualizar Libro", false);
                editarDialogo.cargarDatosLibro(libroEncontrado);

                dialogoLibro = editarDialogo;
                dialogoLibro.setVisible(true); // Se abre el dialogo de forma modal
            } else {
                JOptionPane.showMessageDialog(padre, "No se encontró ningun libro con el ISBN: " + isbn);
            }
        }
    }

    // Se ejecuta al hacer clic en el boton "Actualizar" de adentro del diálogo
    public void guardarActualizacionLibro() {
        if (dialogoLibro != null) {
            try {
                Libro libroActualizado = dialogoLibro.capturarDatos();

                // Realiza la actualizacion en la capa de negocio usando el ISBN
                libreria.actualizarLibro(libroActualizado.getIsbn(), libroActualizado);

                cerrarDialogoLibro();
                refrescarTabla();
                JOptionPane.showMessageDialog(padre, "¡Libro actualizado exitosamente!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(padre, "Error al actualizar: Verifique que los campos numéricos sean correctos.");
            } catch (IllegalArgumentException | LibreriaException e) {
                JOptionPane.showMessageDialog(padre, "Error al actualizar: " + e.getMessage());
            }
        }
    }

    public void verLibro() {
        String isbn = vista.getLibroSeleccionadoIsbn();
        if (isbn != null) {
            Libro libro = libreria.buscarLibro(isbn);
            if (libro != null) {
                JOptionPane.showMessageDialog(padre, libro.toString());
            }
        } else {
            JOptionPane.showMessageDialog(padre, "Seleccione un libro para ver");
        }
    }

    public void buscarLibro() {
        String isbn = JOptionPane.showInputDialog(padre, "Ingrese ISBN a buscar:");
        if (isbn != null && !isbn.trim().isEmpty()) {
            Libro libro = libreria.buscarLibro(isbn.trim());
            if (libro != null) {
                JOptionPane.showMessageDialog(padre, libro.toString());
            } else {
                JOptionPane.showMessageDialog(padre, "Libro no encontrado.");
            }
        }
    }

    public void limpiarTablaLibros() {
        vista.getModelo().setRowCount(0);
    }

    public void refrescarTabla() {
        vista.poblarTabla(libreria.getListaLibros());
    }
}
