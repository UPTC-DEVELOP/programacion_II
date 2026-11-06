/// 
package co.edu.uptc.tiendalibros.negocio.excepciones;

/// Representa un error que ocurre cuando un elemento no existe en el repositorio.
public final class ViolacionElementoNoExisteException extends ViolacionReglaNegocioGeneralException {

  /// @param mensaje 
  public ViolacionElementoNoExisteException(String mensaje) {
    super(mensaje);
  }
  
}
