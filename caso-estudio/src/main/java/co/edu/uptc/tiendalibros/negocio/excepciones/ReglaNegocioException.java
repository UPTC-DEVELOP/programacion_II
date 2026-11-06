/// 
package co.edu.uptc.tiendalibros.negocio.excepciones;

/// Representa un error cuando se viola una regla de negocio.
public class ReglaNegocioException extends RuntimeException {
  public ReglaNegocioException(String mensaje) {
    super(mensaje);
  }
}