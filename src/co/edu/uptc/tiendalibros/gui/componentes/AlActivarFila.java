/// 
package co.edu.uptc.tiendalibros.gui.componentes;

/// Representa una función que manejará un evento cuando el usuario selecciona o activa
/// una fila.
@FunctionalInterface
public interface AlActivarFila {
  void aceptar(int indiceFila);
}
