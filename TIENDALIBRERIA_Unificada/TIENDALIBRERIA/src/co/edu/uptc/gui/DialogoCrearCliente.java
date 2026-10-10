package co.edu.uptc.gui;

import co.uptc.edu.gui.libro.Evento;

public class DialogoCrearCliente extends DialogoCentralCliente {

    public DialogoCrearCliente(Evento evento, String tituloDialogo, boolean isCrear) {
        super(evento, tituloDialogo, isCrear);
    }

    @Override
    public void asignarComandoBotones() {
        btnGuardar.setActionCommand(Evento.GUARDAR_CLIENTE);
        btnCerrar.setActionCommand(Evento.CANCELAR_CREACION_CLIENTE);
    }
}
