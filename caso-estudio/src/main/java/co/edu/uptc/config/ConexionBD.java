package co.edu.uptc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

	private static final String URL = "jdbc:mysql://localhost:3306/caso_estudio"
			+ "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

	private static final String USUARIO = "root";
	private static final String CLAVE = "tu_clave";

	private ConexionBD() {
	}

	public static Connection obtenerConexion() throws SQLException {
		return DriverManager.getConnection(URL, USUARIO, CLAVE);
	}
}
