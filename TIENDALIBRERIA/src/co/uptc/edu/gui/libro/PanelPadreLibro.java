package co.uptc.edu.gui.libro;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

import co.uptc.edu.libro.modelo.Libro;

public class PanelPadreLibro extends PanelCentral {

    public PanelPadreLibro(Evento evento) {
        super(evento);
    }

    @Override
    public void agregarTituloPanel() {
        tituloPanel = "Gestión de Libros";
    }

    @Override
    public void agregarIdentificadorComandoBoton() {
        btnEliminar.setActionCommand(Evento.ELIMINAR_LIBRO);
        btnVer.setActionCommand(Evento.VER_LIBRO);
        btnActualizar.setActionCommand(Evento.ACTUALIZAR_LIBRO);
        btnCrear.setActionCommand(Evento.CREAR_LIBRO);
        btnLimpiar.setActionCommand(Evento.LIMPIAR_LIBRO);
        btnBuscar.setActionCommand(Evento.BUSCAR_LIBRO);
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
        tablaLibros.setModel(modelo);
    }

    @Override
    public void poblarTabla(List<?> listaLibros) {
        modelo.setRowCount(0);
        List<Libro> libros = (List<Libro>) listaLibros;
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
        int fila = tablaLibros.getSelectedRow();
        if (fila != -1) {
            return tablaLibros.getModel().getValueAt(fila, 0).toString();
        }
        return null;
    }
    public DefaultTableModel getModelo() {
        return modelo;
    }
}