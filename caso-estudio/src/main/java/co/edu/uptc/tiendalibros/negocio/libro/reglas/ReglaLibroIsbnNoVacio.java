//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el libro tiene un ISBN.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de tener un ISBN.
public class ReglaLibroIsbnNoVacio implements IReglaValidacion<Libro> {
  
  /// Crea una nueva regla de ISBN de libro no vacío.
  public ReglaLibroIsbnNoVacio() {
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // no se permite un isbn de libro vacío
    return (libro.getIsbn() != null) && (!libro.getIsbn()
      .trim()
      .isEmpty());
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "El ISBN del libro no puede ser vacío.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("isbn", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException(
        "isbn", mensajeError
      )
    );
    
  }
  
}