package persistencia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
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
    public void insertar(Compra compra) {
        if (compra == null) {
            return;
        }
        List<Compra> todas = leerArchivo();
        if (compra.getId() == 0) {
            compra.setId(siguienteId(todas));
        }
        for (Compra existente : todas) {
            if (existente.getId() == compra.getId()) {
                throw new PersistenciaException(
                        "Id de compra duplicado: " + compra.getId());
            }
        }
        todas.add(compra);
        escribirTodas(todas);
    }

    @Override
    public void actualizar(Compra compra) {
        if (compra == null) {
            return;
        }
        List<Compra> todas = leerArchivo();
        boolean encontrada = false;
        for (int i = 0; i < todas.size(); i++) {
            if (todas.get(i).getId() == compra.getId()) {
                todas.set(i, compra);
                encontrada = true;
                break;
            }
        }
        if (encontrada) {
            escribirTodas(todas);
        }
    }

    @Override
    public boolean eliminarPersistente(Compra compra) {
        if (compra == null) {
            return false;
        }
        List<Compra> todas = leerArchivo();
        boolean eliminada = todas.removeIf(c -> c.getId() == compra.getId());
        if (eliminada) {
            escribirTodas(todas);
        }
        return eliminada;
    }

    @Override
    public void cargar() {
        compras.clear();
        compras.addAll(leerArchivo());
    }

    @Override
    public List<Compra> listarUltimos(int limite) {
        if (limite <= 0) {
            return new ArrayList<Compra>();
        }
        List<Compra> todas = leerArchivo();
        todas.sort(Comparator.comparingLong(Compra::getId).reversed());
        int hasta = Math.min(limite, todas.size());
        return new ArrayList<Compra>(todas.subList(0, hasta));
    }

    @Override
    public List<Compra> listarPagina(int inicio, int cantidad) {
        if (inicio < 0 || cantidad <= 0) {
            return new ArrayList<Compra>();
        }
        List<Compra> todas = leerArchivo();
        todas.sort(Comparator.comparingLong(Compra::getId));
        int desde = Math.min(inicio, todas.size());
        int hasta = Math.min(todas.size(), desde + cantidad);
        return new ArrayList<Compra>(todas.subList(desde, hasta));
    }

    @Override
    public int contar() {
        return leerArchivo().size();
    }

    @Override
    public List<Compra> buscar(String criterio) {
        List<Compra> resultado = new ArrayList<Compra>();
        if (criterio == null || criterio.isBlank()) {
            return resultado;
        }
        for (Compra compra : leerArchivo()) {
            if (criterio.equalsIgnoreCase(compra.getCorreoCliente())) {
                resultado.add(compra);
            }
        }
        resultado.sort(Comparator.comparingLong(Compra::getId).reversed());
        if (resultado.size() > 10) {
            return new ArrayList<Compra>(resultado.subList(0, 10));
        }
        return resultado;
    }

    private List<Compra> leerArchivo() {
        if (!Files.exists(archivo)) {
            return new ArrayList<Compra>();
        }
        try {
            String json = Files.readString(archivo, StandardCharsets.UTF_8);
            List<Compra> leidas = gson.fromJson(json, tipoLista);
            return leidas != null ? leidas : new ArrayList<Compra>();
        } catch (IOException | JsonParseException e) {
            throw new PersistenciaException(
                    "No se pudo leer " + archivo.toAbsolutePath(), e);
        }
    }

    @Override
    public void guardar() {
        escribirTodas(compras);
    }

    private void escribirTodas(List<Compra> lista) {
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            Files.writeString(archivo, gson.toJson(lista, tipoLista), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo escribir " + archivo.toAbsolutePath(), e);
        }
    }

    private long siguienteId() {
        return siguienteId(compras);
    }

    private long siguienteId(List<Compra> lista) {
        long maximo = 0;
        for (Compra compra : lista) {
            maximo = Math.max(maximo, compra.getId());
        }
        return maximo + 1;
    }

    public Path getArchivo() {
        return archivo;
    }
}
