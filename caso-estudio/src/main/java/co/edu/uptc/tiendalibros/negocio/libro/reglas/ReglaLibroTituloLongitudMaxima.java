/// 
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el libro tiene un título con la longitud máxima
/// de caracteres.
/// 
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de longitud máxima de título.
public class ReglaLibroTituloLongitudMaxima implements IReglaValidacion<Libro> {
  
  /// Longitud máxima del título.
  private static final int LONGITUD_MAXIMA = 255;
  
  /// Crear una nueva regla de título con longitud máxima
  public ReglaLibroTituloLongitudMaxima() {
    
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // no cumple si el título tiene más caracteres que la longitud máxima
    return (libro.getTitulo() == null) || (libro.getTitulo()
      .trim()
      .length() <= ReglaLibroTituloLongitudMaxima.LONGITUD_MAXIMA);
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    
    // aquí, el elemento viola la regla
    String mensajeError = String
      .format("El título no puede tener más de %d caracteres.", ReglaLibroTituloLongitudMaxima.LONGITUD_MAXIMA);
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("titulo", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException(
        "titulo", mensajeError
      )
    );
    
  }
  
}
