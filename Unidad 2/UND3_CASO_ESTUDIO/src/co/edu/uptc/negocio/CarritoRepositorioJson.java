package co.edu.uptc.negocio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CarritoRepositorioJson implements CarritoRepositorio {

    private final Path archivo;

    public CarritoRepositorioJson(Path archivo) {
        this.archivo = archivo;
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            if (!Files.exists(archivo)) {
                persistirTodos(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo preparar el archivo de carritos.", e);
        }
    }

    @Override
    public Optional<Carrito> buscarPorCliente(String correoCliente) {
        if (!Files.exists(archivo)) {
            return Optional.empty();
        }
        try {
            String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
            Carrito carrito = null;
            String clienteObjetivo = correoCliente.trim().toLowerCase();
            for (Map<String, String> m : JsonUtil.desdeJson(contenido)) {
                if (!clienteObjetivo.equals(m.get("correoCliente"))) {
                    continue;
                }
                if (carrito == null) {
                    carrito = new Carrito(clienteObjetivo);
                }
                carrito.agregar(new CarritoItem(
                        m.get("isbn"), m.get("titulo"), FormatoLibro.valueOf(m.get("formato")),
                        Double.parseDouble(m.get("precioBase")),
                        Double.parseDouble(m.get("porcentajeDescuento")),
                        Double.parseDouble(m.get("impuestoIVA")),
                        Integer.parseInt(m.get("cantidad"))));
            }
            return Optional.ofNullable(carrito);
        } catch (IOException | RuntimeException e) {
            throw new PersistenciaException("No se pudo leer el archivo carritos.json.", e);
        }
    }

    @Override
    public synchronized void guardar(Carrito carrito) {
        List<CarritoPersistido> todos = cargarTodos();
        todos.removeIf(x -> x.correoCliente.equals(carrito.getCorreoCliente()));
        for (CarritoItem item : carrito.getItems()) {
            todos.add(new CarritoPersistido(carrito.getCorreoCliente(), item));
        }
        persistirTodos(todos);
    }

    @Override
    public synchronized void eliminar(String correoCliente) {
        List<CarritoPersistido> todos = cargarTodos();
        todos.removeIf(x -> x.correoCliente.equals(correoCliente.trim().toLowerCase()));
        persistirTodos(todos);
    }

    private List<CarritoPersistido> cargarTodos() {
        List<CarritoPersistido> todos = new ArrayList<>();
        try {
            if (!Files.exists(archivo)) {
                return todos;
            }
            String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
            for (Map<String, String> m : JsonUtil.desdeJson(contenido)) {
                todos.add(new CarritoPersistido(
                        m.get("correoCliente"),
                        new CarritoItem(m.get("isbn"), m.get("titulo"), FormatoLibro.valueOf(m.get("formato")),
                                Double.parseDouble(m.get("precioBase")),
                                Double.parseDouble(m.get("porcentajeDescuento")),
                                Double.parseDouble(m.get("impuestoIVA")),
                                Integer.parseInt(m.get("cantidad")))));
            }
            return todos;
        } catch (IOException | RuntimeException e) {
            throw new PersistenciaException("No se pudo leer el archivo carritos.json.", e);
        }
    }

    private void persistirTodos(List<CarritoPersistido> todos) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (CarritoPersistido item : todos) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("correoCliente", item.correoCliente);
            m.put("isbn", item.item.getIsbn());
            m.put("titulo", item.item.getTitulo());
            m.put("formato", item.item.getFormato().name());
            m.put("precioBase", item.item.getPrecioBase());
            m.put("porcentajeDescuento", item.item.getPorcentajeDescuento());
            m.put("impuestoIVA", item.item.getImpuestoIVA());
            m.put("cantidad", item.item.getCantidad());
            lista.add(m);
        }
        try {
            Files.write(archivo, JsonUtil.aJson(lista).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar el carrito en JSON.", e);
        }
    }

    private static final class CarritoPersistido {
        private final String correoCliente;
        private final CarritoItem item;

        private CarritoPersistido(String correoCliente, CarritoItem item) {
            this.correoCliente = correoCliente;
            this.item = item;
        }
    }
}
