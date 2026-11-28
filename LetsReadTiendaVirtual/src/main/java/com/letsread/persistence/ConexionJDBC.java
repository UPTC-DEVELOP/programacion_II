package com.letsread.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionJDBC {
    // El pom usa mariadb-java-client, por eso la URL es jdbc:mariadb (no jdbc:mysql).
    private static final String URL = "jdbc:mariadb://localhost:3306/lets_read_db?sslMode=disable";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}