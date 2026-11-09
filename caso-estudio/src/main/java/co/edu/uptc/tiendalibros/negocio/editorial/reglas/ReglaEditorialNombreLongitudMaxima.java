//
package co.edu.uptc.tiendalibros.negocio.editorial.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la editorial tiene un nombre con la longitud
/// máxima permitida.
/// 
/// 
public class ReglaEditorialNombreLongitudMaxima implements IReglaValidacion<Editorial> {
  
  /// Crea una nueva regla de nombre de editorial con la longitud máxima.
  public ReglaEditorialNombreLongitudMaxima() {
    
  }
  
  @Override
  public boolean cumple(Editorial editorial) {
    // solo se cumple si el nombre de la editorial no excede la longitud máxima.
    return (editorial.getNombre() == null) || (editorial.getNombre()
      .trim()
      .length() <= Editorial.LONGITUD_MAXIMA_NOMBRE);
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Editorial editorial) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(editorial)) {
      return Optional.empty();
    }
    
    // aquí el elemento viola la regla
    
    String mensajeError = String
      .format("El nombre de la editorial no puede tener más de %d caracteres", Editorial.LONGITUD_MAXIMA_NOMBRE);
    
    // reportar el error (atributo, mensajeError)
    errores.agregarAtributo("nombre", mensajeError);
    
    // devolver la excepción de la regla de negocio respectiva
    return Optional.of(new ViolacionReglaNegocioAtributoException("nombre", mensajeError));
  }
  
}
