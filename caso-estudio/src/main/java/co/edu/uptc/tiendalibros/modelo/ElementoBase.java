///
package co.edu.uptc.tiendalibros.modelo;

import co.edu.uptc.tiendalibros.interfaces.IElemento;

/// Representa un elemento o entidad del negocio.
public class ElementoBase implements IElemento {
  
  /// ID del elemento
  private Long id;
  
  /// Crea un objeto [ElementoBase][ElementoBase] sin ID.
  public ElementoBase() {
    this(null);
  }
  
  /// Crea un objeto [ElementoBase][ElementoBase] con el ID dado.
  ///
  /// @param id ID del elemento.
  public ElementoBase(Long id) {
    this.id = id;
  }
  
  /// Obtiene el ID del elemento.
  @Override
  public Long getId() { return this.id; }
}
