/// 
package main.co.edu.uptc.fesad.tpsi.tienda.interfaces;

/// Representa un objeto que recibe eventos para manejarlos.
/// 
/// Esta clase es un intento para practicar que la clase Evento dependa de una interfaz en
/// vez de una clase concreta JFrame, según los principios SOLID.
@FunctionalInterface
public interface IReceptorEventos {
  
  /// Recibe un evento o ruta y atiende el evento con un elemento dato opcional.
  void manejarEvento(String evento, Object elemento);
}
