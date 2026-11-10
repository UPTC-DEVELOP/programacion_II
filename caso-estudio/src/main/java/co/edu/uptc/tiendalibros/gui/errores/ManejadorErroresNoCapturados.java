//
package co.edu.uptc.tiendalibros.gui.errores;

import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/// Representa un manejador de errores o excepciones no capturadas durante la ejecución de
/// un programa.
/// 
/// Su propósito es ejecutar nuestra propia lógica de gestión de errores que simplemente
/// imprimir en la consola de error.
/// 
/// 
/// La interfaz funcional [Thread.UncaughtExceptionHandler] tiene el método
/// [(uncaughtException Thread t, Throwable
/// e)][Thread.UncaughtExceptionHandler#uncaughtException].
/// 
/// El parámetro `t` es el hilo que falló, y permite obtener su ID, nombre, estado.
/// 
/// El parámetro `e` es la excepción que se lanzó, y contiene el mensaje de error y la
/// traza de la pila de llamadas.
public class ManejadorErroresNoCapturados implements Thread.UncaughtExceptionHandler {
  
  /// Formateador para el identificador del log.
  private static DateTimeFormatter FORMATO_IDENTIFICADOR = DateTimeFormatter.ofPattern("'ERROR'-yyyyMMdd-HHmmss-SSS");
  
  @Override
  public void uncaughtException(Thread hilo, Throwable error) {
    reportarErrorInesperado(
      String.format("Hilo '%s'", hilo.getName()),
      error
    );
  }
  
  /// Registra los detalles del error en el archivo de log, y le muestra al usuario un
  /// mensaje apropiado sin detalles técnicos.
  /// 
  /// El `contexto` y el `error` se escriben en el archivo de log.
  /// 
  /// @param contexto Un texto que describe en dónde ocurrió el error.
  /// @param error    La excepción que ocurrió.
  public static void reportarErrorInesperado(String contexto, Throwable error) {
    
    // crear un identificador para identificar el log
    String identificadorLog = LocalDateTime.now()
      .format(ManejadorErroresNoCapturados.FORMATO_IDENTIFICADOR);
    // crear el mensaje del log
    String mensajeLog = String.format("%s. %s", identificadorLog, contexto);
    // escribir en el logger del programa el mensaje completo del log
    GestorErrores.registrarErrorSevero(ManejadorErroresNoCapturados.class, mensajeLog, error);
    
    // mostrar un mensaje al usuario.
    String mensajeUsuario = String.format(
      "Ocurrió un error interno del programa.%nEs posible que la operación no se haya completado.%nIntente de nuevo. Si el problema continúa, informe a soporte técnico.%nRegistrado en el log como:%n%s.",
      identificadorLog
    );
    ManejadorErroresNoCapturados.mostrarAvisoError(mensajeUsuario);
  }
  
  /// Muestra al usuario un mensaje de error.
  /// 
  /// @param mensaje Mensaje de error que se quiere mostrar al usuario.
  private static void mostrarAvisoError(String mensaje) {
    // toca tener cuidado de si Swing está mostrando una ventana
    
    if (SwingUtilities.isEventDispatchThread()) {
      // el EventDispatchThread (EDT) es el hilo que Swing crea para mostrar la ventana.
      // O sea, Swing ya creó el hilo y está mostrando la interfaz gráfica.
      // La ventana de mensaje se abre en ese hilo de Swing como es usual
      JOptionPane.showMessageDialog(
        obtenerVentanaPadre(),
        mensaje,
        "No se pudo completar la operación",
        JOptionPane.ERROR_MESSAGE
      );
    } else {
      // algo pasó y el error ocurrió en un hilo distinto del EDT de Swing. Por ejemplo, en el
      // hilo de 'main'.
      // Swing solo se debe usar desde el EDT, así que el mensaje debe mostrarse en el EDT
      SwingUtilities.invokeLater(() -> ManejadorErroresNoCapturados.mostrarAvisoError(mensaje));
    }
  }
  
  /// Obtiene la ventana padre.
  private static Window obtenerVentanaPadre() {
    // Obtener la ventana que el usuario tiene activada
    Window activa = KeyboardFocusManager.getCurrentKeyboardFocusManager()
      .getActiveWindow();
    // devolver la ventana activa
    if (activa != null) {
      return activa;
    }
    // o buscar la ventana que se está mostrando
    for (Window ventana : Window.getWindows()) {
      if (ventana.isShowing()) {
        return ventana;
      }
    }
    // no hubo una ventana activa
    return null;
  }
  
}
