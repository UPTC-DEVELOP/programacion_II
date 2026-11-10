//
package co.edu.uptc.tiendalibros.gui.errores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/// Representa un gestor que configura el registro (log) de la aplicación en un archivo de
/// texto.
public class GestorErrores {
  
  /// Registrador de los mensajes log que se escriben mediante esta clase.
  private static final Logger LOGGER = Logger.getLogger(GestorErrores.class.getName());
  
  /// Directorio donde se guardan los archivos log.
  private static final Path DIRECTORIO_LOGS = Path.of("logs");
  
  /// Patrón que se usa para el nombre del archivo log.
  private static final String PATRON_ARCHIVO = "logs/tienda-%g.log";
  
  /// Indicar si el gestor de errores ya está configurado.
  private static boolean configurado = false;
  
  private GestorErrores() {
  }
  
  /// Configura el gestor de errores en el hilo.
  public static synchronized void configurar() {
    if (GestorErrores.configurado) {
      return;
    }
    GestorErrores.configurado = true;
    
    // Formato para el SimpleFormatter produciendo la salida: fecha hora NIVEL clase -
    // mensaje. La sintaxis es complicada :/
    // %1$tF : Fecha (AAAA-MM-DD)
    // %1$tT : Hora (HH:MM:SS)
    // %4$-7s : Nivel del log y alineación a 7 caracteres
    // %3$s : Nombre de la clase o del logger
    // %5$s : Mensaje principal del log
    // %6$s : Excepción o pila Stacktrace
    // %n : Salto de línea
    System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$-7s %3$s - %5$s%6$s%n");
    
    /// Objeto que registra mensajes en el archivo de log
    Logger padre = Logger.getLogger("");
    
    try {
      // crear los directorios padre si es necesario
      Files.createDirectories(GestorErrores.DIRECTORIO_LOGS);
      
      // parámetros FileHandler:
      // - pattern: patrón del nombre del archivo de log
      // - limit: tamaño máximo del archivo de log en bytes
      // - count: cantidad de archivos de log que se manejarán
      // - append: escribir al final del archivo
      
      // tamaño máximo del archivo: 1 MB = 1 millón de bytes
      int tamano = 1_000_000;
      int cantidad = 3;
      // crear el archivo de log
      FileHandler archivo = new FileHandler(GestorErrores.PATRON_ARCHIVO, tamano, cantidad, true);
      archivo.setEncoding("UTF-8");
      archivo.setFormatter(new SimpleFormatter());
      padre.addHandler(archivo);
    } catch (IOException excepcion) {
      // si no se pudo crear el archivo de log, se notifica al usuario
      padre.log(
        Level.WARNING,
        "No se pudo crear el archivo de registro log. Solo habrá registro en consola.",
        excepcion
      );
    }
  }
  
  /// Registra un mensaje de error con su pila completa y sus causas.
  /// 
  /// @param mensaje Mensaje de error describiendo lo que ocurrió.
  /// @param error   Excepción que causó el problema.
  public static void registrarErrorSevero(String mensaje, Throwable error) {
    GestorErrores.LOGGER.log(Level.SEVERE, mensaje, error);
  }
  
  /// Registra un mensaje de advertencia acerca de una situación anómala de la que el
  /// programa se pudo recuperar.
  /// 
  /// @param mensaje Mensaje de error describiendo lo que ocurrió.
  /// @param error   Excepción que causó el problema.
  public static void registrarAdvertencia(String mensaje) {
    GestorErrores.LOGGER.log(Level.WARNING, mensaje);
  }
  
  /// Registra un mensaje informativo sobre hecho normal importante.
  /// 
  /// @param mensaje Mensaje de error describiendo lo que ocurrió.
  /// @param error   Excepción que causó el problema.
  public static void registrarInfo(String mensaje) {
    GestorErrores.LOGGER.log(Level.INFO, mensaje);
  }
  
  /// Registra un mensaje de error con su pila completa y sus causas.
  /// 
  /// @param origen  Clase desde se registra el mensaje.
  /// @param mensaje Mensaje de error describiendo lo que ocurrió.
  /// @param error   Excepción que causó el problema.
  public static void registrarErrorSevero(Class<?> origen, String mensaje, Throwable error) {
    Logger.getLogger(origen.getName())
      .log(Level.SEVERE, mensaje, error);
  }
  
  /// Registra un mensaje de advertencia acerca de una situación anómala de la que el
  /// programa se pudo recuperar.
  /// 
  /// @param origen  Clase desde se registra el mensaje.
  /// @param mensaje Mensaje de error describiendo lo que ocurrió.
  /// @param error   Excepción que causó el problema.
  public static void registrarAdvertencia(Class<?> origen, String mensaje) {
    Logger.getLogger(origen.getName())
      .log(Level.WARNING, mensaje);
  }
  
  /// Registra un mensaje informativo sobre hecho normal importante.
  /// 
  /// @param origen  Clase desde se registra el mensaje.
  /// @param mensaje Mensaje de error describiendo lo que ocurrió.
  /// @param error   Excepción que causó el problema.
  public static void registrarInfo(Class<?> origen, String mensaje) {
    Logger.getLogger(origen.getName())
      .log(Level.INFO, mensaje);
  }
    
}
