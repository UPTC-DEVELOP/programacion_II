package co.uptc.edu.caso.estudio.persistencia;

import co.uptc.edu.caso.estudio.negocio.Cliente;
import co.uptc.edu.caso.estudio.negocio.Compra;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GestorJson {

    private static final String ARCHIVO_CLIENTES = "clientes.json";
    private static final String ARCHIVO_COMPRAS = "compras.json";
    private final Gson gson;

    public GestorJson() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    public void guardarClientes(List<Cliente> clientes) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(ARCHIVO_CLIENTES), StandardCharsets.UTF_8)) {
            gson.toJson(clientes, writer);
        } catch (IOException e) {
            System.err.println("Error al escribir clientes.json: " + e.getMessage());
        }
    }

    public List<Cliente> leerClientes() {
        File archivo = new File(ARCHIVO_CLIENTES);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            Type tipoLista = new TypeToken<ArrayList<Cliente>>() {}.getType();
            List<Cliente> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer clientes.json: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarCompras(List<Compra> compras) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(ARCHIVO_COMPRAS), StandardCharsets.UTF_8)) {
            gson.toJson(compras, writer);
        } catch (IOException e) {
            System.err.println("Error al escribir compras.json: " + e.getMessage());
        }
    }

    public List<Compra> leerCompras() {
        File archivo = new File(ARCHIVO_COMPRAS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            Type tipoLista = new TypeToken<ArrayList<Compra>>() {}.getType();
            List<Compra> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer compras.json: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
