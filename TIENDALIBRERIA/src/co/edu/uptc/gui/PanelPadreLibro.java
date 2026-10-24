package co.edu.uptc.gui;

import java.awt.event.ActionListener;

import co.edu.uptc.util.Comandos;

import java.util.List;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.model.Libro;


public class PanelPadreLibro extends PanelCentral<Libro> {

    public PanelPadreLibro(ActionListener evento) {
        super(evento);
    }

    @Override
    public void agregarTituloPanel() {
        tituloPanel = "Gestión de Libros";
    }

    @Override
    public void agregarIdentificadorComandoBoton() {
        btnEliminar.setActionCommand(Comandos.ELIMINAR_LIBRO);
        btnVer.setActionCommand(Comandos.VER_LIBRO);
        btnActualizar.setActionCommand(Comandos.ACTUALIZAR_LIBRO);
        btnCrear.setActionCommand(Comandos.CREAR_LIBRO);
        btnLimpiar.setActionCommand(Comandos.LIMPIAR_LIBRO);
        btnBuscar.setActionCommand(Comandos.BUSCAR_LIBRO);
    }

    @Override
    public void agregarCabeceraTabla() {
        modelo.addColumn("ISBN");
        modelo.addColumn("Título");
        modelo.addColumn("Autor");
        modelo.addColumn("Año");
        modelo.addColumn("Categoría");
        modelo.addColumn("Editorial");
        modelo.addColumn("Páginas");
        modelo.addColumn("Precio");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Formato");
        tabla.setModel(modelo);
    }

    @Override
    public void poblarTabla(List<Libro> libros) {
        modelo.setRowCount(0);
        for (Libro l : libros) {
            Object[] fila = {
                l.getIsbn(),
                l.getTituloLibro(),
                l.getAutor(),
                l.getFechaPublicacion(),
                l.getCategoria(),
                l.getEditorial(),
                l.getPaginas(),
                l.getPrecioVenta(),
                l.getCantidadDisponible(),
                l.getFormato()
            };
            modelo.addRow(fila);
        }
    }

    // Método para obtener ISBN del libro seleccionado
    public String getLibroSeleccionadoIsbn() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            return tabla.getModel().getValueAt(fila, 0).toString();
        }
        return null;
    }
    public DefaultTableModel getModelo() {
        return modelo;
    }
}