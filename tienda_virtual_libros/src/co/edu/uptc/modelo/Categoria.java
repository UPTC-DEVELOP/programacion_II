
package co.edu.uptc.modelo;

/**
 * ENUMERACIÓN Categoria  (paquete: modelo)
 * ---------------------------------------------------------------------------
 * RF01 exige que la categoría sea "una de las opciones válidas del enumerado":
 * CIENCIA_FICCION, HISTORIA, TECNOLOGIA, ROMANCE, OTROS.
 *
 * ¿Por qué un enum y no un String?
 *  - Es imposible guardar una categoría inválida (el compilador lo impide).
 *  - La validación "categoría válida" del RF01 queda resuelta por diseño.
 *
 * Cada constante guarda una "etiqueta" legible para mostrarla en el JComboBox
 * (la GUI muestra "Ciencia ficción", no "CIENCIA_FICCION").
 */
public enum Categoria {

    CIENCIA_FICCION("Ciencia ficción"),
    HISTORIA("Historia"),
    TECNOLOGIA("Tecnología"),
    ROMANCE("Romance"),
    OTROS("Otros");

    /** Texto amigable para la interfaz gráfica. */
    private final String etiqueta;

    Categoria(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    /** El JComboBox usa toString() para pintar cada opción. */
    @Override
    public String toString() {
        return etiqueta;
    }
}