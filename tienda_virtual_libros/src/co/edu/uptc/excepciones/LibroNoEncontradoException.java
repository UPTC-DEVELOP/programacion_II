package co.edu.uptc.excepciones;

/**
 * EXCEPCIÓN LibroNoEncontradoException  (paquete: excepciones)
 * ---------------------------------------------------------------------------
 * Se lanza cuando el ISBN pedido por el cliente NO existe en el catálogo
 * (validación del RF10: "ISBN existe en catálogo").
 *
 * ¿POR QUÉ EXTIENDE ReglaNegocioException?
 *  - Consistencia con la convención YA establecida en el proyecto: toda
 *    violación de regla de negocio es una excepción CHECKED que obliga a la
 *    capa que llama (controlador/GUI) a manejarla.
 *  - Al heredar de ReglaNegocioException, un catch genérico de esa clase
 *    captura TODAS las fallas de negocio del carrito (SKU, stock, cantidad,
 *    cliente) sin repetir 4 catches; pero aún permite capturarla de forma
 *    específica si la GUI quiere mostrar un mensaje particular.
 */
public class LibroNoEncontradoException extends ReglaNegocioException {

    private static final long serialVersionUID = 1L;

    public LibroNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
