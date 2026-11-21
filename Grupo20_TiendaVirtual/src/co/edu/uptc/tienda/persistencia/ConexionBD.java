package co.edu.uptc.tienda.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mariadb://localhost:3306/tienda_virtual?sslMode=disable";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public static String explicarError(SQLException e) {
        int codigo = e.getErrorCode();
        String estado = e.getSQLState();
        if (codigo == 1045) {
            return "el usuario o la clave no son correctos.";
        } else if (codigo == 1049) {
            return "no existe la base de datos tienda_virtual.";
        } else if (estado != null && estado.substring(0, 2).equals("08")) {
            return "no hay conexión con el servidor. Verifique que MariaDB esté encendido.";
        }
        return e.getMessage();
    }
}
