//
package co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar que el precio de un formato de libro es un número
/// positivo mayor que cero.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el formato de libro
/// viola la regla de precio mayor a cero.
public class ReglaFormatoLibroPrecioPositivo implements IReglaValidacion<FormatoLibro> {
  
  /// Nombre del atributo que se usa para reportar el error de validación.
  private final String nombreAtributo;
  
  /// Crea una nueva regla de precio positivo mayor que cero.
  ///
  /// @param nombreAtributo Nombre del atributo para reportar el error.
  public ReglaFormatoLibroPrecioPositivo(String nombreAtributo) {
    this.nombreAtributo = nombreAtributo;
  }
  
  @Override
  public boolean cumple(FormatoLibro formato) {
    // se revisa si el precio base decimal es mayor que cero
    return (formato.getPrecioBase() != null)
      && (formato.getPrecioBase()
        .esPositivo());
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, FormatoLibro formato) {
    if (cumple(formato) == true) {
      return Optional.empty();
    }
    
    String mensaje = "El precio base debe ser mayor a cero.";
    errores.agregarAtributo(this.nombreAtributo, mensaje);
    return Optional.of(new ViolacionReglaNegocioAtributoException(this.nombreAtributo, mensaje));
  }
}