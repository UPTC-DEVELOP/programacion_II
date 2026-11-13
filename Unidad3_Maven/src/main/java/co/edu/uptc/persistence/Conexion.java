package co.edu.uptc.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    private static final String URL = "jdbc:mariadb://localhost:3306/basedatosuni3?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "02016J";

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    public static void main(String[] args) {
        try (Connection conn = obtenerConexion()) {
            if (conn != null) {
                System.out.println("¡Conexión exitosa a la base de datos de la tienda!");

                // 1. Insertar un libro de prueba
                String insertQuery = "INSERT INTO libro (isbn, titulo, autor, anio_publicacion, categoria, editorial, numero_paginas, precio, stock, formato) "
                                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                                   + "ON DUPLICATE KEY UPDATE stock = stock + 1;";
                
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, "978-0134685991");
                    pstmt.setString(2, "Clean Code");
                    pstmt.setString(3, "Robert C. Martin");
                    pstmt.setInt(4, 2008);
                    pstmt.setString(5, "Tecnología");
                    pstmt.setString(6, "Prentice Hall");
                    pstmt.setInt(7, 464);
                    pstmt.setDouble(8, 85000.00);
                    pstmt.setInt(9, 10);
                    pstmt.setString(10, "Físico");
                    
                    pstmt.executeUpdate();
                    System.out.println("¡Libro insertado/verificado con éxito desde Java!");
                }

                // 2. Consultar los libros que hay en la base de datos
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT isbn, titulo, precio, stock FROM libro;");
                
                System.out.println("\n--- CATÁLOGO DE LIBROS EN LA BASE DE DATOS ---");
                while (rs.next()) {
                    System.out.println("ISBN: " + rs.getString("isbn") + 
                                       " | Título: " + rs.getString("titulo") + 
                                       " | Precio: $" + rs.getDouble("precio") + 
                                       " | Stock: " + rs.getInt("stock"));
                }
                rs.close();
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }
}