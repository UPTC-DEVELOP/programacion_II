//
package co.edu.uptc.tiendalibros.gui.categoria;

import co.edu.uptc.tiendalibros.gui.componentes.ElementoVista;
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un objeto [Categoria] adaptado para ser mostrado en la interfaz gráfica.
public class ElementoVistaCategoria extends ElementoVista<Categoria> {
  
  /// Crea un objeto [ElementoVistaCategoria] con una categoría nueva y sin errores.
  public ElementoVistaCategoria() {
    this(new Categoria(), new ResultadoValidacion());
  }
  
  /// Crea un objeto [ElementoVistaCategoria] con los valores especificados.
  ///
  /// @param categoria Categoría principal de la vista.
  /// @param errores   Posibles errores de validación.
  public ElementoVistaCategoria(
    Categoria categoria,
    ResultadoValidacion errores
  ) {
    super(categoria, errores);
  }
}
