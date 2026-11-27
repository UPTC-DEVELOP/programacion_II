package co.edu.uptc.caso.estudio.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Lectura y escritura genérica de listas en archivos JSON con soporte para fechas Java 8+
public class ManejadorArchivoJson<T> {

    private final String rutaArchivo;
    private final Type tipoLista;
    private final Gson gson;

    public ManejadorArchivoJson(String rutaArchivo, Type tipoLista) {
        this.rutaArchivo = rutaArchivo;
        this.tipoLista = tipoLista;
        this.gson = construirGson();
    }

    // Configura Gson con adaptadores explícitos para LocalDateTime y LocalDate sin usar lambdas
    private Gson construirGson() {
        JsonSerializer<LocalDateTime> serializadorFechaHora = new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) {
                    return null;
                }
                return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        };

        JsonDeserializer<LocalDateTime> deserializadorFechaHora = new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                if (json == null || json.isJsonNull()) {
                    return null;
                }
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        };

        JsonSerializer<LocalDate> serializadorFecha = new JsonSerializer<LocalDate>() {
            @Override
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) {
                    return null;
                }
                return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        };

        JsonDeserializer<LocalDate> deserializadorFecha = new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                if (json == null || json.isJsonNull()) {
                    return null;
                }
                return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
            }
        };

        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, serializadorFechaHora)
                .registerTypeAdapter(LocalDateTime.class, deserializadorFechaHora)
                .registerTypeAdapter(LocalDate.class, serializadorFecha)
                .registerTypeAdapter(LocalDate.class, deserializadorFecha)
                .create();
    }

    // Lee y deserializa la lista desde el archivo JSON; retorna lista vacía si no existe o falla
    public List<T> leerLista() {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return new ArrayList<>();
        }
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            List<T> datos = gson.fromJson(reader, tipoLista);
            return datos != null ? datos : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error leyendo JSON (" + rutaArchivo + "), reiniciando lista: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Escribe la lista serializada en formato JSON creando directorios padres si hace falta
    public void guardarLista(List<T> datos) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return;
        }
        File archivo = new File(rutaArchivo);
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }
        List<T> listaSegura = datos != null ? datos : new ArrayList<T>();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8)) {
            gson.toJson(listaSegura, tipoLista, writer);
        } catch (IOException e) {
            System.err.println("Error guardando JSON (" + rutaArchivo + "): " + e.getMessage());
        }
    }
}