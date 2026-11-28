package co.edu.uptc.gui;

import java.awt.event.ActionListener;

import co.edu.uptc.util.Comandos;

public class DialogoCrearCliente extends DialogoCentralCliente {

    public DialogoCrearCliente(ActionListener evento, String tituloDialogo, boolean isCrear) {
        super(evento, tituloDialogo, isCrear);
    }

    @Override
    public void asignarComandoBotones() {
        btnGuardar.setActionCommand(Comandos.GUARDAR_CLIENTE);
        btnCerrar.setActionCommand(Comandos.CANCELAR_CREACION_CLIENTE);
    }
}
