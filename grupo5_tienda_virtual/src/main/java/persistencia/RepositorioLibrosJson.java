package persistencia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import model.Libro;

/**
 * Persistencia de libros (inventario) en un archivo JSON (Gson via Maven).
 *
 * Libro no es abstracta y solo contiene datos simples y un enum, por lo que
 * no necesita un adaptador especial.
 */
public class RepositorioLibrosJson implements IRepositorio<Libro> {

    private static final String RUTA_POR_DEFECTO = "datos/libros.json";

    private final Path archivo;
    private final List<Libro> libros;
    private final Gson gson;
    private final Type tipoLista = new TypeToken<List<Libro>>() { }.getType();

    public RepositorioLibrosJson() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioLibrosJson(String ruta) {
        this.archivo = Path.of(ruta);
        this.libros = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        cargar();
    }

    @Override
    public List<Libro> listar() {
        return libros;
    }

    @Override
    public void agregar(Libro libro) {
        libros.add(libro);
    }

    @Override
    public boolean eliminar(Libro libro) {
        return libros.remove(libro);
    }

    @Override
    public void cargar() {
        libros.clear();
        libros.addAll(leerArchivo());
    }

    @Override
    public List<Libro> listarUltimos(int limite) {
        List<Libro> todos = leerArchivo();
        if (limite <= 0 || todos.isEmpty()) {
            return new ArrayList<Libro>();
        }
        int desde = Math.max(0, todos.size() - limite);
        List<Libro> ultimos = new ArrayList<Libro>(todos.subList(desde, todos.size()));
        Collections.reverse(ultimos);
        return ultimos;
    }

    @Override
    public List<Libro> listarPagina(int inicio, int cantidad) {
        List<Libro> todos = leerArchivo();
        int desde = Math.max(0, inicio);
        int hasta = Math.min(todos.size(), desde + Math.max(0, cantidad));
        if (desde >= hasta) {
            return new ArrayList<Libro>();
        }
        return new ArrayList<Libro>(todos.subList(desde, hasta));
    }

    @Override
    public int contar() {
        return leerArchivo().size();
    }

    private List<Libro> leerArchivo() {
        if (!Files.exists(archivo)) {
            return new ArrayList<Libro>();
        }
        try {
            String json = Files.readString(archivo, StandardCharsets.UTF_8);
            List<Libro> leidos = gson.fromJson(json, tipoLista);
            return leidos != null ? leidos : new ArrayList<Libro>();
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
            Files.writeString(archivo, gson.toJson(libros, tipoLista), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo escribir " + archivo.toAbsolutePath(), e);
        }
    }

    public Path getArchivo() {
        return archivo;
    }
}
