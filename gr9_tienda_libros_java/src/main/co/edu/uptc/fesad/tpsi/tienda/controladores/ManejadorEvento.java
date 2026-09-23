//
package main.co.edu.uptc.fesad.tpsi.tienda.controladores;

/// Representa un manejador de evento.
@FunctionalInterface
public interface ManejadorEvento {
  
  /// Acepta el elemento y ejecuta las acciones propias del evento.
  void aceptar(Object elemento);
}
