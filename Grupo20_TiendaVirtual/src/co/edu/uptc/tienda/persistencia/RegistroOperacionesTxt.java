package co.edu.uptc.tienda.persistencia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.edu.uptc.tienda.interfaz.IRegistroOperaciones;

public class RegistroOperacionesTxt implements IRegistroOperaciones {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final File archivo;

    public RegistroOperacionesTxt(String ruta) {
        archivo = new File(ruta);
    }

    @Override
    public synchronized void registrar(String modulo, String operacion, String detalle) {
        File carpeta = archivo.getParentFile();
        if (carpeta != null) {
            carpeta.mkdirs();
        }
        String linea = LocalDateTime.now().format(FORMATO) + " | " + modulo + " | " + operacion + " | "
                + detalle.replace("\n", " ");
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, true))) {
            escritor.write(linea);
            escritor.newLine();
        } catch (IOException e) {
            System.out.println("No se pudo escribir en el registro de operaciones " + archivo.getPath() + ".");
        }
    }
}
