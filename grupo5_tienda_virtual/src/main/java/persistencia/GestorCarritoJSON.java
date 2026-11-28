package persistencia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class GestorCarritoJSON {

    private static final String CARPETA_POR_DEFECTO = "datos/carritos";

    private final Path carpeta;
    private final Gson gson;
    private final Type tipoMapa = new TypeToken<Map<String, Integer>>() { }.getType();

    public GestorCarritoJSON() {
        this(CARPETA_POR_DEFECTO);
    }

    public GestorCarritoJSON(String rutaCarpeta) {
        this.carpeta = Path.of(rutaCarpeta);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Map<String, Integer> cargar(String correo) {
        if (correo == null || correo.isBlank()) {
            return new LinkedHashMap<String, Integer>();
        }
        Path archivo = ruta(correo);
        if (!Files.exists(archivo)) {
            return new LinkedHashMap<String, Integer>();
        }
        try {
            String json = Files.readString(archivo, StandardCharsets.UTF_8);
            Map<String, Integer> items = gson.fromJson(json, tipoMapa);
            return items != null ? items : new LinkedHashMap<String, Integer>();
        } catch (IOException | JsonParseException e) {
            throw new PersistenciaException(
                    "No se pudo leer el carrito de " + correo, e);
        }
    }

    public void guardar(Map<String, Integer> items, String correo) {
        if (correo == null || correo.isBlank()) {
            return;
        }
        try {
            Files.createDirectories(carpeta);
            Files.writeString(ruta(correo),
                    gson.toJson(items != null ? items : new LinkedHashMap<String, Integer>(),
                            tipoMapa),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo guardar el carrito de " + correo, e);
        }
    }

    public void eliminar(String correo) {
        if (correo == null || correo.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(ruta(correo));
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo eliminar el carrito de " + correo, e);
        }
    }

    private Path ruta(String correo) {
        String nombre = correo.toLowerCase().replaceAll("[^a-z0-9@._-]", "_");
        return carpeta.resolve(nombre + ".json");
    }
}
