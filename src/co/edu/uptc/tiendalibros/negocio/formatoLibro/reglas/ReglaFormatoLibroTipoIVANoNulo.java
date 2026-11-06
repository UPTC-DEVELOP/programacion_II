//
package co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de negocio para determinar si el tipo de IVA de un formato de libro no sea nulo.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el formato del libro
/// viola la regla de tipo de IVA no nulo.
public class ReglaFormatoLibroTipoIVANoNulo implements IReglaValidacion<FormatoLibro> {
  
  /// Nombre del atributo que se usa para reportar el error de validación.
  private final String nombreAtributo;
  
  /// Crea una nueva regla de tipo de IVA no nulo.
  ///
  /// @param nombreAtributo Nombre del atributo para reportar el error.
  public ReglaFormatoLibroTipoIVANoNulo(String nombreAtributo) {
    this.nombreAtributo = nombreAtributo;
  }
  
  @Override
  public boolean cumple(FormatoLibro formato) {
    return formato.getTipoIVA() != null;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, FormatoLibro formato) {
    if (cumple(formato) == true) {
      return Optional.empty();
    }
    
    String mensaje = "El tipo de IVA no debe ser nulo. Debe seleccionarse un tipo de IVA.";
    errores.agregarAtributo(this.nombreAtributo, mensaje);
    return Optional.of(new ViolacionReglaNegocioAtributoException(this.nombreAtributo, mensaje));
  }
}
