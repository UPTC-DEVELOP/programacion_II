package co.edu.uptc.negocio.modelo;

/**
 * Tipo de cliente. Determina los descuentos aplicables (RF06).
 * - REGULAR: sin descuentos especiales
 * - PREMIUM: acceso a descuentos exclusivos
 */

public enum TipoCliente {

	REGULAR("Regular"),
    PREMIUM("Premium");

    private final String nombreMostrar;

    TipoCliente(String nombreMostrar) {
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
