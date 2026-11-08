package co.edu.uptc.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import co.edu.uptc.model.Carrito;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCarritoJson implements IRepositorioCarrito {
    private final String ARCHIVO = "carritos.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Carrito> cargar() {
        File file = new File(ARCHIVO);
        if (!file.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(file)) {
            Type t = new TypeToken<ArrayList<Carrito>>(){}.getType();
            List<Carrito> res = gson.fromJson(r, t);
            return res != null ? res : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void guardar(List<Carrito> lista) {
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(lista, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Carrito obtenerCarritoPorCliente(String clienteId) {
        return cargar().stream()
                .filter(c -> c.getClienteId().equals(clienteId))
                .findFirst()
                .orElse(new Carrito(clienteId));
    }

    @Override
    public boolean guardarCarrito(Carrito carrito) {
        List<Carrito> lista = cargar();
        lista.removeIf(c -> c.getClienteId().equals(carrito.getClienteId()));
        lista.add(carrito);
        guardar(lista);
        return true;
    }

    @Override
    public boolean vaciarCarrito(String clienteId) {
        List<Carrito> lista = cargar();
        boolean rem = lista.removeIf(c -> c.getClienteId().equals(clienteId));
        if (rem) guardar(lista);
        return rem;
    }
}