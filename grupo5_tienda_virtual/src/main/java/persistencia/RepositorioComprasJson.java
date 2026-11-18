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
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import model.Compra;

public class RepositorioComprasJson implements IRepositorio<Compra> {

    private static final String RUTA_POR_DEFECTO = "datos/compras.json";

    private final Path archivo;
    private final List<Compra> compras;
    private final Gson gson;
    private final Type tipoLista = new TypeToken<List<Compra>>() { }.getType();

    public RepositorioComprasJson() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioComprasJson(String ruta) {
        this.archivo = Path.of(ruta);
        this.compras = new ArrayList<Compra>();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
        cargar();
    }

    @Override
    public List<Compra> listar() {
        return compras;
    }

    @Override
    public void agregar(Compra compra) {
        if (compra.getId() == 0) {
            compra.setId(siguienteId());
        }
        compras.add(compra);
    }

    @Override
    public boolean eliminar(Compra compra) {
        return compras.remove(compra);
    }

    @Override
    public void cargar() {
        compras.clear();
        if (!Files.exists(archivo)) {
            return;
        }
        try {
            String json = Files.readString(archivo, StandardCharsets.UTF_8);
            List<Compra> leidas = gson.fromJson(json, tipoLista);
            if (leidas != null) {
                compras.addAll(leidas);
            }
        } catch (IOException | JsonParseException e) {
            throw new PersistenciaException(
                    "No se pudo leer " + archivo.toAbsolutePath(), e);
        }
    }

    @Override
    public void guardar() {
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            Files.writeString(archivo, gson.toJson(compras, tipoLista), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo escribir " + archivo.toAbsolutePath(), e);
        }
    }

    private long siguienteId() {
        long maximo = 0;
        for (Compra compra : compras) {
            maximo = Math.max(maximo, compra.getId());
        }
        return maximo + 1;
    }

    public Path getArchivo() {
        return archivo;
    }
}
