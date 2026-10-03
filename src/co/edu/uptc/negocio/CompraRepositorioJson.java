package co.edu.uptc.negocio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompraRepositorioJson implements CompraRepositorio {

    private final Path archivo;
    private final Map<Long, Compra> compras = new LinkedHashMap<>();
    private long ultimoId;

    public CompraRepositorioJson(Path archivo) {
        this.archivo = archivo;
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            if (Files.exists(archivo)) {
                cargar();
            } else {
                persistir();
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo preparar el archivo de compras.", e);
        }
    }

    @Override
    public synchronized long siguienteId() {
        return ++ultimoId;
    }

    @Override
    public List<Compra> obtenerTodas() {
        return new ArrayList<>(compras.values());
    }

    @Override
    public Optional<Compra> buscarPorId(long id) {
        return Optional.ofNullable(compras.get(id));
    }

    @Override
    public List<Compra> obtenerPorCliente(String correoCliente) {
        if (correoCliente == null) {
            return new ArrayList<>();
        }
        return compras.values().stream()
                .filter(c -> correoCliente.trim().equalsIgnoreCase(c.getCorreoCliente()))
                .sorted(Comparator.comparing(Compra::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public synchronized void guardar(Compra compra) {
        compras.put(compra.getId(), compra);
        persistir();
    }

    private void cargar() throws IOException {
        String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
        try {
            Map<Long, List<Map<String, String>>> agrupadas = new LinkedHashMap<>();
            for (Map<String, String> m : JsonUtil.desdeJson(contenido)) {
                long id = Long.parseLong(m.get("id"));
                agrupadas.computeIfAbsent(id, x -> new ArrayList<>()).add(m);
            }
            for (Map.Entry<Long, List<Map<String, String>>> entry : agrupadas.entrySet()) {
                List<Map<String, String>> filas = entry.getValue();
                Map<String, String> cabecera = filas.get(0);
                List<DetalleCompra> detalles = new ArrayList<>();
                for (Map<String, String> m : filas) {
                    detalles.add(new DetalleCompra(
                            m.get("isbn"), m.get("titulo"), FormatoLibro.valueOf(m.get("formato")),
                            Integer.parseInt(m.get("cantidad")),
                            Double.parseDouble(m.get("precioUnitarioSinIva")),
                            Double.parseDouble(m.get("subtotalDetalle")),
                            Double.parseDouble(m.get("impuestoDetalle")),
                            Double.parseDouble(m.get("totalDetalle"))));
                }
                Compra compra = new Compra(
                        entry.getKey(), LocalDateTime.parse(cabecera.get("fecha")),
                        cabecera.get("correoCliente"), cabecera.get("nombreCliente"),
                        TipoCliente.valueOf(cabecera.get("tipoCliente")),
                        MetodoPago.valueOf(cabecera.get("metodoPago")), detalles,
                        Double.parseDouble(cabecera.get("subtotal")),
                        Double.parseDouble(cabecera.get("impuestos")),
                        Double.parseDouble(cabecera.get("descuentoPremium")),
                        Double.parseDouble(cabecera.get("total")));
                compras.put(compra.getId(), compra);
                ultimoId = Math.max(ultimoId, compra.getId());
            }
        } catch (RuntimeException e) {
            throw new PersistenciaException("El archivo compras.json tiene un formato inválido.", e);
        }
    }

    private void persistir() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Compra compra : compras.values()) {
            for (DetalleCompra detalle : compra.getDetalles()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", compra.getId());
                m.put("fecha", compra.getFecha().toString());
                m.put("correoCliente", compra.getCorreoCliente());
                m.put("nombreCliente", compra.getNombreCliente());
                m.put("tipoCliente", compra.getTipoCliente().name());
                m.put("metodoPago", compra.getMetodoPago().name());
                m.put("subtotal", compra.getSubtotal());
                m.put("impuestos", compra.getImpuestos());
                m.put("descuentoPremium", compra.getDescuentoPremium());
                m.put("total", compra.getTotal());
                m.put("isbn", detalle.getIsbn());
                m.put("titulo", detalle.getTitulo());
                m.put("formato", detalle.getFormato().name());
                m.put("cantidad", detalle.getCantidad());
                m.put("precioUnitarioSinIva", detalle.getPrecioUnitarioSinIva());
                m.put("subtotalDetalle", detalle.getSubtotal());
                m.put("impuestoDetalle", detalle.getImpuesto());
                m.put("totalDetalle", detalle.getTotal());
                lista.add(m);
            }
        }
        try {
            Files.write(archivo, JsonUtil.aJson(lista).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar el historial de compras.", e);
        }
    }
}
