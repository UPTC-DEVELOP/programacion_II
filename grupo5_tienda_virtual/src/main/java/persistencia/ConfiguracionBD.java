package persistencia;

/**
 * Configuracion central de la base de datos.
 *
 * Todos los repositorios de base de datos (clientes, libros, compras...) deben
 * usar estas constantes para que apunten SIEMPRE a la misma base fisica
 * (datos/tienda.db) y cada uno maneje su propia tabla.
 */
public final class ConfiguracionBD {

    private ConfiguracionBD() {
    }

    /** Ruta del archivo SQLite compartido por todo el proyecto. */
    public static final String RUTA_BD = "datos/tienda.db";

    /** Prefijo de conexion JDBC para SQLite. */
    public static final String JDBC_PREFIJO = "jdbc:sqlite:";

    public static String url() {
        return JDBC_PREFIJO + RUTA_BD;
    }
}
