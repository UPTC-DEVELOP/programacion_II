package co.edu.uptc.negocio;

public enum TipoCliente {
    REGULAR("Regular"),
    PREMIUM("Premium");

    private final String etiqueta;

    TipoCliente(String etiqueta) {
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
