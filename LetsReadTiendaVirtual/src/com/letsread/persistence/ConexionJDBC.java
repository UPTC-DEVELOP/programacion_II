package com.letsread.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionJDBC {
    private static final String URL = "jdbc:mysql://localhost:3306/lets_read_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "admin";

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC no encontrado.", e);
        }
    }
}