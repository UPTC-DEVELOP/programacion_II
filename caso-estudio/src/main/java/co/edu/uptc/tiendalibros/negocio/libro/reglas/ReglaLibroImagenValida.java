//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la imagen de un libro es válida.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de tener una imagen válida.
public class ReglaLibroImagenValida implements IReglaValidacion<Libro> {
  
  /// Lista de extensiones de archivos de imagenes soportadas por Java JDK.
  public static final String EXTENSIONES_ARCHIVO_IMAGENES = "jpg,jpeg,png,gif,bmp,tiff,tif,wbmp";
  
  private static final PathMatcher EVALUADOR_IMAGEN_MATCHER = FileSystems.getDefault()
    .getPathMatcher(String.join("", "glob:**.", "{", ReglaLibroImagenValida.EXTENSIONES_ARCHIVO_IMAGENES, "}"));
  
  /// Crea un nuevo objeto de [ReglaLibroImagenValida] para validar un libro.
  public ReglaLibroImagenValida() {
    
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // no cumple si la imagen es nula o vacía
    if ((libro.getImagenLibro() == null) || (libro.getImagenLibro()
      .isBlank())) {
      return false;
    }
    
    // intentar verificar si la ruta nombre del archivo tiene las extensiones de imágen
    // permitida. Si ocurre un error devolver falso.
    
    try {
      // obtener la ruta de la imagen en minúsculas
      Path ruta = Path.of(
        libro.getImagenLibro()
          .toLowerCase()
      );
      
      return ReglaLibroImagenValida.EVALUADOR_IMAGEN_MATCHER.matches(ruta);
    } catch (IllegalArgumentException | UnsupportedOperationException excepcion) {
      return false;
    }
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format(
      "La imagen del libro no puede ser vacía, y sólo se permite estas extensiones de archivo: %s",
      ReglaLibroImagenValida.EXTENSIONES_ARCHIVO_IMAGENES
    );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("imagenLibro", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("imagenLibro", mensajeError)
    );
  }
}
