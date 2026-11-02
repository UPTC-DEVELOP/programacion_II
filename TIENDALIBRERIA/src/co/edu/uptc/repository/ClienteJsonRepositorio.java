package co.edu.uptc.repository;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.model.Cliente;

public class ClienteJsonRepositorio implements IClienteRepositorio {

    private final Path archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type tipoLista = new TypeToken<List<Cliente>>() {}.getType();
    private final List<Cliente> clientes;

    public ClienteJsonRepositorio(String ruta) {
        this.archivo = Path.of(ruta);
        this.clientes = cargar();
    }

    private List<Cliente> cargar() {
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            List<Cliente> lista = gson.fromJson(lector, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + archivo, e);
        }
    }

    private void guardarArchivo() {
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            try (Writer escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(clientes, tipoLista, escritor);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir " + archivo, e);
        }
    }

    @Override
    public void guardar(Cliente cliente) {
        clientes.add(cliente);
        guardarArchivo();
    }
    @Override
    public void actualizar(Cliente cliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCedula().equals(cliente.getCedula())) {
                clientes.set(i, cliente);
                guardarArchivo();
                return;
            }
        }
    }

    @Override
    public void eliminar(String cedula) {
        boolean eliminado = clientes.removeIf(c -> c.getCedula().equals(cedula));
        if (eliminado) {
            guardarArchivo();
        }
    }
   
    @Override
    public List<Cliente> listar() {
        return clientes;
    }
}