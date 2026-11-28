package co.uptc.edu.caso.estudio.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

	private static final String URL = "jdbc:mariadb://localhost:3306/tienda_virtual?createDatabaseIfNotExist=true";
	private static final String USUARIO = "tienda_user";
	private static final String PASSWORD = "Tienda1234";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static void inicializarTablas() {
        String sqlCliente = "CREATE TABLE IF NOT EXISTS cliente ("
                + "id_cliente INT AUTO_INCREMENT PRIMARY KEY, "
                + "nombre_completo VARCHAR(120) NOT NULL, "
                + "correo_electronico VARCHAR(100) NOT NULL, "
                + "direccion VARCHAR(150), "
                + "telefono VARCHAR(30), "
                + "tipo_cliente VARCHAR(50))";

        String sqlCompra = "CREATE TABLE IF NOT EXISTS compra ("
                + "id_compra INT PRIMARY KEY, "
                + "fecha DATETIME NOT NULL, "
                + "total DOUBLE NOT NULL, "
                + "estado VARCHAR(50) NOT NULL)";

        try (Connection con = conectar(); Statement st = con.createStatement()) {
            st.execute(sqlCliente);
            st.execute(sqlCompra);
        } catch (SQLException e) {
            System.err.println("Error al inicializar tablas: " + e.getMessage());
        }
    }
}