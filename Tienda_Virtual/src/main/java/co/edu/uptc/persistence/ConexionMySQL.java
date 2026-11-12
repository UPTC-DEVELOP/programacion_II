package co.edu.uptc.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionMySQL {

    private static final String URL = "jdbc:mysql://localhost:3308/tienda_virtual?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234"; // Verifica que sea tu contraseña de MySQL 3308

    private static boolean tablasInicializadas = false;

    public static Connection obtenerConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        if (!tablasInicializadas) {
            inicializarTablas(conn);
            tablasInicializadas = true;
        }
        return conn;
    }

    private static void inicializarTablas(Connection conn) {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id VARCHAR(30) PRIMARY KEY,"
                + "nombre_completo VARCHAR(150) NOT NULL,"
                + "correo VARCHAR(120) NOT NULL UNIQUE,"
                + "direccion_envio VARCHAR(200),"
                + "departamento VARCHAR(80),"
                + "ciudad VARCHAR(80),"
                + "telefono VARCHAR(30),"
                + "tipo_cliente VARCHAR(30) NOT NULL,"
                + "porcentaje_descuento DOUBLE NOT NULL,"
                + "contrasena_hash VARCHAR(255) NOT NULL)";

        String sqlCarrito = "CREATE TABLE IF NOT EXISTS carrito_items ("
                + "cliente_id VARCHAR(30) NOT NULL,"
                + "libro_isbn VARCHAR(30) NOT NULL,"
                + "cantidad INT NOT NULL,"
                + "PRIMARY KEY (cliente_id, libro_isbn),"
                + "FOREIGN KEY (cliente_id) REFERENCES usuarios(id) ON DELETE CASCADE,"
                + "FOREIGN KEY (libro_isbn) REFERENCES libros(isbn) ON DELETE CASCADE)";

        try (Statement st = conn.createStatement()) {
            st.execute(sqlUsuarios);
            st.execute(sqlCarrito);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}