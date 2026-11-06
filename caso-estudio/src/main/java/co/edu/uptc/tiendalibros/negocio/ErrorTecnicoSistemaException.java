/// 
package co.edu.uptc.tiendalibros.negocio;

/// Representa un error técnico el sistema. Encapsula, por ejemplo,
/// SQLException, IOException.
public class ErrorTecnicoSistemaException extends RuntimeException {
  /// Crea un nuevo objeto del error.
  public ErrorTecnicoSistemaException(
    String mensaje,
    Throwable causa
  ) {
    super(mensaje, causa);
  }
}
