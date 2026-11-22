package co.edu.uptc.caso.estudio.model.enums;

public enum MetodoPago {
    TARJETA("Tarjeta de Crédito"),
    TRANSFERENCIA("Transferencia");

    private final String etiqueta;

    MetodoPago(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
