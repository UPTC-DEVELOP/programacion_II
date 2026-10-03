package co.edu.uptc.negocio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PedidoRepositorioJson implements PedidoRepositorio {

    private final Path archivo;
    private final Map<Long, Pedido> pedidos = new LinkedHashMap<>();
    private long ultimoId = 0;

    public PedidoRepositorioJson(Path archivo) {
        this.archivo = archivo;
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            if (Files.exists(archivo)) {
                cargar();
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo preparar el archivo de pedidos.", e);
        }
    }

 
    public synchronized long siguienteId() {
        return ++ultimoId;
    }

  
    public List<Pedido> obtenerTodos() {
        return new ArrayList<>(pedidos.values());
    }

   
    public Optional<Pedido> buscarPorId(long id) {
        return Optional.ofNullable(pedidos.get(id));
    }


    public void guardar(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
        persistir();
    }

 
    public boolean eliminar(long id) {
        boolean eliminado = pedidos.remove(id) != null;
        if (eliminado) {
            persistir();
        }
        return eliminado;
    }

    // ===================== Utilidades internas =====================

    private void cargar() throws IOException {
        String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
        try {
            for (Map<String, String> m : JsonUtil.desdeJson(contenido)) {
                if (!m.containsKey("id")) {
                    continue; 
                }
                Pedido p = new Pedido(
                        Long.parseLong(m.get("id")),
                        LocalDateTime.parse(m.get("fecha")),
                        m.get("isbn"),
                        m.get("titulo"),
                        Integer.parseInt(m.get("cantidad")),
                        Double.parseDouble(m.get("precioUnitario")));
                pedidos.put(p.getId(), p);
                ultimoId = Math.max(ultimoId, p.getId());
            }
        } catch (RuntimeException e) {
            throw new PersistenciaException("El archivo ventas.json tiene un formato inválido.", e);
        }
    }

    private void persistir() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Pedido p : pedidos.values()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", p.getId());
            m.put("fecha", p.getFecha().toString());
            m.put("isbn", p.getIsbn());
            m.put("titulo", p.getTitulo());
            m.put("cantidad", p.getCantidad());
            m.put("precioUnitario", p.getPrecioUnitarioFinal());
            m.put("total", p.getTotal());
            lista.add(m);
        }
        try {
            Files.write(archivo, JsonUtil.aJson(lista).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar el historial de pedidos.", e);
        }
    }
}
