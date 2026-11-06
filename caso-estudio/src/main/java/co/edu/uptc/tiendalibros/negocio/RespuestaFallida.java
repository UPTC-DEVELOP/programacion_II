//
package co.edu.uptc.tiendalibros.negocio;

import java.util.Optional;

/// /// Representa una respuesta fallida del sistema, con errores.
///
/// @param <T>
public final class RespuestaFallida<T> extends Respuesta<T> {
  /// Resultado de errores
  private ResultadoValidacion errores;
  
  /// Crea un nuevo objeto [RespuestaFallida] con el elemento, el mensaje y los errores
  /// especificados.
  ///
  /// @param elemento Elemento de la respuesta.
  /// @param mensaje  Mensaje de la respuesta.
  /// @param errores  Errores de validación
  public RespuestaFallida(
    Optional<T> elemento,
    String mensaje,
    ResultadoValidacion errores
  ) {
    super(elemento, mensaje);
    this.errores = errores;
  }
  
  /// Obtiene los errores de validación del elemento.
  ///
  /// @return Errores de validación
  public ResultadoValidacion getErrores() { return this.errores; }
}
