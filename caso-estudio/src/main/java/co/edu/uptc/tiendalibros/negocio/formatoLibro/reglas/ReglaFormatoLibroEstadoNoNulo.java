//
package co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar que el estado del catálogo de un libro no sea
/// nulo.
/// 
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el formato del libro
/// viola la regla de estado en el catálogo no nulo.
public class ReglaFormatoLibroEstadoNoNulo implements IReglaValidacion<FormatoLibro> {
  
  /// Nombre del atributo que se usa para reportar el error de validación.
  private final String nombreAtributo;
  
  /// Crea una nueva regla de estado en el catálogo no nulo.
  /// 
  /// @param nombreAtributo Nombre del atributo para reportar el error.
  public ReglaFormatoLibroEstadoNoNulo(String nombreAtributo) {
    this.nombreAtributo = nombreAtributo;
  }
  
  @Override
  public boolean cumple(FormatoLibro formato) {
    // cumple si el estado en el catálogo no es nulo
    return formato.getEstadoEnCatalogo() != null;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, FormatoLibro formato) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(formato) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "El formato debe tener un estado en el catálogo.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo(this.nombreAtributo, mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException(this.nombreAtributo, mensajeError)
    );
  }
}
