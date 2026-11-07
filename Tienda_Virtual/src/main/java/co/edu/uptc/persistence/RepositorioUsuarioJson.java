package main.java.co.edu.uptc.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.java.co.edu.uptc.model.Cliente;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuarioJson implements IRepositorioUsuario {
    private final String ARCHIVO = "usuarios.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Cliente> cargar() {
        File file = new File(ARCHIVO);
        if (!file.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(file)) {
            Type t = new TypeToken<ArrayList<Cliente>>(){}.getType();
            List<Cliente> res = gson.fromJson(r, t);
            return res != null ? res : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void guardar(List<Cliente> lista) {
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(lista, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean guardarUsuario(Cliente cliente) {
        List<Cliente> lista = cargar();
        lista.add(cliente);
        guardar(lista);
        return true;
    }

    @Override
    public boolean actualizarUsuario(Cliente cliente) {
        List<Cliente> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(cliente.getId())) {
                lista.set(i, cliente);
                guardar(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminarUsuario(String id) {
        List<Cliente> lista = cargar();
        boolean rem = lista.removeIf(c -> c.getId().equals(id));
        if (rem) guardar(lista);
        return rem;
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        return cargar().stream().filter(c -> c.getCorreo().equalsIgnoreCase(correo)).findFirst().orElse(null);
    }

    @Override
    public Cliente buscarPorId(String id) {
        return cargar().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return cargar();
    }
}