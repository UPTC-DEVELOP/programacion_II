//
package co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la cantidad de páginas de un formato de libro
/// es mayor que cero.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el formato del libro
/// viola la regla de cantidad de páginas mayor a cero.
public class ReglaFormatoLibroCantidadPaginasPositivas implements IReglaValidacion<FormatoLibro> {
  
  /// Nombre del atributo que se usa para reportar el error de validación.
  private final String nombreAtributo;
  
  /// Crea una nueva regla de cantidad de páginas mayor a cero.
  ///
  /// @param nombreAtributo Nombre del atributo para reportar el error.
  public ReglaFormatoLibroCantidadPaginasPositivas(String nombreAtributo) {
    this.nombreAtributo = nombreAtributo;
  }
  
  @Override
  public boolean cumple(FormatoLibro formato) {
    return formato.getCantidadPaginas() > 0;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, FormatoLibro formato) {
    if (cumple(formato) == true) {
      return Optional.empty();
    }
    
    String mensaje = "La cantidad de páginas debe ser mayor a cero.";
    errores.agregarAtributo(this.nombreAtributo, mensaje);
    return Optional.of(new ViolacionReglaNegocioAtributoException(this.nombreAtributo, mensaje));
  }
}