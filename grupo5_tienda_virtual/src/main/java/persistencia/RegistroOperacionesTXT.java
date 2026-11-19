package persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroOperacionesTXT {

    private static final String RUTA_POR_DEFECTO = "datos/operaciones.txt";
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path archivo;

    public RegistroOperacionesTXT() {
        this(RUTA_POR_DEFECTO);
    }

    public RegistroOperacionesTXT(String ruta) {
        this.archivo = Path.of(ruta);
    }

    public void registrar(String descripcion) {
        try {
            Path carpeta = archivo.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            String linea = LocalDateTime.now().format(FORMATO_FECHA)
                    + "\t" + limpiar(descripcion) + System.lineSeparator();
            Files.writeString(archivo, linea, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo escribir el registro de operaciones: "
                            + archivo.toAbsolutePath(), e);
        }
    }

    private String limpiar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\t", " ").replace("\r", " ").replace("\n", " ");
    }
}
