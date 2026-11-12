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
        if (existeEnArchivo(archivo, isbn)) {
            return true;
        }
        // Desde V3 las compras completas se almacenan en compras.json.
        Path compras = archivo.getParent() == null
                ? java.nio.file.Paths.get("compras.json")
                : archivo.getParent().resolve("compras.json");
        return existeEnArchivo(compras, isbn);
    }

    private boolean existeEnArchivo(Path ruta, String isbn) {
        if (!Files.exists(ruta)) {
            return false;
        }
        try {
            String contenido = new String(Files.readAllBytes(ruta), StandardCharsets.UTF_8);
            for (Map<String, String> item : JsonUtil.desdeJson(contenido)) {
                if (isbn.equals(item.get("isbn"))) {
                    return true;
                }
            }
            return false;
        } catch (IOException | IllegalArgumentException e) {
            throw new PersistenciaException("No se pudo leer el archivo de ventas o compras.", e);
        }
    }
}
