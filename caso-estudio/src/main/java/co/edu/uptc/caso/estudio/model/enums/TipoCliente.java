package co.edu.uptc.caso.estudio.model.enums;

// Enumeración de perfiles de cliente y su porcentaje de descuento asociado
public enum TipoCliente {
    ESTANDAR(0.0),
    ADMINISTRADOR(0.0),
    VIP(0.10),
    PREMIUM(0.10);

    private final double descuento;

    TipoCliente(double descuento) {
        this.descuento = descuento;
    }

    // Porcentaje de descuento decimal asociado al tipo de cliente
    public double getDescuento() {
        return descuento;
    }

    // Convierte un texto en TipoCliente; si es nulo, vacío o desconocido retorna ESTANDAR
    public static TipoCliente desdeTexto(String valor) {
        if (valor == null) {
            return ESTANDAR;
        }
        String limpio = valor.trim();
        if (limpio.isEmpty()) {
            return ESTANDAR;
        }
        try {
            return TipoCliente.valueOf(limpio.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ESTANDAR;
        }
    }
}