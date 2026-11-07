package co.edu.uptc.persistencia.conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/*
 * 
 * 
 * GESTIONA CONEXION CON postgreSQL, CARGA CREDENCIALES DESDE db.properties, Y ESTABLECE VARIABLES DE SESIÓN PARA RLS.
 * @author: Equipo de desarrollo grupo 7 UPTC
 * */
public class ConexionDB {
    private static Connection conexion = null;
    private static final Properties props = new Properties();

    // Bloque estático para cargar las credenciales desde db.properties una sola vez al iniciar la clase
    static {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("¡Atención! No se encontró el archivo db.properties en el classpath. Asegúrate de configurarlo localmente.");
            } else {
                props.load(input);
            }
        } catch (Exception e) {
            System.err.println("Error crítico al cargar el archivo de configuración de base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ConexionDB() {
        // Constructor privado para prevenir instanciación (Patrón Singleton)
    }

    /**
     * Obtiene la instancia única de la conexión a PostgreSQL.
     * Si la conexión está cerrada o es nula, establece una nueva usando db.properties.
     */
    public static synchronized Connection obtenerConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");

            // Validaciones defensivas para el equipo de desarrollo local
            if (url == null || user == null) {
                throw new SQLException("Faltan propiedades de conexión o no se puede acceder a ellas.");
            }

            try {
                // Carga explícita del driver de PostgreSQL 
                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException e) {
                throw new SQLException("error de comunicacion con postgreSQL, intentelo de nuevo.", e);
            }
        }
        return conexion;
    }

    /**
     * Establece la variable de sesión en PostgreSQL requerida para las políticas RLS .
     * @param email Correo electrónico del usuario autenticado en la aplicación Swing.
     */
    public static void establecerUsuarioSesion(String email) {
        String query = "SELECT set_config('app.user_email', ?, false)";
        try {
            Connection conn = obtenerConexion();
            try (var pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, email != null ? email.toLowerCase().trim() : "");
                pstmt.execute();
            }
        } catch (SQLException e) {
            System.err.println("Error al configurar la variable de sesión para RLS: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión activa de manera segura al apagar la aplicación.
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}