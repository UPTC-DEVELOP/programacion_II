/// 
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;
import java.util.regex.Pattern;

import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar que el ISBN del libro solo tenga dígitos y
/// guiones.
/// 
/// El ISBN de un libro tiene grupos de dígitos separados por un guión. Ejemplo:
/// 978-84-9964-952-8.
/// 
/// Un ISBN vacío cumple esta regla, porque de ese caso se encarga [ReglaLibroIsbnNoVacio].
/// 
/// La cantidad de caracteres la valida [ReglaLibroIsbnLongitudMaxima].
/// 
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de tener un ISBN con solo dígitos y guiones.
public class ReglaLibroIsbnSoloDigitosYGuiones implements IReglaValidacion<Libro> {
  
  /// Patrón del ISBN: uno o más dígitos, seguidos de cero o más grupos de un guión y
  /// más dígitos.
  private static final Pattern PATRON_ISBN = Pattern.compile("[0-9]+(-[0-9]+)*");
  
  /// Crea una nueva regla de ISBN con solo dígitos y guiones.
  public ReglaLibroIsbnSoloDigitosYGuiones() {
  }
  
  @Override
  public boolean cumple(Libro libro) {
    String isbn = libro.getIsbn();
    
    // un ISBN nulo o vacío lo reporta ReglaLibroIsbnNoVacio, no esta regla
    if (isbn == null || isbn.isEmpty()) {
      return true;
    }
    
    // validar si el isbn cumple el patrón de expresión regular.
    return PATRON_ISBN.matcher(isbn)
      .matches();
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "El ISBN solo puede tener dígitos separados por guiones. Ejemplo: 978-84-9964-952-8.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("isbn", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("isbn", mensajeError)
    );
  }
}
