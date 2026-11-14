package co.edu.uptc.persistence.txt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.persistence.RegistroOperaciones;
import co.edu.uptc.util.FechaUtil;

// Esta clase agrega una línea por operación y no conoce ninguna regla de negocio.
public class RegistroOperacionesTxt implements RegistroOperaciones {
    private final Path archivo;
    public RegistroOperacionesTxt(Path archivo) {
        this.archivo = archivo;
        try { if (archivo.getParent() != null) {
			Files.createDirectories(archivo.getParent());
		} }
        catch (IOException e) { throw new PersistenciaException("No se pudo preparar el registro.", e); }
    }
    @Override public synchronized void registrar(String nivel, String correo, String operacion) throws PersistenciaException {
        String linea = "[" + FechaUtil.ahora() + "] [" + nivel + "] " + correo + " - " + operacion + System.lineSeparator();
        try { Files.write(archivo, linea.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND); }
        catch (IOException e) { throw new PersistenciaException("No se pudo escribir el registro de operaciones.", e); }
    }
}
