package co.edu.uptc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


public class Evento implements ActionListener {

    private final Map<String, Runnable> acciones = new HashMap<>();

    public void registrar(String comando, Runnable accion) {
        acciones.put(comando, accion);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando == null) {
            return;
        }

        Runnable accion = acciones.get(comando);
        if (accion != null) {
            accion.run();
        }
    }
}
