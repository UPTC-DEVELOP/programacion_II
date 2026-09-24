package co.edu.uptc.negocio.modelo;


/**
 * Métodos de pago aceptados en la tienda (RF09).
 */

public enum MetodoPago {

	TARJETA_CREDITO("Tarjeta de Crédito"),
    TARJETA_DEBITO("Tarjeta de Débito"),
    PSE("PSE"),
    NEQUI("Nequi"),
    EFECTIVO("Efectivo");

    private final String nombreMostrar;

    MetodoPago(String nombreMostrar) {
        this.nombreMostrar = nombreMostrar;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    @Override
    public String toString() {
        return nombreMostrar;
    }
}
