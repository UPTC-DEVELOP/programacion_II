//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar que el libro tiene un título no vacío.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de tener un título no vacío.
public class ReglaLibroTituloNoVacio implements IReglaValidacion<Libro> {
  
  /// Crea una nueva regla de título de libro no vacío.
  public ReglaLibroTituloNoVacio() {
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // no se permite un título de libro vacío
    return libro.getTitulo() != null && !libro.getTitulo()
      .trim()
      .isEmpty();
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "El título del libro no puede ser vacío.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("titulo", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("titulo", mensajeError)
    );
    
  }
  
}