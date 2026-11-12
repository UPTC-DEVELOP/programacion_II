//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el libro tiene un ISBN con la longitud máxima
/// de caracteres.
/// 
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de longitud máxima de ISBN.
public class ReglaLibroIsbnLongitudMaxima implements IReglaValidacion<Libro> {
  
  /// Longitud máxima del ISBN.
  private static final int LONGITUD_MAXIMA = 17;
  
  /// Crear una nueva regla de ISBN con longitud máxima
  public ReglaLibroIsbnLongitudMaxima() {
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // no cumple si el ISBN tiene más caracteres que la longitud máxima
    return (libro.getIsbn() == null) || (libro.getIsbn()
      .trim()
      .length() <= ReglaLibroIsbnLongitudMaxima.LONGITUD_MAXIMA);
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    
    // aquí, el elemento viola la regla
    String mensajeError = String
      .format("El ISBN no puede tener más de %d caracteres.", ReglaLibroIsbnLongitudMaxima.LONGITUD_MAXIMA);
    
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
