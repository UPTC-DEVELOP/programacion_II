//
package co.edu.uptc.tiendalibros.negocio;

import java.util.Optional;

/// Representa una respuesta exitosa del sistema, sin errores.
///
/// @param <T>
public final class RespuestaExitosa<T> extends Respuesta<T> {
  
  /// Crea un nuevo objeto [RespuestaExistosa] con el elemento y el mensaje especificados.
  ///
  /// @param elemento Elemento de la respuesta.
  /// @param mensaje  Mensaje de la respuesta.
  public RespuestaExitosa(
    Optional<T> elemento,
    String mensaje
  ) {
    super(elemento, mensaje);
  }
  
}
