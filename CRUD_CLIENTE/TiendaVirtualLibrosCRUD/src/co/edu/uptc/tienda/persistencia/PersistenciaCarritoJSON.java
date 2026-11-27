package co.edu.uptc.tienda.persistencia;

import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.negocio.IPersistenciaCarrito;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PersistenciaCarritoJSON implements IPersistenciaCarrito {

    private static final Path ARCHIVO_CARRITOS = Paths.get("data", "carritos.json");

    private static final Type TIPO_LISTA_ITEMS =
            TypeToken.getParameterized(List.class, ItemGuardadoCarrito.class).getType();

    private static final Type TIPO_MAPA_CARRITOS =
            TypeToken.getParameterized(Map.class, String.class, TIPO_LISTA_ITEMS).getType();

    private final Gson gson;

    public PersistenciaCarritoJSON() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void guardarCarritoCliente(String claveCliente, List<ItemCarrito> items) {
        if (claveCliente == null || claveCliente.isBlank()) {
            return;
        }
        String clave = normalizarClave(claveCliente);
        Map<String, List<ItemGuardadoCarrito>> todos = leerTodos();

        if (items == null || items.isEmpty()) {
            todos.remove(clave);
        } else {
            List<ItemGuardadoCarrito> guardados = new ArrayList<>();
            for (ItemCarrito item : items) {
                if (item != null && item.getLibro() != null && item.getLibro().getIsbn() != null) {
                    String isbn = item.getLibro().getIsbn().trim();
                    if (!isbn.isEmpty() && item.getCantidad() > 0) {
                        guardados.add(new ItemGuardadoCarrito(isbn, item.getCantidad()));
                    }
                }
            }
            if (guardados.isEmpty()) {
                todos.remove(clave);
            } else {
                todos.put(clave, guardados);
            }
        }
        escribirTodos(todos);
    }

    @Override
    public List<ItemGuardadoCarrito> cargarCarritoCliente(String claveCliente) {
        if (claveCliente == null || claveCliente.isBlank()) {
            return Collections.emptyList();
        }
        Map<String, List<ItemGuardadoCarrito>> todos = leerTodos();
        List<ItemGuardadoCarrito> lista = todos.get(normalizarClave(claveCliente));
        if (lista == null || lista.isEmpty()) {
            return Collections.emptyList();
        }
        return convertirListaSegura(lista);
    }

    @Override
    public void eliminarCarritoCliente(String claveCliente) {
        if (claveCliente == null || claveCliente.isBlank()) {
            return;
        }
        String clave = normalizarClave(claveCliente);
        Map<String, List<ItemGuardadoCarrito>> todos = leerTodos();
        if (todos.remove(clave) != null) {
            escribirTodos(todos);
        }
    }

    private String normalizarClave(String claveCliente) {
        return claveCliente.trim().toLowerCase();
    }

    private Map<String, List<ItemGuardadoCarrito>> leerTodos() {
        if (!Files.exists(ARCHIVO_CARRITOS)) {
            return new LinkedHashMap<>();
        }
        try (Reader reader = Files.newBufferedReader(ARCHIVO_CARRITOS, StandardCharsets.UTF_8)) {
            Map<String, List<ItemGuardadoCarrito>> datos = gson.fromJson(reader, TIPO_MAPA_CARRITOS);
            if (datos == null) {
                return new LinkedHashMap<>();
            }

            Map<String, List<ItemGuardadoCarrito>> normalizado = new LinkedHashMap<>();
            for (Map.Entry<String, List<ItemGuardadoCarrito>> entry : datos.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                List<ItemGuardadoCarrito> listaLimpia = convertirListaSegura(entry.getValue());
                if (!listaLimpia.isEmpty()) {
                    normalizado.put(normalizarClave(entry.getKey()), listaLimpia);
                }
            }
            return normalizado;
        } catch (IOException | RuntimeException e) {
            System.err.println("Error al leer carritos.json con Gson -> " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }

    private List<ItemGuardadoCarrito> convertirListaSegura(List<?> listaOrigen) {
        List<ItemGuardadoCarrito> resultado = new ArrayList<>();
        if (listaOrigen == null) {
            return resultado;
        }

        for (Object rawItem : listaOrigen) {
            if (rawItem instanceof ItemGuardadoCarrito item) {
                if (item.getIsbn() != null && !item.getIsbn().isBlank() && item.getCantidad() > 0) {
                    resultado.add(new ItemGuardadoCarrito(item.getIsbn().trim(), item.getCantidad()));
                }
            } else if (rawItem instanceof Map<?, ?> mapItem) {
                Object isbnObj = mapItem.get("isbn");
                Object cantObj = mapItem.get("cantidad");
                if (isbnObj != null && cantObj != null) {
                    String isbn = String.valueOf(isbnObj).trim();
                    int cantidad = (cantObj instanceof Number num)
                            ? num.intValue()
                            : Integer.parseInt(String.valueOf(cantObj).trim());
                    if (!isbn.isEmpty() && cantidad > 0) {
                        resultado.add(new ItemGuardadoCarrito(isbn, cantidad));
                    }
                }
            }
        }
        return resultado;
    }

    private void escribirTodos(Map<String, List<ItemGuardadoCarrito>> todos) {
        try {
            if (ARCHIVO_CARRITOS.getParent() != null) {
                Files.createDirectories(ARCHIVO_CARRITOS.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(ARCHIVO_CARRITOS, StandardCharsets.UTF_8)) {
                gson.toJson(todos, TIPO_MAPA_CARRITOS, writer);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar carritos.json con Gson -> " + e.getMessage());
        }
    }
}