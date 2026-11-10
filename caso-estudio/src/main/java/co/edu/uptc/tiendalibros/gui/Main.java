//
package co.edu.uptc.tiendalibros.gui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import co.edu.uptc.tiendalibros.gui.errores.GestorErrores;
import co.edu.uptc.tiendalibros.gui.errores.ManejadorErroresNoCapturados;
import co.edu.uptc.tiendalibros.gui.estilos.Fuentes;
import co.edu.uptc.tiendalibros.gui.estilos.IconoImagen;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;
import co.edu.uptc.tiendalibros.persistencia.jdbc.ConexionBaseDatos;
import co.edu.uptc.tiendalibros.persistencia.jdbc.ConfiguracionBaseDatos;

/// Representa el punto inicial para ejecutar el programa de la tienda de libros.
public class Main {
  
  /// Ejecuta el programa de la tienda de libros.
  public static void main(String[] args) {
    // antes de iniciar a ejecutar el programa, se debe configurar el registro de los errores
    // log
    // - configurar el LOGGER y el archivo de log
    GestorErrores.configurar();
    // - establecer el manejador global para las excepciones no capturadas en cualquier hilo
    Thread.setDefaultUncaughtExceptionHandler(new ManejadorErroresNoCapturados());
    // - escribir el primer mensaje cuando se ejecuta el programa
    GestorErrores.registrarInfo(Main.class, "Programa de Tienda de Libros Iniciado");
    
    // cargar las fuentes y los íconos antes de crear la ventana
    Fuentes.precargar();
    IconoImagen.precargar();
    
    // probar la conexión con la base de datos
    ConexionBaseDatos conexionBaseDatos;
    
    try {
      // cargar las opciones de conexión con la base de datos
      ConfiguracionBaseDatos configuracionBaseDatos = ConfiguracionBaseDatos
        .cargar(ConfiguracionBaseDatos.RUTA_PREDETERMINADA);
      
      // crear la conexión con la base de datos
      conexionBaseDatos = new ConexionBaseDatos(configuracionBaseDatos);
      
      // mostrar el mensaje de que se pudo conectar exitosamente a la base de datos
      String mensajeConexion = conexionBaseDatos.probar();
      // registar la conexión exitosa con la base de datos en el archivo log
      GestorErrores.registrarInfo(Main.class, "Conexión Exitosa con la Base de datos");
      
      JOptionPane.showMessageDialog(
        null,
        mensajeConexion,
        "Conexión Exitosa",
        JOptionPane.INFORMATION_MESSAGE
      );
      
    } catch (ErrorTecnicoSistemaException excepcion) {
      // registrar el error en el archivo de log
      GestorErrores.registrarErrorSevero(Main.class, "No se pudo iniciar la aplicación", excepcion);
      // mostrar la causa del error de conexión con la base de datos
      String motivo = excepcion.getCause() == null ? ""
        : excepcion.getCause()
          .getLocalizedMessage();
      String mensajeError = String.format("%s%n%s", excepcion.getMessage(), motivo);
      
      JOptionPane.showMessageDialog(
        null,
        mensajeError,
        "Conexión Fallida",
        JOptionPane.ERROR_MESSAGE
      );
      
      return;
    }
    
    // mostrar la ventana en el hilo o proceso propio de Swing
    SwingUtilities.invokeLater(() -> {
      // crear la ventana principal
      VentanaPrincipal ventana = new VentanaPrincipal();
      // pedirle a la aplicación que inicialice componentes y que arranque la aplicación
      new Aplicacion(ventana, conexionBaseDatos).iniciar();
    });
    
  }
  
}
