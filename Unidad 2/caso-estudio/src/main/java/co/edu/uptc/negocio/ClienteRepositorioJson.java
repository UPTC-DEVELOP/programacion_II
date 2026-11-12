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

public class ClienteRepositorioJson implements ClienteRepositorio {

    private final Path archivo;
    private final Map<String, Cliente> clientes = new LinkedHashMap<>();

    public ClienteRepositorioJson(Path archivo) {
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
            throw new PersistenciaException("No se pudo preparar el archivo de clientes.", e);
        }
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return new ArrayList<>(clientes.values());
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correoElectronico) {
        if (correoElectronico == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(clientes.get(correoElectronico.trim().toLowerCase()));
    }

    @Override
    public void guardar(Cliente cliente) {
        clientes.put(cliente.getCorreoElectronico().trim().toLowerCase(), cliente);
        persistir();
    }

    @Override
    public boolean eliminar(String correoElectronico) {
        boolean eliminado = clientes.remove(correoElectronico.trim().toLowerCase()) != null;
        if (eliminado) {
            persistir();
        }
        return eliminado;
    }

    private void cargar() throws IOException {
        String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
        try {
            for (Map<String, String> m : JsonUtil.desdeJson(contenido)) {
                Cliente cliente = new Cliente(
                        m.get("nombreCompleto"),
                        m.get("correoElectronico"),
                        m.get("direccionEnvio"),
                        m.get("telefono"),
                        TipoCliente.valueOf(m.get("tipoCliente")),
                        m.get("contrasenaHash"));
                clientes.put(cliente.getCorreoElectronico().trim().toLowerCase(), cliente);
            }
        } catch (RuntimeException e) {
            throw new PersistenciaException("El archivo clientes.json tiene un formato inválido.", e);
        }
    }

    private void persistir() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Cliente cliente : clientes.values()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("nombreCompleto", cliente.getNombreCompleto());
            m.put("correoElectronico", cliente.getCorreoElectronico());
            m.put("direccionEnvio", cliente.getDireccionEnvio());
            m.put("telefono", cliente.getTelefono());
            m.put("tipoCliente", cliente.getTipoCliente().name());
            m.put("contrasenaHash", cliente.getContrasenaHash());
            lista.add(m);
        }
        try {
            Files.write(archivo, JsonUtil.aJson(lista).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar el archivo de clientes.", e);
        }
    }
}
