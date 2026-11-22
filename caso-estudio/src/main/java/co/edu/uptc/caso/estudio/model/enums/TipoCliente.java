package co.edu.uptc.caso.estudio.model.enums;

public enum TipoCliente {
    ESTANDAR(0.0),
    ADMINISTRADOR(0.0),
    VIP(0.10),
    PREMIUM(0.10);

    private final double descuento;

    TipoCliente(double descuento) {
        this.descuento = descuento;
    }

    public double getDescuento() {
        return descuento;
    }
}
