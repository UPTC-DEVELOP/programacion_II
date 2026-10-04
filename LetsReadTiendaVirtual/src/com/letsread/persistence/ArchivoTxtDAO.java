package com.letsread.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class ArchivoTxtDAO {
    private static final String RUTA_TXT = "registro_operaciones_letsread.txt";

    public static void registrarOperacion(String mensaje) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_TXT, true))) {
            writer.println("[" + LocalDateTime.now() + "] " + mensaje);
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo TXT: " + e.getMessage());
        }
    }
}