package co.edu.uptc.libreria.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

	private static final String URL = "jdbc:mariadb://localhost:3306/libreria_db?sslMode=disable";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	
	public static Connection getConnection() {
		Connection conexion = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conexion = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			System.err.println("Error: no se encontro el driver de Maria DB");
			e.printStackTrace();
		}catch (SQLException e) {
			System.err.println("Error al conectar a la base de datos: " + e.getMessage());
		}
		return conexion;
	}
	
	
}
