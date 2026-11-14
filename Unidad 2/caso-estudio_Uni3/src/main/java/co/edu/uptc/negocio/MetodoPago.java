package co.edu.uptc.negocio;

public enum MetodoPago {
    TARJETA("Tarjeta"),
    PSE("PSE"),
    TRANSFERENCIA("Transferencia bancaria"),
    EFECTIVO("Efectivo");

    private final String etiqueta;

    MetodoPago(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
