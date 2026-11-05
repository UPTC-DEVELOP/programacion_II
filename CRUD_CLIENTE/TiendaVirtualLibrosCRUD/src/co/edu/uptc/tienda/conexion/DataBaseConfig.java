package co.edu.uptc.tienda.conexion;

import java.io.InputStream;
import java.util.Properties;

public class DataBaseConfig {

	private String host;
	private int port;
	private String dataBase;
	private String user;
	private String password;

	public DataBaseConfig() {
		Properties prop = new Properties();

		try (InputStream input = DataBaseConfig.class.getClassLoader()
				.getResourceAsStream("credencialesAndres.properties")) {

			prop.load(input);

			this.host = prop.getProperty("db.host");
			this.user = prop.getProperty("db.user");
			this.dataBase = prop.getProperty("db.database");
			this.password = prop.getProperty("db.password");
			this.port = Integer.parseInt(prop.getProperty("db.port"));

		} catch (Exception e) {
			System.err.println("Error: El archivo (.properties) especificado no existe: " + e.getMessage());
		}

	}

	public String generarURL() {
		return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDataBase();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getDataBase() {
		return dataBase;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
