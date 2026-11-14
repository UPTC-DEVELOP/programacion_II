package co.uptc.edu.persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

 //Conexión a la base de datos MariaDB de la librería.
 // Usa los valores por defecto de una instalación local; si existe el archivo
 // db.properties en la carpeta del proyecto, se toman de ahí (url, usuario, contrasena).

public final class ConexionBD {

    private static final String ARCHIVO_CONFIGURACION = "db.properties";
    private static final String URL_POR_DEFECTO =
            "jdbc:mariadb://localhost:3306/libreria_virtual?connectTimeout=3000";
    private static final String USUARIO_POR_DEFECTO = "root";
    private static final String CONTRASENA_POR_DEFECTO = "";

    private static final Properties CONFIGURACION = cargarConfiguracion();

    private ConexionBD() {}

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(
                    CONFIGURACION.getProperty("url", URL_POR_DEFECTO),
                    CONFIGURACION.getProperty("usuario", USUARIO_POR_DEFECTO),
                    CONFIGURACION.getProperty("contrasena", CONTRASENA_POR_DEFECTO));
        } catch (RuntimeException e) {
            // El driver puede fallar con errores propios (por ejemplo, autenticación de Windows).
            throw new SQLException("No fue posible autenticarse en MariaDB: " + e.getMessage(), e);
        }
    }

    // Indica si se puede abrir una conexión con la base de datos
    public static boolean disponible() {
        try (Connection conexion = getConnection()) {
            return true;
        } catch (SQLException e) {
            System.err.println("No hay conexion con la base de datos: " + e.getMessage());
            return false;
        }
    }

    private static Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        Path archivo = Paths.get(ARCHIVO_CONFIGURACION);
        if (Files.exists(archivo)) {
            try (InputStream entrada = Files.newInputStream(archivo)) {
                propiedades.load(entrada);
            } catch (IOException e) {
                System.err.println("No se pudo leer " + ARCHIVO_CONFIGURACION + ": " + e.getMessage());
            }
        }
        return propiedades;
    }
}
