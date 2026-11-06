//
package co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el inventario de un formato de libro es mayor o
/// igual a cero.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el formato del libro
/// viola la regla de inventario mayor o igual a cero.
public class ReglaFormatoLibroInventarioPositivo implements IReglaValidacion<FormatoLibro> {
  
  /// Nombre del atributo que se usa para reportar el error de validación.
  private final String nombreAtributo;
  
  /// Crea una nueva regla de inventario mayor o igual a cero.
  ///
  /// @param nombreAtributo Nombre del atributo para reportar el error.
  public ReglaFormatoLibroInventarioPositivo(String nombreAtributo) {
    this.nombreAtributo = nombreAtributo;
  }
  
  @Override
  public boolean cumple(FormatoLibro formato) {
    return formato.getInventario() >= 0;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, FormatoLibro formato) {
    if (cumple(formato) == true) {
      return Optional.empty();
    }
    
    String mensaje = "El inventario debe ser mayor o igual a cero.";
    errores.agregarAtributo(this.nombreAtributo, mensaje);
    return Optional.of(new ViolacionReglaNegocioAtributoException(this.nombreAtributo, mensaje));
  }
}