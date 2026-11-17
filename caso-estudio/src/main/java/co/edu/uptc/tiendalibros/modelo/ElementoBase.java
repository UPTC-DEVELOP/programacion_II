//
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
  
  /// Indica si otro objeto representa la misma entidad: misma clase y mismo ID.
  ///
  /// Un elemento sin ID (porque aún no guardado) solo es igual a sí mismo.
  @Override
  public boolean equals(Object otro) {
    if (this == otro) {
      return true;
    }
    // se deben comparar IDs de la misma clase
    if ((otro == null) || this.getClass() != otro.getClass()) {
      return false;
    }
    
    ElementoBase elemento = (ElementoBase) otro;
    return (this.id != null) && (this.id.equals(elemento.id));
  }
  
  /// Calcula el código hash a partir del ID.
  /// 
  /// Dos objetos son iguales si también tienen el mismo hash code.
  @Override
  public int hashCode() {
    if (this.id == null) {
      // si el elemento no tiene ID, devolver una identidad dada por el sistema
      return System.identityHashCode(this);
    }
    // si el elemento ya tiene ID, se calcula el hash del ID
    return this.id.hashCode();
  }
}
