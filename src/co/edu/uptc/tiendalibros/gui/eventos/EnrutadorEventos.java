//
package co.edu.uptc.tiendalibros.gui.eventos;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import co.edu.uptc.tiendalibros.interfaces.IReceptorEventos;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa un objeto que enruta o asocia eventos con sus manejadores.
public class EnrutadorEventos implements IReceptorEventos {
  
  /// Objeto que registra los errores provocados por excepciones.
  private static final Logger LOGGER = Logger.getLogger(EnrutadorEventos.class.getName());
  
  /// Contenedor que asocia un evento o ruta con su función manejadora.
  private final Map<String, ManejadorEvento> rutasEventos;
  
  /// Inicializa un nuevo objeto enrutador de eventos.
  public EnrutadorEventos() {
    this.rutasEventos = new HashMap<String, ManejadorEvento>();
  }
  
  /// Registra el evento especificado con una función para manejar el evento cuando suceda.
  ///
  /// @param evento                 Evento o ruta que se quiere manejar.
  /// @param funcionManejadorEvento Función que manejará el evento.
  public void registrarEvento(String evento, ManejadorEvento funcionManejadorEvento) {
    // sólo se deben registrar eventos o rutas únicas para evitar llamar a manejadores
    // incorrectos
    if (this.rutasEventos.containsKey(evento)) {
      
      // se aplica el concepto de excepción cuando el estado de un objeto es incorrecto
      
      throw new IllegalStateException(String.format("El evento \"%s\" ya está registrado", evento));
    } else {
      // no se ha registrado el evento antes, entonces registrarlo: se asocia el evento o ruta
      // con la función que manejerá el evento cuando suceda.
      this.rutasEventos.put(evento, funcionManejadorEvento);
      
    }
  }
  
  /// Maneja el evento especificado para el elemento dado. Internamente, se invoca a la
  /// función registrada que manejará el evento.
  ///
  /// @param evento   Evento o ruta que se quiere manejar.
  /// @param elemento Objeto que se quiere enviar junto al evento.
  @Override
  public void manejarEvento(String evento, Object elemento) {
    // primero, obtener la función asociada al evento
    ManejadorEvento funcion = this.rutasEventos.get(evento);
    // segundo, verificar que la función exista, que no sea nula
    if (funcion == null) {
      // TODO: no debería pasar que el evento no está registrado, por ahora sólo mostrar que el
      // evento no está disponible.
      String mensaje = String.format("Opción No Disponible.%nEl evento \"%s\" no está registrado.", evento);
      JOptionPane.showMessageDialog(null, mensaje);
      return;
    }
    
    try {
      // tercero, invocar la función manejadora enviando el elemento que se quiere manejar
      funcion.aceptar(elemento);
    } catch (ErrorTecnicoSistemaException error) {
      // registrar los errores técnicos del sistema manejados por el programa de la tienda
      LOGGER.log(Level.SEVERE, String.format("Error técnico al manejar el evento: %s", evento), error);
      notificarErrorSistema();
    } catch (RuntimeException error) {
      // registrar los demás errores técnicos del sistema
      LOGGER.log(Level.SEVERE, String.format("Error inesperado al manejar el evento: %s", evento), error);
      notificarErrorSistema();
    }
  }
  
  /// Maneja el evento especificado sin un elemento. Es decir, que para manejar el evento no
  /// es necesario enviar un elemento. Internamente, se invoca a la función registrada que
  /// manejará el evento.
  ///
  /// @param evento Evento o ruta que se quiere manejar.
  public void manejarEvento(String evento) {
    // manejar el evento pero sin enviar un objeto
    manejarEvento(evento, null);
  }
  
  private void notificarErrorSistema() {
    JOptionPane.showMessageDialog(
      null,
      String.format(
        "Ocurrió un problema inesperado en el sistema. La operación no se completó.%nSi este problema persisten, por favor, contacte a soporte técnico."
      ),
      "Error Técnico del Sistema",
      JOptionPane.ERROR_MESSAGE
    );
  }
}
