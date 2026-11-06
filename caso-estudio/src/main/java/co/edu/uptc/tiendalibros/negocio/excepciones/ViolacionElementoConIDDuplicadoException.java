/// 
package co.edu.uptc.tiendalibros.negocio.excepciones;

/// Representa una error que ocurre cuando hay elementos que tienen ID duplicados.
public final class ViolacionElementoConIDDuplicadoException extends ViolacionReglaNegocioGeneralException {

  /// @param mensaje 
  public ViolacionElementoConIDDuplicadoException(String mensaje) {
    super(mensaje);

  }
  
}
