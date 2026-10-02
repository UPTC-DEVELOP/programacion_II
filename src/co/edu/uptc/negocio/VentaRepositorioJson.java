package co.edu.uptc.negocio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class VentaRepositorioJson implements VentaRepositorio {

    private final Path archivo;

    public VentaRepositorioJson(Path archivo) {
        this.archivo = archivo;
    }

    public boolean existenVentasDelLibro(String isbn) {
        if (!Files.exists(archivo)) {
            return false;
        }
        try {
            String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
            for (Map<String, String> item : JsonUtil.desdeJson(contenido)) {
                if (isbn.equals(item.get("isbn"))) {
                    return true;
                }
            }
            return false;
        } catch (IOException | IllegalArgumentException e) {
            throw new PersistenciaException("No se pudo leer el archivo de ventas.", e);
        }
    }
}
