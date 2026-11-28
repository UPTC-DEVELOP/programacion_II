package co.edu.uptc.caso.estudio.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Utilidad centralizada para obtener y validar conexiones JDBC hacia tienda_virtual en MySQL
public class ConexionMySQL {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3308";
    private static final String DEFAULT_DB = "tienda_virtual";
    private static final String DEFAULT_PARAMS = "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota";

    private static final String URL = resolverUrl();
    private static final String USER = ConfiguracionTienda.obtener("db.user", "root");
    private static final String PASSWORD = ConfiguracionTienda.obtener("db.password", "1234");

    private static volatile boolean driverCargado = false;

    private ConexionMySQL() {
    }

    // Construye la URL JDBC usando db.url si existe o combinando host, puerto y base de datos
    private static String resolverUrl() {
        String urlDirecta = ConfiguracionTienda.obtener("db.url", "");
        if (urlDirecta != null && !urlDirecta.trim().isEmpty()) {
            return urlDirecta.trim();
        }
        String host = ConfiguracionTienda.obtener("db.host", DEFAULT_HOST).trim();
        String puerto = ConfiguracionTienda.obtener("db.port", DEFAULT_PORT).trim();
        String baseDatos = ConfiguracionTienda.obtener("db.name", DEFAULT_DB).trim();
        String params = ConfiguracionTienda.obtener("db.params", DEFAULT_PARAMS).trim();

        String baseUrl = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDatos;
        if (params.isEmpty()) {
            return baseUrl;
        }
        return baseUrl + (params.startsWith("?") ? params : "?" + params);
    }

    // Carga el driver de MySQL Connector/J una sola vez de forma segura
    private static void asegurarDriver() throws SQLException {
        if (driverCargado) {
            return;
        }
        synchronized (ConexionMySQL.class) {
            if (driverCargado) {
                return;
            }
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                driverCargado = true;
            } catch (ClassNotFoundException e) {
                throw new SQLException(
                        "No se encontró el driver JDBC de MySQL (mysql-connector-j). Agrégalo al Build Path en Eclipse.",
                        e);
            }
        }
    }

    // Abre y retorna una nueva conexión JDBC activa hacia MySQL
    public static Connection obtenerConexion() throws SQLException {
        asegurarDriver();
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn == null || conn.isClosed()) {
                throw new SQLException("La conexión con MySQL se abrió cerrada.");
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("Error conectando a MySQL (" + URL + ") con usuario '" + USER + "': " + e.getMessage());
            throw e;
        }
    }

    // Verifica rápidamente si la conexión con MySQL responde
    public static boolean probarConexion() {
        try (Connection conn = obtenerConexion()) {
            return conn.isValid(3);
        } catch (SQLException e) {
            return false;
        }
    }

    // Expone la URL efectiva para diagnóstico sin exponer la contraseña
    public static String getUrlConfigurada() {
        return URL;
    }

    // Expone el usuario configurado
    public static String getUsuarioConfigurado() {
        return USER;
    }
}