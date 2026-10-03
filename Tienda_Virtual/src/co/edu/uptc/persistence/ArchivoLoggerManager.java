package co.edu.uptc.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class ArchivoLoggerManager {
    private static final String RUTA_ARCHIVO = "operaciones_tienda.txt";

    public static synchronized void registrarOperacion(String mensaje) {
        try (FileWriter fw = new FileWriter(RUTA_ARCHIVO, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("[" + LocalDateTime.now() + "] - " + mensaje);
        } catch (IOException e) {
            System.err.println("Error al escribir en el log de operaciones: " + e.getMessage());
        }
    }
}