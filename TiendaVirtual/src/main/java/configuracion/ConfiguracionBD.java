package configuracion;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConfiguracionBD {
    private static final Properties P = new Properties();
    static {
        Path p = Path.of("config", "database.properties");
        if (Files.isRegularFile(p)) {
            try (InputStream in = Files.newInputStream(p)) { P.load(in); }
            catch (IOException e) { System.err.println("Configuracion BD: " + e.getMessage()); }
        }
    }
    private ConfiguracionBD() {}
    private static String get(String env, String property, String defaultValue) {
        String val = System.getenv(env);
        return val != null && !val.isBlank() ? val : P.getProperty(property, defaultValue);
    }
    public static boolean carritoEnJdbc() { return "jdbc".equalsIgnoreCase(get("CARRITO_STORAGE", "carrito.storage", "json")); }
    public static Connection conexion() throws SQLException {
        String url = get("DB_URL", "database.url", "jdbc:mysql://127.0.0.1:3306/tienda_virtual");
        String usuario = get("DB_USER", "database.user", "tienda_app");
        String clave = get("DB_PASSWORD", "database.password", "");
        if (clave.isBlank()) throw new SQLException("Configure DB_PASSWORD o config/database.properties antes de conectarse.");
        DriverManager.setLoginTimeout(5);
        return DriverManager.getConnection(url, usuario, clave);
    }
}
