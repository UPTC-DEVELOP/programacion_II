package co.edu.uptc.persistencia;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.dto.ClientePersistenciaDto;
import co.edu.uptc.modelo.mapper.ClientePersistenciaMapper;

/**
 * Implementación del repositorio de clientes con persistencia en JSON.
 *
  *Gestiona el archivo data/clientes.json: si no existe lo crea vacio,
 * y en cada operación lee el archivo, aplica el cambio y lo vuelve a
 * escribir. No contiene reglas de negocio (correo único, formato,
 * cifrado…); esas viven en GestionCliente y ValidadorDatos (SRP).
 *
 *  *Cumple LSP y DIP: cualquier clase que espere un IClienteRepositorio
 * puede usarla sin cambiar la capa de negocio ni la GUI
 *
 * El identificador se asigna de forma secuencial con el campo ultimoId,
 * que se guarda en el archivo para que los ids no se repitan después de
 * reiniciar la aplicación.
 *
 * Estructura del archivo:
 * 
 * {
 *   "clientes": [ { "idCliente": 1, "correoElectronico": "...", "contrasenia": "&lt;hash&gt;", ... } ],
 *   "ultimoId": 3
 * }
 *
 */
public class ClienteJsonRepository implements IClienteRepositorio {

    /** Ruta usada por la aplicación real. */
    private static final String RUTA_POR_DEFECTO = "data/clientes.json";

    private final Gson gson;
    private final Path rutaArchivo;

    /**
     * Construye el repositorio usando la ruta por defecto data/clientes.json.
     */
    public ClienteJsonRepository() {
        this(RUTA_POR_DEFECTO);
    }

    /**
     * Construye el repositorio con una ruta personalizada. Se usa en las
     * pruebas para no modificar el archivo real.
     *
     * @param rutaArchivo Ruta del archivo JSON de clientes
     */
    public ClienteJsonRepository(String rutaArchivo) {
        this.rutaArchivo = Paths.get(rutaArchivo);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        inicializarArchivo();
    }

    /**
     * Crea la carpeta y el archivo vacio si no existen
     * o si el archivo esta vacio
     */
    private void inicializarArchivo() {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            if (!Files.exists(rutaArchivo) || Files.size(rutaArchivo) == 0) {
                ClienteData datos = new ClienteData();
                escribir(datos);
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo inicializar el archivo de clientes: "
                    + rutaArchivo, e);
        }
    }

    // ------------------------- IClienteRepositorio -------------------------

    /**
     * Guarda un cliente nuevo. Si llega con id 0 se le asigna el siguiente id
     * secuencial (ultimoId + 1) y se actualiza también en el objeto recibido.
     */
    @Override
    public synchronized void guardar(Cliente cliente) {
        if (cliente == null) {
            return;
        }
        ClienteData datos = leer();
        if (cliente.getIdCliente() == 0) {
            cliente.setIdCliente(datos.getUltimoId() + 1);
        }
        datos.setUltimoId(Math.max(datos.getUltimoId(), cliente.getIdCliente()));
        datos.getClientes().add(ClientePersistenciaMapper.toDto(cliente));
        escribir(datos);
    }

    /**
     * Reemplaza el cliente que tenga el mismo id (o, si no lo encuentra, la
     * misma identificación). Si no existe, no hace nada.
     */
    @Override
    public synchronized void actualizar(Cliente clienteActualizado) {
        if (clienteActualizado == null) {
            return;
        }
        ClienteData datos = leer();
        List<ClientePersistenciaDto> clientes = datos.getClientes();
        for (int i = 0; i < clientes.size(); i++) {
            ClientePersistenciaDto actual = clientes.get(i);
            boolean mismoId = actual.getIdCliente() == clienteActualizado.getIdCliente();
            boolean mismaIdentificacion = actual.getIdentificacion() != null
                    && actual.getIdentificacion().equals(clienteActualizado.getIdentificacion());
            if (mismoId || mismaIdentificacion) {
                clientes.set(i, ClientePersistenciaMapper.toDto(clienteActualizado));
                escribir(datos);
                return;
            }
        }
    }

    @Override
    public synchronized void eliminar(String identificacion) {
        if (identificacion == null) {
            return;
        }
        eliminarSi(c -> identificacion.equals(c.getIdentificacion()));
    }

    @Override
    public synchronized void eliminarPorId(int idCliente) {
        eliminarSi(c -> c.getIdCliente() == idCliente);
    }

    @Override
    public synchronized List<Cliente> listar() {
        List<Cliente> resultado = new ArrayList<>();
        for (ClientePersistenciaDto dto : leer().getClientes()) {
            resultado.add(ClientePersistenciaMapper.toEntity(dto));
        }
        return resultado;
    }

    @Override
    public synchronized Cliente buscar(String identificacion) {
        if (identificacion == null) {
            return null;
        }
        return buscarPrimero(c -> identificacion.equals(c.getIdentificacion()));
    }

    @Override
    public synchronized Cliente buscarPorCorreo(String correo) {
        if (correo == null) {
            return null;
        }
        return buscarPrimero(c -> correo.equalsIgnoreCase(c.getCorreoElectronico()));
    }

    @Override
    public synchronized Cliente buscarPorId(int idCliente) {
        return buscarPrimero(c -> c.getIdCliente() == idCliente);
    }

    // -------------------------- Métodos auxiliares --------------------------

    /** Devuelve el primer cliente que cumpla la condición, o null. */
    private Cliente buscarPrimero(Predicate<ClientePersistenciaDto> condicion) {
        for (ClientePersistenciaDto dto : leer().getClientes()) {
            if (condicion.test(dto)) {
                return ClientePersistenciaMapper.toEntity(dto);
            }
        }
        return null;
    }

    /** Elimina los clientes que cumplan la condición y guarda si hubo cambios. */
    private void eliminarSi(Predicate<ClientePersistenciaDto> condicion) {
        ClienteData datos = leer();
        if (datos.getClientes().removeIf(condicion)) {
            escribir(datos);
        }
    }

    /**
     * Lee y deserializa el archivo de clientes.
     *
     * @return Estructura raíz con los clientes; nunca null
     * @throws PersistenciaException si el archivo no se puede leer o el JSON
     *                              está corrupto
     */
    private ClienteData leer() {
        try {
            if (!Files.exists(rutaArchivo)) {
                return new ClienteData();
            }
            String contenido = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
            if (contenido == null || contenido.trim().isEmpty()) {
                return new ClienteData();
            }
            ClienteData datos = gson.fromJson(contenido, ClienteData.class);
            if (datos == null) {
                return new ClienteData();
            }
            if (datos.getClientes() == null) {
                datos.setClientes(new ArrayList<>());
            }
            return datos;
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el archivo de clientes: "
                    + rutaArchivo, e);
        } catch (JsonParseException e) {
            throw new PersistenciaException("El archivo de clientes contiene un JSON inválido: "
                    + rutaArchivo, e);
        }
    }

    /**
     * Serializa y escribe el archivo de clientes con UTF-8.
     *
     * @param datos Estructura completa a persistir
     * @throws PersistenciaException si ocurre un error de escritura
     */
    private void escribir(ClienteData datos) {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            try (Writer writer = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
                gson.toJson(datos, writer);
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir el archivo de clientes: "
                    + rutaArchivo, e);
        }
    }

    /**
     * Estructura raíz del archivo JSON de clientes. Es privada porque solo
     * este repositorio la necesita.
     */
    private static final class ClienteData {

        private List<ClientePersistenciaDto> clientes = new ArrayList<>();
        private int ultimoId;

        public List<ClientePersistenciaDto> getClientes() {
            return clientes;
        }

        public void setClientes(List<ClientePersistenciaDto> clientes) {
            this.clientes = clientes;
        }

        public int getUltimoId() {
            return ultimoId;
        }

        public void setUltimoId(int ultimoId) {
            this.ultimoId = ultimoId;
        }
    }
}
