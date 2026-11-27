package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.FabricaClientes;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Persistencia JSON para clientes normalizando instancias mediante FabricaClientes
public class RepositorioUsuarioJson implements IRepositorioUsuario {

    private final ManejadorArchivoJson<Cliente> manejadorJson;

    public RepositorioUsuarioJson() {
        String ruta = ConfiguracionTienda.obtener("ruta.json.usuarios", "data/usuarios.json");
        Type tipoLista = new TypeToken<ArrayList<Cliente>>() {
        }.getType();
        this.manejadorJson = new ManejadorArchivoJson<>(ruta, tipoLista);
    }

    // Lee y normaliza todas las instancias de Cliente desde el archivo JSON
    private List<Cliente> cargar() {
        List<Cliente> leidos = manejadorJson.leerLista();
        List<Cliente> normalizados = new ArrayList<>();
        for (Cliente cliente : leidos) {
            if (cliente != null) {
                normalizados.add(normalizarInstancia(cliente));
            }
        }
        return normalizados;
    }

    // Reconstruye la subclase concreta de Cliente según su tipo y descuento
    private Cliente normalizarInstancia(Cliente c) {
        return FabricaClientes.crear(
                c.getId(), c.getNombreCompleto(), c.getCorreo(),
                c.getDireccionEnvio(), c.getDepartamento(), c.getCiudad(),
                c.getTelefono(), c.getTipoCliente(), c.getContrasenaHash());
    }

    // Registra un usuario si no coincide ni su ID ni su correo con un registro previo
    @Override
    public boolean guardarUsuario(Cliente cliente) {
        if (cliente == null || cliente.getId() == null || cliente.getCorreo() == null) {
            return false;
        }
        List<Cliente> lista = cargar();
        for (Cliente actual : lista) {
            if (actual != null) {
                boolean mismoId = actual.getId() != null && cliente.getId().equalsIgnoreCase(actual.getId());
                boolean mismoCorreo = actual.getCorreo() != null
                        && cliente.getCorreo().trim().equalsIgnoreCase(actual.getCorreo().trim());
                if (mismoId || mismoCorreo) {
                    return false;
                }
            }
        }

        lista.add(normalizarInstancia(cliente));
        manejadorJson.guardarLista(lista);
        return true;
    }

    // Actualiza los datos de un cliente existente por ID
    @Override
    public boolean actualizarUsuario(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            return false;
        }
        List<Cliente> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            Cliente actual = lista.get(i);
            if (actual != null && cliente.getId().equals(actual.getId())) {
                lista.set(i, normalizarInstancia(cliente));
                manejadorJson.guardarLista(lista);
                return true;
            }
        }
        return false;
    }

    // Elimina un cliente por ID y persiste la lista si se encontró
    @Override
    public boolean eliminarUsuario(String id) {
        if (id == null) {
            return false;
        }
        List<Cliente> lista = cargar();
        boolean removido = false;
        Iterator<Cliente> it = lista.iterator();
        while (it.hasNext()) {
            Cliente actual = it.next();
            if (actual != null && id.equals(actual.getId())) {
                it.remove();
                removido = true;
            }
        }
        if (removido) {
            manejadorJson.guardarLista(lista);
        }
        return removido;
    }

    // Busca un cliente por correo electrónico sin distinguir mayúsculas
    @Override
    public Cliente buscarPorCorreo(String correo) {
        if (correo == null) {
            return null;
        }
        String buscado = correo.trim();
        List<Cliente> lista = cargar();
        for (Cliente cliente : lista) {
            if (cliente != null && cliente.getCorreo() != null
                    && buscado.equalsIgnoreCase(cliente.getCorreo().trim())) {
                return cliente;
            }
        }
        return null;
    }

    // Busca un cliente por identificador exacto
    @Override
    public Cliente buscarPorId(String id) {
        if (id == null) {
            return null;
        }
        List<Cliente> lista = cargar();
        for (Cliente cliente : lista) {
            if (cliente != null && id.equals(cliente.getId())) {
                return cliente;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return cargar();
    }
}