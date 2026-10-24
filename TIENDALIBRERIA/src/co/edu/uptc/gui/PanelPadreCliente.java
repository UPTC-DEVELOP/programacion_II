package co.edu.uptc.gui;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import co.edu.uptc.model.Cliente;
import java.awt.event.ActionListener;

import co.edu.uptc.util.Comandos;

public class PanelPadreCliente extends PanelCentral<Cliente> {

    public PanelPadreCliente(ActionListener evento) {
        super(evento);
    }

    @Override
    public void agregarTituloPanel() {
        tituloPanel = "Gestión de Clientes";
    }

    @Override
    public void agregarIdentificadorComandoBoton() {
        // Se reutilizan los botones CRUD de PanelCentral con los comandos de clientes
        btnEliminar.setText(Comandos.ELIMINAR_CLIENTE);
        btnActualizar.setText(Comandos.ACTUALIZAR_CLIENTE);
        btnCrear.setText(Comandos.CREAR_CLIENTE);
        btnVer.setText(Comandos.VER_CLIENTE);
        btnLimpiar.setText(Comandos.LIMPIAR_CLIENTE);
        btnBuscar.setText(Comandos.BUSCAR_CLIENTE);

        btnEliminar.setActionCommand(Comandos.ELIMINAR_CLIENTE);
        btnActualizar.setActionCommand(Comandos.ACTUALIZAR_CLIENTE);
        btnCrear.setActionCommand(Comandos.CREAR_CLIENTE);
        btnVer.setActionCommand(Comandos.VER_CLIENTE);
        btnLimpiar.setActionCommand(Comandos.LIMPIAR_CLIENTE);
        btnBuscar.setActionCommand(Comandos.BUSCAR_CLIENTE);
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
        tabla.setModel(modelo);
    }

    @Override
    public void poblarTabla(List<Cliente> listaClientes) {
        modelo.setRowCount(0);
        for (Cliente c : listaClientes) {
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

    // Devuelve el correo (llave del cliente) de la fila seleccionada
    public String getClienteSeleccionadoCorreo() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            return tabla.getModel().getValueAt(fila, 4).toString();
        }
        return null;
    }

    // Devuelve la cédula del cliente de la fila seleccionada
    public String getClienteSeleccionadoCedula() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            return tabla.getModel().getValueAt(fila, 0).toString();
        }
        return null;
    }

    // Resalta en la tabla al cliente con ese correo
    public void seleccionarClientePorCorreo(String correo) {
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {
            if (correo.equals(modelo.getValueAt(fila, 4))) {
                tabla.setRowSelectionInterval(fila, fila);
                return;
            }
        }
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }
}
