package co.edu.uptc.negocio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class BitacoraRepositorioTxt implements BitacoraRepositorio {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path archivo;

    public BitacoraRepositorioTxt(Path archivo) {
        this.archivo = archivo;
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo crear la carpeta de la bitácora.", e);
        }
    }

    public synchronized void registrar(String operacion, String detalle) {
        String linea = String.format("[%s] %-18s | %s%n",
                LocalDateTime.now().format(FORMATO_FECHA), operacion, detalle);
        try {
            Files.write(archivo, linea.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            // Confiabilidad: un fallo en la bitácora NO debe detener la operación principal
            System.err.println("No se pudo escribir en la bitácora: " + e.getMessage());
        }
    }
}

