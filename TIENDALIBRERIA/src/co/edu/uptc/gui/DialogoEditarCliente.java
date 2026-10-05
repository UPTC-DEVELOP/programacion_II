package co.edu.uptc.gui;

import co.edu.uptc.modelo.Cliente;
import co.uptc.edu.gui.libro.Evento;

public class DialogoEditarCliente extends DialogoCentralCliente {

    public DialogoEditarCliente(Evento evento, String tituloDialogo, boolean isCrear) {
        super(evento, tituloDialogo, isCrear);
        // El correo es la llave del cliente: no se puede modificar
        txCorreo.setEnabled(false);
    }

    @Override
    public void asignarComandoBotones() {
        btnGuardar.setActionCommand(Evento.GUARDAR_ACTUALIZACION_CLIENTE);
        btnCerrar.setActionCommand(Evento.CANCELAR_CLIENTE);
    }

    // Precarga los datos actuales del cliente en las casillas
    public void cargarDatosCliente(Cliente cliente) {
        txCedula.setText(cliente.getCedula());
        txNombre.setText(cliente.getNombre());
        txApellido.setText(cliente.getApellido());
        txTelefono.setText(cliente.getTelefono());
        txCorreo.setText(cliente.getCorreo());
        txDireccion.setText(cliente.getDireccion());
        cbxTipoCliente.setSelectedItem(cliente.getTipoCliente());
    }
}
