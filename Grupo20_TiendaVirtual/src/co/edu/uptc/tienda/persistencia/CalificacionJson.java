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

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCalificacion;
import co.edu.uptc.tienda.modelo.Calificacion;

public class CalificacionJson implements IPersistenciaCalificacion {

    private final File archivo;
    private final Gson gson;

    public CalificacionJson(String ruta) {
        archivo = new File(ruta);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void guardar(Calificacion calificacion) throws LibroExcepcion {
        List<Calificacion> calificaciones = leer();
        calificaciones.removeIf(guardada -> guardada.esDe(calificacion.getIsbn(), calificacion.getIdCliente()));
        calificaciones.add(calificacion);
        File carpeta = archivo.getParentFile();
        if (carpeta != null) {
            carpeta.mkdirs();
        }
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            escritor.write(gson.toJson(calificaciones));
        } catch (IOException e) {
            throw new LibroExcepcion("No se pudo guardar la calificación en el archivo " + archivo.getPath() + ".");
        }
    }

    @Override
    public List<Calificacion> leer() throws LibroExcepcion {
        List<Calificacion> calificaciones = new ArrayList<>();
        if (!archivo.exists()) {
            return calificaciones;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            Calificacion[] leidas = gson.fromJson(lector, Calificacion[].class);
            if (leidas != null) {
                for (Calificacion calificacion : leidas) {
                    if (calificacion != null) {
                        calificaciones.add(calificacion);
                    }
                }
            }
        } catch (IOException | JsonIOException e) {
            throw new LibroExcepcion("No se pudieron leer las calificaciones del archivo " + archivo.getPath() + ".");
        } catch (JsonSyntaxException e) {
            throw new LibroExcepcion("El archivo " + archivo.getPath() + " no tiene un formato JSON válido.");
        }
        return calificaciones;
    }
}
