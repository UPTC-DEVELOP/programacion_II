package persistencia;

import java.util.List;

/**
 * Contrato generico de persistencia.
 *
 * Toda fuente de datos (archivo JSON, base de datos, archivo plano...) debe
 * poder leer, escribir, agregar y eliminar elementos. Quien usa la persistencia
 * depende SOLO de esta interfaz: si manejamos JSON hoy y una base de datos
 * manana, el resto del codigo no cambia (SOLID: Dependency Inversion).
 */
public interface IRepositorio<T> extends IRepositorioConsulta<T> {

    /**
     * Devuelve la lista almacenada con las referencias vivas a los elementos,
     * de modo que la capa de servicio pueda actualizar sus campos.
     */
    List<T> listar();

    /**
     * Agrega un elemento en memoria (NO persiste hasta llamar guardar()).
     */
    void agregar(T elemento);

    /**
     * Elimina un elemento en memoria (NO persiste hasta llamar guardar()).
     */
    boolean eliminar(T elemento);

    /**
     * Carga los datos desde el almacenamiento fisico.
     */
    void cargar();

    /**
     * Guarda los datos actuales en el almacenamiento fisico.
     */
    void guardar();

    /**
     * Compatibilidad: inserta en memoria y persiste con guardar().
     * La escritura incremental real se implementa en F09-F11.
     */
    default void insertar(T elemento) {
        agregar(elemento);
        guardar();
    }

    /**
     * Compatibilidad: persiste los cambios de un elemento ya presente en memoria.
     * Si el elemento no esta en la lista, no tiene efecto.
     */
    default void actualizar(T elemento) {
        guardar();
    }

    /**
     * Compatibilidad: elimina en memoria y persiste con guardar().
     * La escritura incremental real se implementa en F09-F11.
     *
     * @return true si el elemento existia y se persistio la eliminacion
     */
    default boolean eliminarPersistente(T elemento) {
        if (eliminar(elemento)) {
            guardar();
            return true;
        }
        return false;
    }
}
