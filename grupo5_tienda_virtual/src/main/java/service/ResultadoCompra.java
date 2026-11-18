package service;

import model.Compra;

public final class ResultadoCompra {

    private final boolean exitoso;
    private final String mensaje;
    private final Compra compra;

    public ResultadoCompra(boolean exitoso, String mensaje, Compra compra) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.compra = compra;
    }

    public static ResultadoCompra exito(String mensaje, Compra compra) {
        return new ResultadoCompra(true, mensaje, compra);
    }

    public static ResultadoCompra error(String mensaje) {
        return new ResultadoCompra(false, mensaje, null);
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Compra getCompra() {
        return compra;
    }
}
