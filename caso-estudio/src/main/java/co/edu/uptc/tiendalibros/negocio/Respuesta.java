//
package co.edu.uptc.tiendalibros.negocio;

import java.util.Optional;

/// Representa una respuesta del sistema.
///
/// @param <T>
public sealed abstract class Respuesta<T> permits RespuestaExitosa, RespuestaFallida {
  
  /// Elemento de la respuesta.
  private Optional<T> elemento;
  
  /// Mensaje de la respuesta.
  private String mensaje;
  
  /// Crea un nuevo objeto [Respuesta] con el elemento y el mensaje especificados.
  ///
  /// @param elemento Elemento de la respuesta.
  /// @param mensaje  Mensaje de la respuesta.
  protected Respuesta(
    Optional<T> elemento,
    String mensaje
  ) {
    this.elemento = elemento;
    this.mensaje = mensaje;
  }
  
  /// Obtiene el elemento de la respuesta.
  ///
  /// @return Elemento de la respuesta.
  public Optional<T> getElemento() { return this.elemento; }
  
  /// Obtiene el mensaje de la respuesta.
  ///
  /// @return Mensaje de la respuesta.
  public String getMensaje() { return this.mensaje; }
  
}
