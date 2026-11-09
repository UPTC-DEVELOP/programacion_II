package co.edu.uptc.persistencia.conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Locale;
import java.util.Properties;
/*
 * 
 * 
 * GESTIONA CONEXION CON postgreSQL, CARGA CREDENCIALES DESDE db.properties, Y ESTABLECE VARIABLES DE SESIÓN PARA RLS.
 * @author: Equipo de desarrollo grupo 7 
 * */
public final class ConexionDB {
    private static final Properties props = new Properties();
    private static final ThreadLocal<String> USUARIO_SESION = new ThreadLocal<>();

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
        // Constructor privado para prevenir instanciación.
    }

    /*modificacion:Brayan Panqueva*/
    public static Connection obtenerConexion() throws SQLException {
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");
        if (url == null || url.isBlank() || user == null || user.isBlank()) {
            throw new SQLException("Faltan propiedades de conexión PostgreSQL.");
        }
        try {
            Class.forName("org.postgresql.Driver");
            Connection nueva = DriverManager.getConnection(url.trim(), user.trim(), pass);
            configurarSesion(nueva, USUARIO_SESION.get());
            return nueva;
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de PostgreSQL.", e);
        }
    }

    /**
     * Establece la variable de sesión en PostgreSQL requerida para las políticas RLS .
     * @param email Correo electrónico del usuario autenticado en la aplicación Swing.
     */
    public static void establecerUsuarioSesion(String email) {
        USUARIO_SESION.set(email == null ? "" : email.trim().toLowerCase(Locale.ROOT));
    }

    public static String obtenerUsuarioSesion() { return USUARIO_SESION.get(); }

    public static void configurarSesion(Connection conexion, String email) throws SQLException {
        if (conexion == null) return;
        try (var pstmt = conexion.prepareStatement("SELECT set_config('app.user_email', ?, false)")) {
            pstmt.setString(1, email == null ? "" : email.trim().toLowerCase(Locale.ROOT));
            pstmt.execute();
        }
    }

    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try { conexion.close(); } catch (SQLException ignored) { }
        }
    }

    /**
     * Cierra la conexión activa de manera segura al apagar la aplicación.
     */
    public static void cerrarConexion() { USUARIO_SESION.remove(); }
}
