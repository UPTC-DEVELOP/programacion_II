package co.edu.uptc.caso.estudio.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Registra eventos operativos relevantes en un archivo de texto plano con marca de tiempo
public class RegistradorOperacionesTxt {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String rutaArchivo;

    public RegistradorOperacionesTxt() {
        this(ConfiguracionTienda.obtener("ruta.txt.log", "data/operaciones_tienda.txt"));
    }

    public RegistradorOperacionesTxt(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // Añade una línea con fecha y hora al archivo de log si el mensaje no está vacío
    public void registrar(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return;
        }
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return;
        }

        File archivo = new File(rutaArchivo);
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        String linea = "[" + LocalDateTime.now().format(FORMATO_FECHA) + "] " + mensaje;
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo, true), StandardCharsets.UTF_8))) {
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error escribiendo log TXT: " + e.getMessage());
        }
    }
}