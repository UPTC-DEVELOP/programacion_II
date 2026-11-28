package co.edu.uptc.repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.model.Carrito;

public class CarritoJsonRepositorio {

    private static final String RUTA_ARCHIVO = "data/carrito.json";
    private final Gson gson;

    public CarritoJsonRepositorio() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        crearDirectorio();
    }

    private void crearDirectorio() {
        File carpeta = new File("data");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public void guardar(List<Carrito> lista) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar carrito JSON: " + e.getMessage());
        }
    }

    public List<Carrito> cargar() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Carrito>>() {}.getType();
            List<Carrito> resultado = gson.fromJson(reader, tipoLista);
            return (resultado != null) ? resultado : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Aviso: Formato JSON no válido en carrito.json. Se iniciará vacío: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}