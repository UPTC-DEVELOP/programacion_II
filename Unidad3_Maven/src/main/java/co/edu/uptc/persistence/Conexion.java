package co.edu.uptc.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    private static final String URL_POR_DEFECTO =
            "jdbc:mariadb://localhost:3306/tienda_virtual_libros?useSSL=false&serverTimezone=UTC";
    private static final Properties CONFIGURACION = cargarConfiguracion();

    private static final String URL = obtenerConfiguracion(
            "db.url", "TIENDA_DB_URL", URL_POR_DEFECTO);
    private static final String USUARIO = obtenerConfiguracion(
            "db.user", "TIENDA_DB_USER", null);
    private static final String PASSWORD = obtenerConfiguracion(
            "db.password", "TIENDA_DB_PASSWORD", "");

    private static Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (InputStream entrada = Conexion.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (entrada != null) {
                propiedades.load(entrada);
            } else {
                System.err.println("No se encontró db.properties en src/main/resources.");
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer db.properties: " + e.getMessage());
        }
        return propiedades;
    }

    private static String obtenerConfiguracion(String clave, String variableEntorno,
                                               String valorPorDefecto) {
        String valor = CONFIGURACION.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            valor = System.getProperty(clave);
        }
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(variableEntorno);
        }
        if ((valor == null || valor.isBlank()) && valorPorDefecto != null) {
            return valorPorDefecto;
        }
        return valor;
    }

    public static Connection obtenerConexion() {
    	if (USUARIO == null || USUARIO.isBlank()) {
            System.err.println("Configure db.user y db.password en db.properties.");
            return null;
        }
        try {
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            System.err.println("No fue posible conectar con MariaDB: " + e.getMessage());
            return null;
        }
    }


public static void main(String[] args) {

    try (Connection conn = obtenerConexion()) {

        if (conn != null) {
            System.out.println("¡CONEXIÓN EXITOSA CON MARIADB!");
        } else {
            System.out.println("NO SE PUDO CONECTAR CON MARIADB.");
        }

    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }

}

}
