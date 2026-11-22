package co.edu.uptc.caso.estudio.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistradorOperacionesTxt {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String rutaArchivo;

    public RegistradorOperacionesTxt() {
        this.rutaArchivo = ConfiguracionTienda.obtener("ruta.txt.log", "data/operaciones_tienda.txt");
    }

    public void registrar(String mensaje) {
        File archivo = new File(rutaArchivo);
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }
        String linea = "[" + LocalDateTime.now().format(FORMATO_FECHA) + "] " + mensaje;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error escribiendo log TXT: " + e.getMessage());
        }
    }
}