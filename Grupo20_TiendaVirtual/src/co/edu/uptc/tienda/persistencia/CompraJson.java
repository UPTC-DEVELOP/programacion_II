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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCompra;
import co.edu.uptc.tienda.modelo.Compra;

public class CompraJson implements IPersistenciaCompra {

    private final File archivo;
    private final Gson gson;

    public CompraJson(String ruta) {
        archivo = new File(ruta);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void guardar(Compra compra) throws CompraExcepcion {
        List<Compra> compras = leer();
        for (Compra guardada : compras) {
            if (guardada.getNumero() == compra.getNumero()) {
                return;
            }
        }
        compras.add(compra);
        File carpeta = archivo.getParentFile();
        if (carpeta != null) {
            carpeta.mkdirs();
        }
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            escritor.write(gson.toJson(compras));
        } catch (IOException e) {
            throw new CompraExcepcion("No se pudo guardar la compra en el archivo " + archivo.getPath() + ".");
        }
    }

    @Override
    public List<Compra> leer() throws CompraExcepcion {
        List<Compra> compras = new ArrayList<>();
        if (!archivo.exists()) {
            return compras;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            Compra[] leidas = gson.fromJson(lector, Compra[].class);
            if (leidas != null) {
                for (Compra compra : leidas) {
                    if (compra != null) {
                        compras.add(compra);
                    }
                }
            }
        } catch (IOException | JsonIOException e) {
            throw new CompraExcepcion("No se pudieron leer las compras del archivo " + archivo.getPath() + ".");
        } catch (JsonSyntaxException e) {
            throw new CompraExcepcion("El archivo " + archivo.getPath() + " no tiene un formato JSON válido.");
        }
        return compras;
    }
}
