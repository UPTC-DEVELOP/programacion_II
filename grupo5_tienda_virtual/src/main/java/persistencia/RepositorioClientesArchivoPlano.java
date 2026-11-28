package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Cliente;
import model.ClientePremium;
import model.ClienteRegular;

/**
 * Repositorio de clientes en archivo de texto plano (datos/clientes.txt).
 */
public class RepositorioClientesArchivoPlano implements IRepositorio<Cliente> {

    private static final String RUTA_POR_DEFECTO = "datos/clientes.txt";
    private static final String SEPARADOR = "\t";

    private final String rutaArchivo;
    private final List<Cliente> clientes;

    public RepositorioClientesArchivoPlano() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioClientesArchivoPlano(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.clientes = new ArrayList<Cliente>();
        cargar();
    }

    @Override
    public List<Cliente> listar() {
        return clientes;
    }

    @Override
    public void agregar(Cliente elemento) {
        if (elemento != null) {
            clientes.add(elemento);
        }
    }

    @Override
    public boolean eliminar(Cliente elemento) {
        if (elemento == null) {
            return false;
        }
        return clientes.remove(elemento);
    }

    @Override
    public void insertar(Cliente elemento) {
        if (elemento == null) {
            return;
        }
        File archivo = new File(rutaArchivo);
        File carpetaPadre = archivo.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }
        try {
            Files.writeString(archivo.toPath(),
                    formatearLinea(elemento) + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new PersistenciaException(
                    "Error al insertar cliente en " + rutaArchivo, e);
        }
    }

    @Override
    public void actualizar(Cliente elemento) {
        if (elemento == null) {
            return;
        }
        List<Cliente> todos = leerTodos();
        boolean encontrado = false;
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).equals(elemento)) {
                todos.set(i, elemento);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            escribirTodos(todos);
        }
    }

    @Override
    public boolean eliminarPersistente(Cliente elemento) {
        if (elemento == null) {
            return false;
        }
        List<Cliente> todos = leerTodos();
        if (!todos.remove(elemento)) {
            return false;
        }
        escribirTodos(todos);
        return true;
    }

    @Override
    public void cargar() {
        clientes.clear();
        clientes.addAll(leerTodos());
    }

    @Override
    public List<Cliente> listarUltimos(int limite) {
        List<Cliente> todos = leerTodos();
        if (limite <= 0 || todos.isEmpty()) {
            return new ArrayList<Cliente>();
        }
        int desde = Math.max(0, todos.size() - limite);
        List<Cliente> ultimos = new ArrayList<Cliente>(todos.subList(desde, todos.size()));
        Collections.reverse(ultimos);
        return ultimos;
    }

    @Override
    public List<Cliente> listarPagina(int inicio, int cantidad) {
        List<Cliente> todos = leerTodos();
        int desde = Math.max(0, inicio);
        int hasta = Math.min(todos.size(), desde + Math.max(0, cantidad));
        if (desde >= hasta) {
            return new ArrayList<Cliente>();
        }
        return new ArrayList<Cliente>(todos.subList(desde, hasta));
    }

    @Override
    public int contar() {
        return leerTodos().size();
    }

    @Override
    public List<Cliente> buscar(String criterio) {
        List<Cliente> resultado = new ArrayList<Cliente>();
        if (criterio == null || criterio.isBlank()) {
            return resultado;
        }
        for (Cliente cliente : leerTodos()) {
            if (criterio.equalsIgnoreCase(cliente.getCorreo())) {
                resultado.add(cliente);
            }
        }
        return resultado;
    }

    private List<Cliente> leerTodos() {
        List<Cliente> leidos = new ArrayList<Cliente>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return leidos;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                try {
                    leidos.add(parsearLinea(linea));
                } catch (IllegalArgumentException e) {
                    throw new PersistenciaException(
                            "Linea " + numeroLinea + " invalida en " + rutaArchivo, e);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de clientes: " + rutaArchivo, e);
        }
        return leidos;
    }

    /**
     * Convierte una linea con formato: TIPO\tNOMBRE\tCORREO\tCONTRASENA\tDIRECCION\tTELEFONO
     */
    private Cliente parsearLinea(String linea) {
        String[] partes = linea.split(SEPARADOR, -1);
        if (partes.length != 6) {
            throw new IllegalArgumentException("Se esperaban 6 campos y llegaron " + partes.length);
        }

        String tipo = partes[0].trim();
        if (!"PREMIUM".equalsIgnoreCase(tipo) && !"REGULAR".equalsIgnoreCase(tipo)) {
            throw new IllegalArgumentException("Tipo de cliente desconocido: " + tipo);
        }

        String nombre = partes[1];
        String correo = partes[2];
        String contrasena = partes[3];

        Cliente cliente = "PREMIUM".equalsIgnoreCase(tipo)
                ? new ClientePremium(nombre, correo, contrasena)
                : new ClienteRegular(nombre, correo, contrasena);

        if (!partes[4].isEmpty()) {
            cliente.setDireccion(partes[4]);
        }
        if (!partes[5].isEmpty()) {
            cliente.setTelefono(partes[5]);
        }
        return cliente;
    }

    @Override
    public void guardar() {
        escribirTodos(clientes);
    }

    private void escribirTodos(List<Cliente> lista) {
        File archivo = new File(rutaArchivo);
        File carpetaPadre = archivo.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            for (Cliente cliente : lista) {
                writer.write(formatearLinea(cliente));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar el archivo de clientes: " + rutaArchivo, e);
        }
    }

    /**
     * Serializa un cliente en una linea tabulada compatible con parsearLinea().
     */
    private String formatearLinea(Cliente cliente) {
        String tipo = (cliente instanceof ClientePremium) ? "PREMIUM" : "REGULAR";
        return String.join(SEPARADOR,
                tipo,
                limpiarCampo(cliente.getNombreCompleto()),
                limpiarCampo(cliente.getCorreo()),
                limpiarCampo(cliente.getContrasena()),
                limpiarCampo(cliente.getDireccion()),
                limpiarCampo(cliente.getTelefono()));
    }

    private String limpiarCampo(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\t", " ").replace("\r", " ").replace("\n", " ");
    }
}
