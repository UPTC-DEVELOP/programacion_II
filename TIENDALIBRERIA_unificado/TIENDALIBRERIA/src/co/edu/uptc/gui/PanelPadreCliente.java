package co.edu.uptc.gui;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import co.edu.uptc.modelo.Cliente;
import co.uptc.edu.gui.libro.Evento;

public class PanelPadreCliente extends co.uptc.edu.gui.libro.PanelCentral {

    public PanelPadreCliente(Evento evento) {
        super(evento);
    }

    @Override
    public void agregarTituloPanel() {
        tituloPanel = "Gestión de Clientes";
    }

    @Override
    public void agregarIdentificadorComandoBoton() {
        // Se reutilizan los botones CRUD de PanelCentral con los comandos de clientes
        btnEliminar.setText(Evento.ELIMINAR_CLIENTE);
        btnActualizar.setText(Evento.ACTUALIZAR_CLIENTE);
        btnCrear.setText(Evento.CREAR_CLIENTE);
        btnVer.setText(Evento.VER_CLIENTE);
        btnLimpiar.setText(Evento.LIMPIAR_CLIENTE);
        btnBuscar.setText(Evento.BUSCAR_CLIENTE);

        btnEliminar.setActionCommand(Evento.ELIMINAR_CLIENTE);
        btnActualizar.setActionCommand(Evento.ACTUALIZAR_CLIENTE);
        btnCrear.setActionCommand(Evento.CREAR_CLIENTE);
        btnVer.setActionCommand(Evento.VER_CLIENTE);
        btnLimpiar.setActionCommand(Evento.LIMPIAR_CLIENTE);
        btnBuscar.setActionCommand(Evento.BUSCAR_CLIENTE);
    }

    @Override
    public void agregarCabeceraTabla() {
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Correo");
        modelo.addColumn("Dirección");
        modelo.addColumn("Tipo de cliente");
        tablaLibros.setModel(modelo);
    }

    @Override
    public void poblarTabla(List<?> listaClientes) {
        modelo.setRowCount(0);
        for (Object elemento : listaClientes) {
            if (elemento instanceof Cliente) {
                Cliente c = (Cliente) elemento;
                modelo.addRow(new Object[] {
                    c.getCedula(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getTelefono(),
                    c.getCorreo(),
                    c.getDireccion(),
                    c.getTipoCliente()
                });
            }
        }
    }

    // Devuelve el correo (llave del cliente) de la fila seleccionada
    public String getClienteSeleccionadoCorreo() {
        int fila = tablaLibros.getSelectedRow();
        if (fila != -1) {
            return tablaLibros.getModel().getValueAt(fila, 4).toString();
        }
        return null;
    }

    // Resalta en la tabla al cliente con ese correo
    public void seleccionarClientePorCorreo(String correo) {
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {
            if (correo.equals(modelo.getValueAt(fila, 4))) {
                tablaLibros.setRowSelectionInterval(fila, fila);
                return;
            }
        }
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }
}
