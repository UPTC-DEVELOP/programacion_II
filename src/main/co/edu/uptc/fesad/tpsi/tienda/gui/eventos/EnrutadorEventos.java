//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IReceptorEventos;

/// Representa un objeto que enruta o asocia eventos con sus manejadores.
public class EnrutadorEventos implements IReceptorEventos {
  
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
    // sólo se deben registrar eventos o rutas únicas para evitar llamar a manejadores incorrectos 
    if (this.rutasEventos.containsKey(evento)) {
      
    // se aplica el concepto de excepción cuando el estado de un objeto es incorrecto
    
    throw new IllegalStateException(String.format("El evento \"%s\" ya está registrado", evento));
  } else {
    // no se ha registrado el evento antes, entonces registrarlo: se asocia el evento o ruta
    // con la función que manjerá el evento cuando suceda.
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
    if (funcion != null) {
      // tercero, invocar la función manejadora enviando el elemento que se quiere manejar
      funcion.aceptar(elemento);
    } else {
      // TODO: no debería pasar que el evento no está registrado, por ahora sólo mostrar que el
      // evento no está disponible.
      String mensaje = String.format("Opción No Disponible.%nEl evento \"%s\" no está registrado.", evento);
      JOptionPane.showMessageDialog(null, mensaje);
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
