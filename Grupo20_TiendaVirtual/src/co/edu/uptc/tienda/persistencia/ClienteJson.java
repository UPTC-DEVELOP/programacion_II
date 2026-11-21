package co.edu.uptc.tienda.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import co.edu.uptc.tienda.excepcion.ClienteExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCliente;
import co.edu.uptc.tienda.modelo.Cliente;

public class ClienteJson implements IPersistenciaCliente {

    private final File archivo;
    private final Gson gson;

    public ClienteJson(String ruta) {
        archivo = new File(ruta);
        gson = new Gson();
    }

    @Override
    public void guardar(List<Cliente> clientes) throws ClienteExcepcion {
        File carpeta = archivo.getParentFile();
        if (carpeta != null) {
            carpeta.mkdirs();
        }
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            escritor.write(gson.toJson(clientes));
        } catch (IOException e) {
            throw new ClienteExcepcion("No se pudieron guardar los clientes en el archivo " + archivo.getPath() + ".");
        }
    }

    @Override
    public List<Cliente> leer() throws ClienteExcepcion {
        List<Cliente> clientes = new ArrayList<>();
        if (!archivo.exists()) {
            return clientes;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String texto = "";
            String linea = lector.readLine();
            while (linea != null) {
                texto += linea;
                linea = lector.readLine();
            }
            Cliente[] leidos = gson.fromJson(texto, Cliente[].class);
            if (leidos != null) {
                for (Cliente cliente : leidos) {
                    if (cliente != null) {
                        clientes.add(cliente);
                    }
                }
            }
        } catch (IOException e) {
            throw new ClienteExcepcion("No se pudieron leer los clientes del archivo " + archivo.getPath() + ".");
        } catch (JsonSyntaxException e) {
            throw new ClienteExcepcion("El archivo " + archivo.getPath() + " no tiene un formato JSON válido.");
        }
        return clientes;
    }
}
