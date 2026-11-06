///
package co.edu.uptc.tiendalibros.gui.autor;

import co.edu.uptc.tiendalibros.gui.componentes.ElementoVista;
import co.edu.uptc.tiendalibros.modelo.Autor;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un objeto [Autor] adaptado para ser mostrado en la interfaz gráfica.
public class ElementoVistaAutor extends ElementoVista<Autor> {
  
  /// Crea un objeto [ElementoVistaAutor] con un autor nuevo y sin errores.
  public ElementoVistaAutor() {
    this(new Autor(), new ResultadoValidacion());
  }
  
  /// Crea un objeto [ElementoVistaAutor] con los valores especificados.
  ///
  /// @param autor   Autor principal de la vista.
  /// @param errores Posibles errores de validación.
  public ElementoVistaAutor(
    Autor autor,
    ResultadoValidacion errores
  ) {
    super(autor, errores);
  }
}