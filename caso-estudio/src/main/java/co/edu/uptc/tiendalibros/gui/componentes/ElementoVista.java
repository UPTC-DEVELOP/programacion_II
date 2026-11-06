//
package co.edu.uptc.tiendalibros.gui.componentes;

import co.edu.uptc.tiendalibros.interfaces.IElemento;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa una elemento adaptado para ser mostrado en un formulario de la interfaz
/// gráfica.
/// 
/// @param <T>
public abstract class ElementoVista<T extends IElemento> {
  
  /// Elemento principal que se mostrará en la vista.
  private final T elementoPrincipal;
  
  /// Resultado con los posibles errore de validación.
  private final ResultadoValidacion errores;
  
  /// Crea un nuevo objeto [ElementoVista] con valores predeterminados y sin errores.
  ///
  /// @param elemento Elemento principal que se quiere a mostrar en la vista.
  public ElementoVista(T elemento) {
    this(elemento, new ResultadoValidacion());
  }
  
  /// Crea un nuevo objeto [ElementoVista].
  ///
  /// @param elemento Elemento principal que se quiere mostrar en la vista.
  /// @param errores  Resultado con los posibles errores de validación.
  public ElementoVista(
    T elemento,
    ResultadoValidacion errores
  ) {
    this.elementoPrincipal = elemento;
    this.errores = errores;
  }
  
  public T getElemento() { return this.elementoPrincipal; }
  
  public ResultadoValidacion getErrores() { return this.errores; }
  
  public boolean tieneErrores() {
    return getErrores() != null && getErrores().hayErrores();
  }
  
}
