package com.letsread.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionJDBC {
   // private static final String URL = "jdbc:mariadb://localhost:3306/lets_read_db";
    private static final String URL = "jdbc:mariadb://localhost:3306/lets_read_db?sslMode=disable";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // XAMPP por defecto: sin clave

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}