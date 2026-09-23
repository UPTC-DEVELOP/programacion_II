//
package main.co.edu.uptc.fesad.tpsi.tienda.controladores;

import java.util.HashMap;
import java.util.Map;

/// Representa un objeto que enruta o asocia eventos con sus manejadores.
public class EnrutadorEventos {
  
  /// Contenedor que asocia un evento o ruta con su función manejadora.
  private final Map<String, ManejadorEvento> rutasEventos;
  
  /// Inicializa un nuevo objeto enrutador de eventos.
  public EnrutadorEventos() {
    this.rutasEventos = new HashMap<String, ManejadorEvento>();
  }
  
  /// Registra el evento especificado con una función para manejar el evento.
  /// 
  /// @param evento                 Evento o ruta que se quiere manejar.
  /// @param funcionManejadorEvento Función que manejará el evento.
  public void registrarEvento(String evento, ManejadorEvento funcionManejadorEvento) {
    this.rutasEventos.put(evento, funcionManejadorEvento);
  }
  
  /// Maneja el evento especificado para el elemento dado. Internamente, se invoca a la
  /// función registrada que manejará el evento.
  /// 
  /// @param evento   Evento o ruta que se quiere manejar.
  /// @param elemento Objeto que se quiere enviar junto al evento.
  public void manejarEvento(String evento, Object elemento) {
    // primero, obtener la función asociada al evento
    ManejadorEvento funcion = this.rutasEventos.get(evento);
    // segundo, verificar que la función exista, que no sea nula
    if (funcion != null) {
      // tercero, invocar la función manejadora enviando el elemento que se quiere manejar
      funcion.aceptar(elemento);
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
}
