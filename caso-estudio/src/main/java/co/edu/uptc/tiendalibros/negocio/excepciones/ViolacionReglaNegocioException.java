/// 
package co.edu.uptc.tiendalibros.negocio.excepciones;

import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un error cuando se viola una regla de negocio.
public abstract sealed class ViolacionReglaNegocioException extends Exception
  permits ViolacionReglaNegocioAtributoException, ViolacionReglaNegocioGeneralException {


  /// Crea una nueva excepción provocada por la violación a una regla de negocio de un
  /// atributo.
  /// 
  /// @param mensaje  Mensaje explicativo.
  protected ViolacionReglaNegocioException(
    String mensaje
  ) {
    // La clase base Exception tiene estos parámetros
    // - message: mensaje de detalle
    // - cause: causa de la excepción
    // - enableSuppression: habilitar la supresión
    // - writableStackTrace: habilitar la pila de trazabilidad
    
    // para crear un objeto excepción liviana que no consuma recursos:
    // enableSuppression = false : la excepción habilitada
    // writableStackTrace = false: no es necesario una pila de trazabilidad
    super(mensaje, null, false, false);
  }
  
  /// Reporta esta violación de regla de negocio a la categoría de errores apropiado.
  /// 
  /// @errores Objeto errores al que se le reportará esta violación de regla de negocio.
  public abstract void reportarEn(ResultadoValidacion errores);
}
