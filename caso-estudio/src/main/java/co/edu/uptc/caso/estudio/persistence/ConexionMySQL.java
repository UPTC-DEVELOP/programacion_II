package co.edu.uptc.caso.estudio.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Utilidad centralizada para obtener conexiones JDBC hacia la base de datos MySQL
public class ConexionMySQL {

    private static final String URL = ConfiguracionTienda.obtener(
            "db.url",
            "jdbc:mysql://localhost:3308/tienda_virtual?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER = ConfiguracionTienda.obtener("db.user", "root");
    private static final String PASSWORD = ConfiguracionTienda.obtener("db.password", "1234");

    private ConexionMySQL() {
    }

    // Abre y retorna una nueva conexión JDBC con los parámetros configurados
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}