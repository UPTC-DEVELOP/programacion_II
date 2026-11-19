
package co.edu.uptc.tiendalibros.gui.clientes;

import co.edu.uptc.tiendalibros.gui.componentes.ElementoVista;
import co.edu.uptc.tiendalibros.modelo.Cliente;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un objeto [Cliente] adaptado para ser mostrado en la interfaz gráfica.
public class ElementoVistaCliente extends ElementoVista<Cliente> {

  /// Crea un objeto [ElementoVistaCliente] con un cliente nuevo y sin errores.
  public ElementoVistaCliente() {
    this(new Cliente(), new ResultadoValidacion());
  }

  /// Crea un objeto [ElementoVistaCliente] con los valores especificados.
  ///
  /// @param cliente Cliente principal de la vista.
  /// @param errores Posibles errores de validación.
  public ElementoVistaCliente(
    Cliente cliente,
    ResultadoValidacion errores
  ) {
    super(cliente, errores);
  }
}
