package co.edu.uptc.excepciones;

/**
 * EXCEPCIÓN LibroConVentasException  (paquete: excepciones)
 * ---------------------------------------------------------------------------
 * Se lanza cuando se intenta eliminar un libro que tiene ventas asociadas
 * (RF03: "validar que no tenga ventas asociadas").
 *
 * Hereda de ReglaNegocioException para integrarse con el manejo de errores
 * existente en la capa de presentación.
 *
 * Aplica: SRP (Single Responsibility Principle) - representa una única
 * restricción de integridad referencial del catálogo.
 */
public class LibroConVentasException extends ReglaNegocioException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepción con un mensaje listo para mostrar al usuario.
     *
     * @param mensaje Descripción legible del error
     */
    public LibroConVentasException(String mensaje) {
        super(mensaje);
    }
}
