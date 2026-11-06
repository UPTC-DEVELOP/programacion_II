///
package co.edu.uptc.tiendalibros.modelo;

import co.edu.uptc.tiendalibros.interfaces.IElemento;

///
public abstract class ElementoBaseBuilder<T extends IElemento> {
  
  /// ID del elemento
  private Long id;
  
  public abstract T construir();
  
  public ElementoBaseBuilder<T> id(Long id) {
    this.id = id;
    return this;
  }
  
  public Long id() {
    return this.id;
  }
  
}
