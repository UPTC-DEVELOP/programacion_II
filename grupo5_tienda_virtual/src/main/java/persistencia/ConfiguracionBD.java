package persistencia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public static void crearCarpeta(String ruta) {
        Path padre = Path.of(ruta).toAbsolutePath().getParent();
        if (padre == null) {
            return;
        }
        try {
            Files.createDirectories(padre);
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo crear la carpeta de datos: " + padre, e);
        }
    }
}
