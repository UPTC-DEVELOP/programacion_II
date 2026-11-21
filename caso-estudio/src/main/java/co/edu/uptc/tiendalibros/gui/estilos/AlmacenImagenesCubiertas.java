//
package co.edu.uptc.tiendalibros.gui.estilos;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

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
  
  /// Carga la imagen de una cubierta y la escala según el tamaño especificado y conserva su
  /// proporción.
  /// 
  /// @param nombreArchivo Nombre del archivo dentro del directorio de cubiertas.
  /// @param anchoMaximo   Ancho máximo del icono medido en píxeles.
  /// @param altoMaximo    Alto máximo del icono medido en píxeles.
  /// @return Devuelve un icono si el archivo existe y es una imagen válida. O vacío, en caso
  ///         de fallar al cargar y redimensionar la imagen.
  public static Optional<ImageIcon> cargarCubierta(String nombreArchivo, int anchoMaximo, int altoMaximo) {
    // sin el nombre de archivo, no hay nada que cargar
    if (nombreArchivo == null || nombreArchivo.isBlank()) {
      return Optional.empty();
    }
    
    // obtener el directorio donde las cubiertas están almacendas
    Optional<Path> directorio = AlmacenImagenesCubiertas.obtenerDirectorioCubiertas();
    
    // si no se encontró el directorio, no hay nada que cargar
    if (directorio.isEmpty()) {
      return Optional.empty();
    }
    
    // 
    try {
      // obtener la ruta completa de la imagen: directorio + nombreArchivo
      Path rutaImagen = directorio.get().resolve(nombreArchivo);
      // leer el archivo de imagen
      BufferedImage imagenOriginal = ImageIO.read(rutaImagen.toFile());
      
      // si el archivo leído no es una imagen soportada, tampoco se carga nada
      if (imagenOriginal == null) {
        return Optional.empty();
      }
      // aplicar esta fórmula conocida para ajustar el tamaño de una imagen sin que se deforme
      // calcular cuál dimensión (ancho o alto) tiene la menor proporción: (máximo / original)
      double escala = Math.min(
        (double) anchoMaximo / imagenOriginal.getWidth(),
        (double) altoMaximo / imagenOriginal.getHeight()
      );
      
      // calcular el ancho en píxeles redondeando la multiplicación de la dimensión por la escala.
      // Mínimo 1 píxel
      int ancho = Math.max(1, (int) Math.round(imagenOriginal.getWidth() * escala));
      int alto = Math.max(1, (int) Math.round(imagenOriginal.getHeight() * escala));
      
      // crear una nueva imagen con los nuevos ancho y alto, y un escalado fiel 
      Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
      // devolver el nuevo ícono
      return Optional.of(new ImageIcon(imagenEscalada));
    } catch (IllegalArgumentException | IOException excepcion) {
      System.out.println(
        String.format("Error al cargar la cubierta '%s' (excepción): %s", nombreArchivo, excepcion.getMessage())
      );
      return Optional.empty();
    }
  }
}
