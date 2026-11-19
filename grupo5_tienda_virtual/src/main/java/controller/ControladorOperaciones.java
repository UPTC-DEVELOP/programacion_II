package controller;

import persistencia.RegistroOperacionesTXT;

public class ControladorOperaciones {

    private final RegistroOperacionesTXT registro;

    public ControladorOperaciones(RegistroOperacionesTXT registro) {
        this.registro = registro;
    }

    public void registrar(String descripcion) {
        if (registro != null) {
            registro.registrar(descripcion);
        }
    }
}
