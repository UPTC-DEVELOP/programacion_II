package co.edu.uptc.persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/*
 * GUARDA EL CORREO DEL USUARIO QUE INICIO SESION PARA RECORDARLO EN LA PROXIMA SESION.
 * @author: Equipo de desarrollo grupo 7 UPTC
 * */
/*modificacion:Brayan Panqueva*/
public final class SesionRecordadaRepository {

    private static final Path RUTA = Paths.get("data/sesion.properties");

    private SesionRecordadaRepository() {
    }

    public static String obtenerCorreo() {
        try {
            return Files.exists(RUTA) ? Files.readString(RUTA, StandardCharsets.UTF_8).trim() : "";
        } catch (IOException e) {
            return "";
        }
    }

    public static void guardarCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return;
        }
        try {
            Files.createDirectories(RUTA.toAbsolutePath().getParent());
            Files.writeString(RUTA, correo.trim().toLowerCase(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("No se pudo recordar la sesión: " + e.getMessage());
        }
    }
}
