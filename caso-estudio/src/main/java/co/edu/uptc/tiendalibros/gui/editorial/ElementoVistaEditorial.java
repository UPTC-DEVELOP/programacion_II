///
package co.edu.uptc.tiendalibros.gui.editorial;

import co.edu.uptc.tiendalibros.gui.componentes.ElementoVista;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un objeto [Editorial] adaptado para ser mostrado en la
/// interfaz gráfica.
public class ElementoVistaEditorial extends ElementoVista<Editorial> {
  
  
  /// Crea un objeto [ElementoVistaEditorial] con una editorial nueva y sin errores.
  public ElementoVistaEditorial() {
    this(new Editorial(), new ResultadoValidacion());
  }
  
  /// Crea un objeto [ElementoVistaEditorial] con los valores especificados.
  ///
  /// @param editorial Editorial principal de la vista.
  /// @param errores   Posibles errores de validación.
  public ElementoVistaEditorial(Editorial editorial, ResultadoValidacion errores) {
    super(editorial, errores);
  }

}
