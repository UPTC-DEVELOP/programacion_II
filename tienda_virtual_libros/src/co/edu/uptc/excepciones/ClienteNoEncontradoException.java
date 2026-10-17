package co.edu.uptc.excepciones;

/**
 * EXCEPCIÓN ClienteNoEncontradoException  (paquete: excepciones)
 * ---------------------------------------------------------------------------
 * Se lanza cuando el idCliente con el que se invoca una operación del carrito
 * NO corresponde a un cliente existente en el sistema.
 *
 *  "VALIDAR SESIÓN" (RF10)
 * 
 *  - El servicio del carrito trabaja con el idCliente que le pasa la capa de
 *    presentación (que YA tiene la sesión activa: solo muestra el carrito a un
 *    usuario logueado). La validación que le corresponde al SERVICIO es
 *    verificar que ese idCliente exista de verdad en el repositorio de
 *    clientes. Si no existe, la "sesión" invocando el carrito es inválida.
 *  - La verificación estricta de "¿hay sesión activa?" vive en
 *    IGestionSeguridad y es responsabilidad de la GUI, no del servicio.
 *
 * 
 * LibroNoEncontradoException: misma jerarquía y misma justificación (checked).
 */
public class ClienteNoEncontradoException extends ReglaNegocioException {

    private static final long serialVersionUID = 1L;

    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
