package co.edu.uptc.excepciones;

/**
 * EXCEPCIÓN CantidadInvalidaException  (paquete: excepciones)
 * ---------------------------------------------------------------------------
 * Se lanza cuando la cantidad pedida NO es válida: menor o igual a cero
 * (validación del RF10: "cantidad > 0"). Es el guardia de entrada de toda
 * operación que agregue o modifique cantidades en el carrito (RF10 y RF11).
 *
 * 
 * LibroNoEncontradoException: misma jerarquía y misma justificación (checked).
 */
public class CantidadInvalidaException extends ReglaNegocioException {

    private static final long serialVersionUID = 1L;

    public CantidadInvalidaException(String mensaje) {
        super(mensaje);
    }
}
