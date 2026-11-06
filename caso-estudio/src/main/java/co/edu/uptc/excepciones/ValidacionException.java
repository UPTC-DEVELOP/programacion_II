package co.edu.uptc.excepciones;

/**
 * EXCEPCIÓN ValidacionException  (paquete: excepciones)
 * ---------------------------------------------------------------------------
 * Se lanza cuando un dato de entrada no cumple una validación de negocio
 * (ISBN con longitud distinta de 13, título demasiado largo, año fuera de
 * rango, precio no positivo, stock negativo, etc.).
 *
 * Hereda de ReglaNegocioException para que pueda capturarse con el mismo
 * catch genérico que ya usa la capa de presentación, sin perder la posibilidad
 * de capturarla de forma específica.
 *
 * Aplica: SRP (Single Responsibility Principle) - encapsula los errores de
 * validación de datos.
 */
public class ValidacionException extends ReglaNegocioException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepción con un mensaje listo para mostrar al usuario.
     *
     * @param mensaje Descripción legible del dato inválido
     */
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
