package co.edu.uptc.tienda.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	private DataBaseConfig dataBaseConfig;
	private Connection conn = null;

	public Conexion() {
		this.dataBaseConfig = new DataBaseConfig();
	}

	public Connection conexion() {
		try {

			conn = DriverManager.getConnection(dataBaseConfig.generarURL(), dataBaseConfig.getUser(),
					dataBaseConfig.getPassword());
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
				conn = null;
			}
		} catch (SQLException e) {
			System.err.println("Error al cerrar la conexión -> " + e.getMessage());
		}
	}

	public Connection getConnection() {
		return conn;
	}
}