package co.edu.uptc.libreria.persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.edu.uptc.libreria.interfaces.IRegistroOperaciones;

/** Guarda una linea por operacion en un archivo de texto (modo agregar). */
public class ArchivoRegistroOperaciones implements IRegistroOperaciones {

	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private Path archivo;

	public ArchivoRegistroOperaciones(String ruta) {
		super();
		this.archivo = Paths.get(ruta);
	}

	@Override
	public void registrar(String operacion, String detalle) {
		String linea = LocalDateTime.now().format(FORMATO) + " | " + operacion + " | " + detalle
				+ System.lineSeparator();
		try {
			Path carpeta = archivo.toAbsolutePath().getParent();
			if (carpeta != null) {
				Files.createDirectories(carpeta);
			}
			Files.writeString(archivo, linea, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			// un fallo al escribir el registro no debe impedir la operacion del usuario
			System.err.println("No se pudo escribir el registro de operaciones: " + e.getMessage());
		}
	}

}