package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.FabricaClientes;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioUsuarioJson implements IRepositorioUsuario {

    private final ManejadorArchivoJson<Cliente> manejadorJson;

    public RepositorioUsuarioJson() {
        String ruta = ConfiguracionTienda.obtener("ruta.json.usuarios", "data/usuarios.json");
        Type tipoLista = new TypeToken<ArrayList<Cliente>>() {}.getType();
        this.manejadorJson = new ManejadorArchivoJson<>(ruta, tipoLista);
    }

    private List<Cliente> cargar() {
        return manejadorJson.leerLista().stream()
                .map(this::normalizarInstancia)
                .collect(Collectors.toList());
    }

    private Cliente normalizarInstancia(Cliente c) {
        return FabricaClientes.crear(
                c.getId(), c.getNombreCompleto(), c.getCorreo(),
                c.getDireccionEnvio(), c.getDepartamento(), c.getCiudad(),
                c.getTelefono(), c.getTipoCliente(), c.getContrasenaHash()
        );
    }

    @Override
    public boolean guardarUsuario(Cliente cliente) {
        if (cliente == null || cliente.getId() == null || cliente.getCorreo() == null) return false;
        List<Cliente> lista = cargar();
        boolean existe = lista.stream().anyMatch(c ->
                cliente.getId().equalsIgnoreCase(c.getId()) ||
                cliente.getCorreo().equalsIgnoreCase(c.getCorreo())
        );
        if (existe) return false;

        lista.add(normalizarInstancia(cliente));
        manejadorJson.guardarLista(lista);
        return true;
    }

    @Override
    public boolean actualizarUsuario(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) return false;
        List<Cliente> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            if (cliente.getId().equals(lista.get(i).getId())) {
                lista.set(i, normalizarInstancia(cliente));
                manejadorJson.guardarLista(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminarUsuario(String id) {
        if (id == null) return false;
        List<Cliente> lista = cargar();
        boolean removido = lista.removeIf(c -> id.equals(c.getId()));
        if (removido) {
            manejadorJson.guardarLista(lista);
        }
        return removido;
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        if (correo == null) return null;
        return cargar().stream()
                .filter(c -> correo.equalsIgnoreCase(c.getCorreo().trim()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Cliente buscarPorId(String id) {
        if (id == null) return null;
        return cargar().stream()
                .filter(c -> id.equals(c.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return cargar();
    }
}