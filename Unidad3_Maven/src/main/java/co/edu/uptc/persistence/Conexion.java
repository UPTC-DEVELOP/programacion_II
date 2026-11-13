package co.edu.uptc.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = configuracion(
            "tienda.db.url", "TIENDA_DB_URL",
            "jdbc:mariadb://localhost:3306/basedatosuni3?useSSL=false&serverTimezone=UTC");
    private static final String USUARIO = configuracion("tienda.db.usuario", "TIENDA_DB_USER", null);
    private static final String PASSWORD = configuracion("tienda.db.password", "TIENDA_DB_PASSWORD", null);

    private static String configuracion(String propiedad, String variableEntorno, String valorPorDefecto) {
        String valor = System.getProperty(propiedad);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(variableEntorno);
        }
        if ((valor == null || valor.isBlank()) && valorPorDefecto != null) {
            return valorPorDefecto;
        }
        return valor;
    }

    public static Connection obtenerConexion() {
        Connection conexion = null;
        if (USUARIO == null || USUARIO.isBlank() || PASSWORD == null || PASSWORD.isBlank()) {
            System.err.println("Falta configurar TIENDA_DB_USER y TIENDA_DB_PASSWORD.");
            return null;
        }
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            System.err.println("No fue posible conectar con MariaDB: " + e.getMessage());
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
                String selectQuery = "SELECT isbn, titulo, precio, stock FROM libro";
                try (PreparedStatement stmt = conn.prepareStatement(selectQuery);
                     ResultSet rs = stmt.executeQuery()) {
                    System.out.println("\n--- CATÁLOGO DE LIBROS EN LA BASE DE DATOS ---");
                    while (rs.next()) {
                        System.out.println("ISBN: " + rs.getString("isbn") +
                                           " | Título: " + rs.getString("titulo") +
                                           " | Precio: $" + rs.getDouble("precio") +
                                           " | Stock: " + rs.getInt("stock"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }
}
