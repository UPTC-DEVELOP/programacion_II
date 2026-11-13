package co.edu.uptc.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    private static final String URL_POR_DEFECTO =
            "jdbc:mariadb://localhost:3306/tienda_virtual_libros?useSSL=false&serverTimezone=UTC";
    private static final Properties CONFIGURACION = cargarConfiguracion();

    private static final String URL = obtenerConfiguracion(
            "db.url", "TIENDA_DB_URL", URL_POR_DEFECTO);
    private static final String USUARIO = obtenerConfiguracion(
            "db.user", "TIENDA_DB_USER", null);
    private static final String PASSWORD = obtenerConfiguracion(
            "db.password", "TIENDA_DB_PASSWORD", null);

    private static Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (InputStream entrada = Conexion.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (entrada != null) {
                propiedades.load(entrada);
            } else {
                System.err.println("No se encontró db.properties en src/main/resources.");
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer db.properties: " + e.getMessage());
        }
        return propiedades;
    }

    private static String obtenerConfiguracion(String clave, String variableEntorno,
                                               String valorPorDefecto) {
        String valor = CONFIGURACION.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            valor = System.getProperty(clave);
        }
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(variableEntorno);
        }
        if ((valor == null || valor.isBlank()) && valorPorDefecto != null) {
            return valorPorDefecto;
        }
        return valor;
    }

    public static Connection obtenerConexion() {
        if (USUARIO == null || USUARIO.isBlank()
                || PASSWORD == null || PASSWORD.isBlank()
                || PASSWORD.equals("REEMPLACE_ESTE_VALOR")) {
            System.err.println("Configure db.user y db.password en db.properties.");
            return null;
        }
        try {
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            System.err.println("No fue posible conectar con MariaDB: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        try (Connection conn = obtenerConexion()) {
            if (conn != null) {
                System.out.println("¡Conexión exitosa a la base de datos de la tienda!");

                String insertQuery = "INSERT INTO libro "
                        + "(isbn, titulo, autor, anio_publicacion, categoria, editorial, "
                        + "numero_paginas, precio, stock, formato) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                        + "ON DUPLICATE KEY UPDATE stock = stock + 1";

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

                String selectQuery = "SELECT isbn, titulo, precio, stock FROM libro";
                try (PreparedStatement stmt = conn.prepareStatement(selectQuery);
                     ResultSet rs = stmt.executeQuery()) {
                    System.out.println("\n--- CATÁLOGO DE LIBROS EN LA BASE DE DATOS ---");
                    while (rs.next()) {
                        System.out.println("ISBN: " + rs.getString("isbn")
                                + " | Título: " + rs.getString("titulo")
                                + " | Precio: $" + rs.getDouble("precio")
                                + " | Stock: " + rs.getInt("stock"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la base de datos: " + e.getMessage());
        }
    }
}
