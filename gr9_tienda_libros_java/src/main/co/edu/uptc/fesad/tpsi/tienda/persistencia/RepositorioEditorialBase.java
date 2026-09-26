/// 
package main.co.edu.uptc.fesad.tpsi.tienda.persistencia;

import main.co.edu.uptc.fesad.tpsi.tienda.negocio.Editorial;

/// Representa el repositorio que almacena las editoriales. También hace
/// validaciones generales.
public abstract class RepositorioEditorialBase implements IRepositorioEditorial {
  
  public void validarEditorial(Editorial editorial) {
    if (editorial == null) {
      throw new IllegalArgumentException("La editorial no puede ser nula.");
    }
    if (editorial.getNombre() == null || editorial.getNombre()
      .isBlank()) {
      throw new IllegalArgumentException("El nombre de la editorial no puede estar vacío.");
    }
  }
}
