package persistencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    public void cargar() {
        clientes.clear();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                Cliente cliente = parsearLinea(linea);
                if (cliente != null) {
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de clientes: " + rutaArchivo, e);
        }
    }

    /**
     * Convierte una linea con formato: TIPO\tNOMBRE\tCORREO\tCONTRASENA\tDIRECCION\tTELEFONO
     */
    private Cliente parsearLinea(String linea) {
        String[] partes = linea.split(SEPARADOR, -1);
        if (partes.length < 4) {
            return null;
        }

        String tipo = partes[0].trim();
        String nombre = partes[1];
        String correo = partes[2];
        String contrasena = partes[3];

        Cliente cliente = "PREMIUM".equalsIgnoreCase(tipo)
                ? new ClientePremium(nombre, correo, contrasena)
                : new ClienteRegular(nombre, correo, contrasena);

        if (partes.length > 4 && !partes[4].isEmpty()) {
            cliente.setDireccion(partes[4]);
        }
        if (partes.length > 5 && !partes[5].isEmpty()) {
            cliente.setTelefono(partes[5]);
        }
        return cliente;
    }

    @Override
    public void guardar() {
        // Implementacion en C08
    }
}
