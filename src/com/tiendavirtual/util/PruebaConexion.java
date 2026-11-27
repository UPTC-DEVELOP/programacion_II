package com.tiendavirtual.util;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection conexion = Conexion.conectar()) {
            System.out.println("Base de datos conectada correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo conectar: " + e.getMessage());
        }
    }
}
