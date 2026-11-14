package co.edu.uptc.model;

public enum MetodoPago {
    EFECTIVO("Efectivo"), TARJETA_CREDITO("Tarjeta de crédito"),
    TARJETA_DEBITO("Tarjeta débito"), PSE("PSE");
    private final String texto;
    MetodoPago(String texto) { this.texto = texto; }
    public String getTexto() { return texto; }
    @Override public String toString() { return texto; }
}
