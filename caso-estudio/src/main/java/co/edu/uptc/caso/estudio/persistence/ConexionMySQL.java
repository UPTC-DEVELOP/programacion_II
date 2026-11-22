package co.edu.uptc.caso.estudio.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    private static final String URL = ConfiguracionTienda.obtener(
            "db.url", "jdbc:mysql://localhost:3308/tienda_virtual?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER = ConfiguracionTienda.obtener("db.user", "root");
    private static final String PASSWORD = ConfiguracionTienda.obtener("db.password", "1234");

    private static boolean tablasInicializadas = false;

    private ConexionMySQL() {}

    public static Connection obtenerConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        if (!tablasInicializadas) {
            InicializadorBaseDatos.inicializarTablas(conn);
            tablasInicializadas = true;
        }
        return conn;
    }
}