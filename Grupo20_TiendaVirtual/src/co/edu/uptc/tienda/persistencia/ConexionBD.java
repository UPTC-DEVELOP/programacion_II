package co.edu.uptc.tienda.persistencia;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static final String ARCHIVO_CONFIGURACION = "db.properties";
    private static final Properties CONFIGURACION = cargarConfiguracion();

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(CONFIGURACION.getProperty("url"),
                CONFIGURACION.getProperty("usuario"), CONFIGURACION.getProperty("clave"));
    }

    private static Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        propiedades.setProperty("url", "jdbc:mariadb://localhost:3306/tienda_virtual?sslMode=disable");
        propiedades.setProperty("usuario", "root");
        propiedades.setProperty("clave", "");
        File archivo = new File(ARCHIVO_CONFIGURACION);
        if (archivo.exists()) {
            try (FileReader lector = new FileReader(archivo)) {
                propiedades.load(lector);
            } catch (IOException e) {
                System.out.println("No se pudo leer " + ARCHIVO_CONFIGURACION + "; se usan los valores por defecto.");
            }
        }
        return propiedades;
    }

    public static String explicarError(SQLException e) {
        int codigo = e.getErrorCode();
        String estado = e.getSQLState();
        if (codigo == 1045) {
            return "el usuario o la clave no son correctos.";
        } else if (codigo == 1049) {
            return "no existe la base de datos tienda_virtual.";
        } else if (estado != null && estado.startsWith("08")) {
            return "no hay conexión con el servidor. Verifique que MariaDB esté encendido.";
        }
        return e.getMessage();
    }
}
