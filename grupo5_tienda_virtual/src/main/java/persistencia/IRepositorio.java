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
     * @deprecated usar listarUltimos() o listarPagina().
     */
    @Deprecated
    List<T> listar();

    /**
     * @deprecated usar insertar().
     */
    @Deprecated
    void agregar(T elemento);

    /**
     * @deprecated usar eliminarPersistente().
     */
    @Deprecated
    boolean eliminar(T elemento);

    /**
     * @deprecated las consultas nuevas leen el almacenamiento cuando se necesitan.
     */
    @Deprecated
    void cargar();

    /**
     * @deprecated usar insertar(), actualizar() o eliminarPersistente().
     */
    @Deprecated
    void guardar();

    /**
     * Compatibilidad: inserta en memoria y persiste con guardar().
     * La escritura incremental real se implementa en F09-F11.
     */
    @SuppressWarnings("deprecation")
    default void insertar(T elemento) {
        agregar(elemento);
        guardar();
    }

    /**
     * Compatibilidad: persiste los cambios de un elemento ya presente en memoria.
     * Si el elemento no esta en la lista, no tiene efecto.
     */
    @SuppressWarnings("deprecation")
    default void actualizar(T elemento) {
        guardar();
    }

    /**
     * Compatibilidad: elimina en memoria y persiste con guardar().
     * La escritura incremental real se implementa en F09-F11.
     *
     * @return true si el elemento existia y se persistio la eliminacion
     */
    @SuppressWarnings("deprecation")
    default boolean eliminarPersistente(T elemento) {
        if (eliminar(elemento)) {
            guardar();
            return true;
        }
        return false;
    }
}
