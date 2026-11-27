package co.edu.uptc.caso.estudio.model.enums;

// Enumeración de métodos de pago soportados en el proceso de compra
public enum MetodoPago {
    TARJETA("Tarjeta de Crédito"),
    TRANSFERENCIA("Transferencia");

    private final String etiqueta;

    MetodoPago(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // Etiqueta legible para mostrar en la interfaz o comprobantes
    public String getEtiqueta() {
        return etiqueta;
    }

    // Resuelve un MetodoPago desde su nombre de enum o su etiqueta; retorna null si no coincide
    public static MetodoPago desdeTexto(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        if (limpio.isEmpty()) {
            return null;
        }
        for (MetodoPago metodo : values()) {
            if (metodo.name().equalsIgnoreCase(limpio) || metodo.etiqueta.equalsIgnoreCase(limpio)) {
                return metodo;
            }
        }
        return null;
    }
}