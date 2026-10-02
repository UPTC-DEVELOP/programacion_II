package co.edu.uptc.negocio.admin;

/**
 * EXCEPCIÓN ReglaNegocioException  (paquete: negocio)
 * ---------------------------------------------------------------------------
 * Se lanza cuando se viola una regla de negocio (ISBN repetido, precio <= 0,
 * libro con ventas, etc.). Su mensaje es TEXTO LISTO PARA MOSTRAR al usuario.
 *
 * Es una excepción "checked" (extends Exception) a propósito: obliga a la capa
 * que llama (el controlador) a decidir qué hacer con el error, en lugar de
 * dejar que la aplicación se caiga.
 */
public class ReglaNegocioException extends Exception {

    private static final long serialVersionUID = 1L;

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
