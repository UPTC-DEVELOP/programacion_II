//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el libro tiene un nombre de archivo de imagen
/// con la longitud máxima de caracteres.
/// 
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de longitud máxima del nombre de archivo de la imagen.
public class ReglaLibroImagenLongitudMaxima implements IReglaValidacion<Libro> {
  
  /// Longitud máxima del archivo de imagen.
  private static final int LONGITUD_MAXIMA = 255;
  
  /// Crear una nueva regla de nombre de archivo de imagen con longitud máxima
  public ReglaLibroImagenLongitudMaxima() {
    
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // no cumple si nombre del archivo de imagen tiene más caracteres que la longitud máxima
    return (libro.getImagenLibro() == null) || (libro.getImagenLibro()
      .trim()
      .length() <= ReglaLibroImagenLongitudMaxima.LONGITUD_MAXIMA);
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    
    // aquí, el elemento viola la regla
    String mensajeError = String
      .format(
        "El nombre de archivo de la imagen no puede tener más de %d caracteres.",
        ReglaLibroImagenLongitudMaxima.LONGITUD_MAXIMA
      );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("imagenLibro", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException(
        "imagenLibro", mensajeError
      )
    );
    
  }
  
}
