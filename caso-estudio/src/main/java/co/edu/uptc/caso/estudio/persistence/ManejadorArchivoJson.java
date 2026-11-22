package co.edu.uptc.caso.estudio.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ManejadorArchivoJson<T> {

    private final String rutaArchivo;
    private final Type tipoLista;
    private final Gson gson;

    public ManejadorArchivoJson(String rutaArchivo, Type tipoLista) {
        this.rutaArchivo = rutaArchivo;
        this.tipoLista = tipoLista;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, _, _) ->
                                new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, _, _) ->
                                LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, _, _) ->
                                new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, _, _) ->
                                LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
                .create();
    }

    public List<T> leerLista() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(archivo)) {
            List<T> datos = gson.fromJson(reader, tipoLista);
            return datos != null ? datos : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error leyendo JSON (" + rutaArchivo + "), reiniciando lista: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarLista(List<T> datos) {
        File archivo = new File(rutaArchivo);
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }
        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(datos, writer);
        } catch (IOException e) {
            System.err.println("Error guardando JSON (" + rutaArchivo + "): " + e.getMessage());
        }
    }
}