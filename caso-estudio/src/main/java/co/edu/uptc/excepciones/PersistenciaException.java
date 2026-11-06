package co.edu.uptc.excepciones;

/**
 * EXCEPCIÓN PersistenciaException  (paquete: excepciones)
 * ---------------------------------------------------------------------------
 * Se lanza cuando ocurre un error al leer o escribir los archivos JSON
 * (carpeta inexistente sin permisos, JSON corrupto, disco lleno, etc.).
 *
 * Extiende RuntimeException por decisión de diseño: las interfaces del
 * repositorio (ILibroRepositorio) no declaran excepciones checked y no se
 * desea obligar a todas las capas a agregar cláusulas throws. Los errores de
 * infraestructura se consideran no recuperables en esta aplicación académica.
 *
 * Aplica: SRP (Single Responsibility Principle) - representa un fallo de la
 * capa de infraestructura.
 */
public class PersistenciaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepción con un mensaje descriptivo.
     *
     * @param mensaje Descripción del fallo de persistencia
     */
    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Construye la excepción conservando la causa original.
     *
     * @param mensaje Descripción del fallo de persistencia
     * @param causa   Excepción original que provocó el error
     */
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
