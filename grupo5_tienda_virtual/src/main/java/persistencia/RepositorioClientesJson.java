package persistencia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import model.Cliente;
import model.ClientePremium;
import model.ClienteRegular;

/**
 * Persistencia de clientes en un archivo JSON (libreria Gson via Maven).
 *
 * Deja la persistencia "en memoria": ahora los datos se guardan y se leen
 * del archivo datos/clientes.json entre sesiones.
 */
public class RepositorioClientesJson implements IRepositorio<Cliente> {

    private static final String RUTA_POR_DEFECTO = "datos/clientes.json";

    private final Path archivo;
    private final List<Cliente> clientes;
    private final Gson gson;

    public RepositorioClientesJson() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioClientesJson(String ruta) {
        this.archivo = Path.of(ruta);
        this.clientes = new ArrayList<>();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Cliente.class, new ClienteAdapter())
                .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
        cargar();
    }

    @Override
    public List<Cliente> listar() {
        return clientes;
    }

    @Override
    public void agregar(Cliente cliente) {
        clientes.add(cliente);
    }

    @Override
    public boolean eliminar(Cliente cliente) {
        return clientes.remove(cliente);
    }

    @Override
    public void cargar() {
        clientes.clear();
        if (!Files.exists(archivo)) {
            return;
        }
        try {
            String json = Files.readString(archivo, StandardCharsets.UTF_8);
            Type tipoLista = new TypeToken<List<Cliente>>() { }.getType();
            List<Cliente> leidos = gson.fromJson(json, tipoLista);
            if (leidos != null) {
                clientes.addAll(leidos);
            }
        } catch (IOException | JsonParseException e) {
            System.err.println("No se pudo leer " + archivo.toAbsolutePath() + ": " + e.getMessage());
        }
    }

    @Override
    public void guardar() {
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            Type tipoLista = new TypeToken<List<Cliente>>() { }.getType();
            Files.writeString(archivo, gson.toJson(clientes, tipoLista), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("No se pudo escribir " + archivo.toAbsolutePath() + ": " + e.getMessage());
        }
    }

    public Path getArchivo() {
        return archivo;
    }

    /**
     * Cliente es abstracta (Regular / Premium): este adaptador guarda en el JSON
     * un campo "tipo" para saber, al leerlo, que subclase reconstruir.
     */
    private static class ClienteAdapter implements JsonSerializer<Cliente>, JsonDeserializer<Cliente> {

        // Gson propio SIN el adaptador de Cliente (para no recursar), pero SI
        // con el de LocalDate, que serializa las fechas de las compras.
        private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
                .create();

        @Override
        public JsonElement serialize(Cliente cliente, Type tipoOrigen, JsonSerializationContext contexto) {
            JsonObject objeto = gson.toJsonTree(cliente, cliente.getClass()).getAsJsonObject();
            objeto.addProperty("tipo", cliente instanceof ClientePremium ? "premium" : "regular");
            return objeto;
        }

        @Override
        public Cliente deserialize(JsonElement json, Type tipoDestino, JsonDeserializationContext contexto)
                throws JsonParseException {
            JsonObject objeto = json.getAsJsonObject();
            String tipo = objeto.has("tipo") ? objeto.get("tipo").getAsString() : "regular";
            if ("premium".equals(tipo)) {
                return gson.fromJson(objeto, ClientePremium.class);
            }
            return gson.fromJson(objeto, ClienteRegular.class);
        }
    }
}
