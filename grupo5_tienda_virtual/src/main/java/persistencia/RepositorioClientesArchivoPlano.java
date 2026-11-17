package persistencia;

import java.util.ArrayList;
import java.util.List;

import model.Cliente;

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
    	
        // Implementacion en C07
    }

    @Override
    public void guardar() {
    	
        // Implementacion en C08
    }
}
