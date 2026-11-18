//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.time.Year;
import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el año de publicación es válido.
/// 
/// El año es válido si está entre un año mínimo y el año siguiente al año actual.
/// 
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de tener un año de publicación válido.
public class ReglaLibroAnioPublicacionValido implements IReglaValidacion<Libro> {
  /// Año mínimo de publicación permitido
  public static final int ANIO_MINIMO = 1900;
  
  /// Crea una nueva regla de año de publicación válido
  public ReglaLibroAnioPublicacionValido() {
    
  }
  
  @Override
  public boolean cumple(Libro libro) {
    int anio = libro.getAnioPublicacion();
    // cumple si el año de publicación está en el rango del año mínimo y el año máximo
    return ((anio >= ReglaLibroAnioPublicacionValido.ANIO_MINIMO) && (anio <= ReglaLibroAnioPublicacionValido.obtenerAnioMaximo()));
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format(
      "El año de publicación debe estar entre %d y %d.",
      ReglaLibroAnioPublicacionValido.ANIO_MINIMO,
      obtenerAnioMaximo()
    );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("anioPublicacion", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("anioPublicacion", mensajeError)
    );
  }
  
  /// Obtiene el año máximo de publicación permitido.
  /// 
  /// El año máximo es el año próximo.
  /// 
  /// @return Año máximo de publicación permitido.
  public static int obtenerAnioMaximo() {
    // calcular el próximo año
    return Year.now()
      .getValue() + 1;
  }
}
