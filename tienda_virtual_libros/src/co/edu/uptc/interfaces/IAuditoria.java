package co.edu.uptc.interfaces;

/**
 * INTERFAZ IAuditoria  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * RF02 y RF03 exigen "generación automática del registro de actividad en el
 * log de auditoría". Esta interfaz oculta DÓNDE se guarda el log (hoy memoria/consola;
 * más adelante un archivo .txt).
 */
public interface IAuditoria {

    /**
     * @param operacion  REGISTRAR_LIBRO, ACTUALIZAR_LIBRO, ELIMINAR_LIBRO...
     * @param detalle    texto libre (ISBN afectado, motivo de error, etc.)
     */
    void registrar(String operacion, String detalle);
}
