//
package co.edu.uptc.tiendalibros.gui.estilos;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/// Representa un almacén que contiene las imágenes de las cubiertas de los libros.
public class AlmacenImagenesCubiertas {
  
  /// Ruta del directorio que contiene las imágenes de las cubiertas de los libros
  public static final Path RUTA_CUBIERTAS = Path.of("cubiertas");
  
  /// Obtiene el directorio donde se guarden las imágenes de las cubiertas de los libros.
  public static Optional<Path> obtenerDirectorioCubiertas() {
    try {
      // intentar crear los directorios padre si es necesario
      Files.createDirectories(AlmacenImagenesCubiertas.RUTA_CUBIERTAS);
      Path rutaAbsoluta = AlmacenImagenesCubiertas.RUTA_CUBIERTAS.toAbsolutePath();
      // devolver la ruta absoluta
      return Optional.of(rutaAbsoluta);
    } catch (IllegalArgumentException | FileSystemNotFoundException | IOException excepcion) {
      System.out.println(
        String.format("Error al buscar el directorio de cubiertas (excepción): %s", excepcion.getMessage())
      );
      return Optional.empty();
    }
  }
}
