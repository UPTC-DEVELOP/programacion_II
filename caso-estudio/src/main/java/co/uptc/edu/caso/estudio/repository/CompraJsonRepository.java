package co.uptc.edu.caso.estudio.repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.uptc.edu.caso.estudio.negocio.Compra;

public class CompraJsonRepository implements CompraRepository {

    private static final String ARCHIVO = "datos/compras.json";
    private Gson gson;

    public CompraJsonRepository() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    @Override
    public void guardar(Compra compra) {
        List<Compra> compras = listar();
        compras.add(compra);
        guardarLista(compras);
    }

    @Override
    public List<Compra> listar() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<List<Compra>>() {}.getType();
            List<Compra> compras = gson.fromJson(reader, tipoLista);
            return compras != null ? compras : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Error al leer compras JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Compra buscarPorId(int idCompra) {
        for (Compra c : listar()) {
            if (c.getIdCompra() == idCompra) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Compra compra) {
        List<Compra> compras = listar();
        for (int i = 0; i < compras.size(); i++) {
            if (compras.get(i).getIdCompra() == compra.getIdCompra()) {
                compras.set(i, compra);
                guardarLista(compras);
                return;
            }
        }
    }

    @Override
    public void eliminar(int idCompra) {
        List<Compra> compras = listar();
        for (int i = 0; i < compras.size(); i++) {
            if (compras.get(i).getIdCompra() == idCompra) {
                compras.remove(i);
                guardarLista(compras);
                return;
            }
        }
    }

    private void guardarLista(List<Compra> compras) {
        try {
            File archivo = new File(ARCHIVO);
            File carpeta = archivo.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            try (FileWriter writer = new FileWriter(archivo)) {
                gson.toJson(compras, writer);
            }
        } catch (Exception e) {
            System.out.println("Error al guardar compras JSON: " + e.getMessage());
        }
    }
}
