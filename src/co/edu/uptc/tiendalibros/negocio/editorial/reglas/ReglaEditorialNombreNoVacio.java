//
package co.edu.uptc.tiendalibros.negocio.editorial.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la editorial tiene un nombre.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar la editorial viola
/// la regla de tener un nombre.
public class ReglaEditorialNombreNoVacio implements IReglaValidacion<Editorial> {
  
  /// Crea una nueva regla de nombre de editorial no vacío.
  public ReglaEditorialNombreNoVacio() {
  }
  
  @Override
  public boolean cumple(Editorial editorial) {
    // no se permite un nombre de editorial vacío
    if (editorial.getNombre() == null || editorial.getNombre()
      .trim()
      .isEmpty()) {
      return false;
    }
    return true;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Editorial editorial) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(editorial) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format("El nombre de la editorial no puede ser vacío.");
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("nombre", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("nombre", mensajeError)
    );
  }
  
}
