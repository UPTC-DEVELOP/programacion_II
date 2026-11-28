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

        String sqlDetalle = "CREATE TABLE IF NOT EXISTS detalle_compra ("
                + "id_detalle INT AUTO_INCREMENT PRIMARY KEY, "
                + "id_compra INT NOT NULL, "
                + "isbn_libro VARCHAR(20) NOT NULL, "
                + "titulo_libro VARCHAR(150), "
                + "cantidad INT NOT NULL, "
                + "precio_unitario DOUBLE NOT NULL, "
                + "subtotal DOUBLE NOT NULL, "
                + "FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON DELETE CASCADE)";

        try (Connection con = conectar(); Statement st = con.createStatement()) {
            st.execute(sqlCliente);
            st.execute(sqlCompra);
            st.execute(sqlDetalle);
        } catch (SQLException e) {
            System.err.println("Error al inicializar tablas: " + e.getMessage());
        }
    }
}