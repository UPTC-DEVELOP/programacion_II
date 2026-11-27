package com.tiendavirtual.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Crea conexiones a MySQL. Los datos de acceso NO se escriben en el código:
 * se leen de las variables de entorno DB_URL, DB_USER y DB_PASSWORD
 * o del archivo config.properties (ver config.properties.example).
 */
public class Conexion {

    private static final String ARCHIVO = "config.properties";
    private static final String URL_POR_DEFECTO =
        "jdbc:mysql://localhost:3306/tienda_virtual_libros";
    private static final Properties CONFIG = cargarConfiguracion();

    private Conexion() {
    }

    private static Properties cargarConfiguracion() {
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(ARCHIVO)) {
            p.load(in);
        } catch (IOException e) {
            // Sin archivo: se usan las variables de entorno.
        }
        return p;
    }

    private static String leer(String entorno, String clave, String porDefecto) {
        String valor = System.getenv(entorno);
        if (valor == null) {
            valor = CONFIG.getProperty(clave);
        }
        return valor != null ? valor : porDefecto;
    }

    public static Connection conectar() {
        String url = leer("DB_URL", "db.url", URL_POR_DEFECTO);
        String usuario = leer("DB_USER", "db.user", "root");
        String clave = leer("DB_PASSWORD", "db.password", null);

        if (clave == null) {
            throw new IllegalStateException(
                "Falta la contraseña de la base de datos. "
                + "Copie config.properties.example como config.properties "
                + "y complete db.password.");
        }

        try {
            return DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException e) {
            throw new RuntimeException(
                "No se pudo conectar a MySQL: " + e.getMessage(), e);
        }
    }
}
