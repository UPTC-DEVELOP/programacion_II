package co.edu.uptc.tienda.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	private String user = "root";
	private String password = "G@nzalez.2015";
	private String database = "sistema_biblioteca";
	private String port = "3306";
	private String url = "jdbc:mysql://localhost:" + port + "/" + database;

	private Connection conn = null;

	public Connection conexion() {
		try {

			conn = DriverManager.getConnection(url, user, password);
			return conn;

		} catch (SQLException e) {
			System.err.println("Error al crear la conexión con la Base de Datos -> ");
			e.printStackTrace();
			return null;
		}
	}

	public void desconectar() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			System.err.println("Error al cerrar la conexión -> " + e.getMessage());
		}
	}

	public Connection getConnection() {
		return conn;
	}
}