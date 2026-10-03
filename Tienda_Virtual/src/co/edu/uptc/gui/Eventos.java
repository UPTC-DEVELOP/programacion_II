package co.edu.uptc.gui;

import co.edu.uptc.model.Libro;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Eventos implements ActionListener {
    private VentanaTienda ventana;

    public Eventos(VentanaTienda ventana) {
        this.ventana = ventana;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "LOGIN":
                ventana.mostrarPanelCatalogo();
                break;
            case "CANCELAR":
                ventana.accionCancelar();
                break;
            case "GUARDAR_LIBRO":
                guardarLibro();
                break;
            case "ACTUALIZAR_LIBRO":
                actualizarLibro();
                break;
            case "ELIMINAR_LIBRO":
                eliminarLibro();
                break;
            case "LIMPIAR_FORMULARIO":
                ventana.getPanelCatalogo().limpiarCampos();
                break;
        }
    }

    private void guardarLibro() {
        try {
            Libro nuevo = ventana.getPanelCatalogo().obtenerLibroDesdeFormulario();
            boolean exito = ventana.getController().registrarLibro(nuevo);
            if (exito) {
                JOptionPane.showMessageDialog(ventana, "Libro guardado exitosamente.");
                ventana.refrescarCatalogo();
                ventana.getPanelCatalogo().limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(ventana, "Error: El ISBN ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ventana, "Por favor ingrese valores numéricos válidos en los campos correspondientes.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarLibro() {
        try {
            Libro libro = ventana.getPanelCatalogo().obtenerLibroDesdeFormulario();
            boolean exito = ventana.getController().actualizarLibro(libro);
            if (exito) {
                JOptionPane.showMessageDialog(ventana, "Libro actualizado correctamente.");
                ventana.refrescarCatalogo();
            } else {
                JOptionPane.showMessageDialog(ventana, "No se encontró el libro con el ISBN especificado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ventana, "Verifique los formatos de entrada.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarLibro() {
        String isbn = ventana.getPanelCatalogo().getIsbnSeleccionado();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Ingrese un ISBN para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean exito = ventana.getController().eliminarLibro(isbn);
        if (exito) {
            JOptionPane.showMessageDialog(ventana, "Libro eliminado.");
            ventana.refrescarCatalogo();
            ventana.getPanelCatalogo().limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(ventana, "No existe un libro con ese ISBN.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}