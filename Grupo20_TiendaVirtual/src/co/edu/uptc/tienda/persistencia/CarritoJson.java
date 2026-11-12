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

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCarrito;
import co.edu.uptc.tienda.modelo.RegistroCarrito;

public class CarritoJson implements IPersistenciaCarrito {

    private final File archivo;
    private final Gson gson;

    public CarritoJson(String ruta) {
        archivo = new File(ruta);
        gson = new Gson();
    }

    @Override
    public void guardar(List<RegistroCarrito> registros) throws CarritoExcepcion {
        File carpeta = archivo.getParentFile();
        if (carpeta != null) {
            carpeta.mkdirs();
        }
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            escritor.write(gson.toJson(registros));
        } catch (IOException e) {
            throw new CarritoExcepcion("No se pudo guardar el carrito en el archivo " + archivo.getPath() + ".");
        }
    }

    @Override
    public List<RegistroCarrito> leer() throws CarritoExcepcion {
        List<RegistroCarrito> registros = new ArrayList<>();
        if (!archivo.exists()) {
            return registros;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String texto = "";
            String linea = lector.readLine();
            while (linea != null) {
                texto += linea;
                linea = lector.readLine();
            }
            RegistroCarrito[] leidos = gson.fromJson(texto, RegistroCarrito[].class);
            if (leidos != null) {
                for (RegistroCarrito registro : leidos) {
                    if (registro != null) {
                        registros.add(registro);
                    }
                }
            }
        } catch (IOException e) {
            throw new CarritoExcepcion("No se pudo leer el carrito guardado en el archivo " + archivo.getPath() + ".");
        } catch (JsonSyntaxException e) {
            throw new CarritoExcepcion("El archivo " + archivo.getPath() + " no tiene un formato JSON válido.");
        }
        return registros;
    }
}
