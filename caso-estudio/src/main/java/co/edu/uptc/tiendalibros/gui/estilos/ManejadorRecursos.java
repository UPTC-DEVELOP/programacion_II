//
package co.edu.uptc.tiendalibros.gui.estilos;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.ImageIcon;

/// Maneja los recursos que se necesitan para la aplicación.
public class ManejadorRecursos {
  
  /// Colección con las fuentes (Fonts) personalizadas que se cargan desde el disco. Evitar
  /// repetir la carga archivos .ttf
  private static final Map<String, Font> FUENTES = new HashMap<>();
  
  /// Ruta del directorio que contiene los archivos de las fuentes para la tipografía.
  private static final String RUTA_FUENTES = "/fuentes";
  
  /// Ruta del directorio que contiene los archivos de imagen de los íconos.
  private static final String RUTA_ICONOS = "/iconos";
  
  
  /// Crea un objeto [ManejadorRecursos]
  private ManejadorRecursos() {
  }
  
  /// Obtiene el ícono especificado
  public static Optional<ImageIcon> obtenerIcono(String nombreArchivo) {
    return obtenerIcono(nombreArchivo, 16, 16);
  }
  
  /// Obtiene el ícono especificado.
  public static Optional<ImageIcon> obtenerIcono(String nombreArchivo, int ancho, int alto) {
    String rutaArchivo = String.join("/", ManejadorRecursos.RUTA_ICONOS, nombreArchivo);
    
    // cualquier error puede ocurrir al cargar el archivo de imagen del ícono
    try (InputStream flujo = ManejadorRecursos.class.getResourceAsStream(rutaArchivo)) {
      // si el objeto flujo es nulo, significa que no se pudo cargar el archivo
      if (flujo == null) {
        System.out.println(String.format("Error al cargar la imagen el ícono '%s' (flujo nulo)", nombreArchivo));
        return Optional.empty();
      }
      // construir propiamente el ícono
      ImageIcon icono = new ImageIcon(flujo.readAllBytes());
      Image imagenIcono = icono.getImage()
        .getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
      return Optional.of(new ImageIcon(imagenIcono));
      
    } catch (IllegalArgumentException | OutOfMemoryError | IOException excepcion) {
      System.out
        .println(
          String
            .format("Error al cargar la imagen del ícono '%s' (excepción): %s", nombreArchivo, excepcion.getMessage())
        );
      
      return Optional.empty();
    }
  }
  
  /// Obtiene la fuente especificada. La fuente se carga desde el disco una sola vez para
  /// ser reutilizada.
  ///
  /// Si la fuente especificada no puede ser cargada, se reemplaza por la fuente
  /// predeterminada.
  ///
  /// @param nombreDirectorio Directorio del computador donde están los archivos .ttf de la
  ///                         fuente.
  /// @param nombreArchivo    Nombre del archivo de la fuente.
  /// @param estilo           Estilo de la fuente [Font].
  /// @param tamano           Tamaño de la fuente.
  public static Font obtenerFuente(String nombreDirectorio, String nombreArchivo, int estilo, float tamano) {
    // tengo la carpeta de fuentes organizada en esta forma: directorioFuente -> archivoFuente
    
    String rutaArchivo = String.join("/", ManejadorRecursos.RUTA_FUENTES, nombreDirectorio, nombreArchivo);
    
    // buscar si la colección de fuentes ya tiene cargada la fuente especificada
    Font fuente = ManejadorRecursos.FUENTES.get(rutaArchivo);
    
    if (fuente == null) {
      // no se encontró la fuente en la colección. Intentar cargar la fuente
      Optional<Font> fuenteCargada = cargarFuente(rutaArchivo, nombreArchivo);
      
      if (fuenteCargada.isEmpty()) {
        // la fuente no se encontró en el disco. Retornar la fuente predeterminada
        return obtenerFuentePredeterminada();
      }
      
      // guardar la fuente cargada en la colección de fuentes
      fuente = fuenteCargada.get();
      ManejadorRecursos.FUENTES.put(rutaArchivo, fuente);
    }
    // retornar la fuente solicitada
    return fuente.deriveFont(estilo, tamano);
  }
  
  private static Optional<Font> cargarFuente(String rutaArchivo, String nombreArchivo) {
    
    // cualquier error puede ocurrir al cargar el archivo de fuente
    try (InputStream flujo = ManejadorRecursos.class.getResourceAsStream(rutaArchivo)) {
      // si el objeto flujo es nulo, significa que no se pudo cargar el archivo
      if (flujo == null) {
        System.out.println(String.format("Error al cargar la fuente '%s' (flujo nulo)", nombreArchivo));
        return Optional.empty();
      }
      // configurar los detalles de la fuente encontrada y retornarla
      return Optional.of(Font.createFont(Font.TRUETYPE_FONT, flujo));
    } catch (FontFormatException | IOException excepcion) {
      System.out.println(
        String.format("Error al cargar la fuente '%s' (excepción): %s", nombreArchivo, excepcion.getMessage())
      );
      return Optional.empty();
    }
  }
  
  /// Obtiene la fuente predeterminada si falla cargar la fuente personalizada.
  private static Font obtenerFuentePredeterminada() {
    return new Font(Font.SANS_SERIF, Font.PLAIN, 12);
  }
  
  /// Obtiene la fuente base.
  public static Font obtenerFuenteBase() {
    return obtenerFuente("arvo", "Arvo-Regular.ttf", Font.PLAIN, 23f);
  }
  

  
}
