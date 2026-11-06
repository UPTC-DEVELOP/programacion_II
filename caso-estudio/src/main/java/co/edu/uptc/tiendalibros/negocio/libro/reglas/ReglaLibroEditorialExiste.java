//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar que la editorial asignada al libro exista en el
/// repositorio de datos.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el el libro la
/// editorial no existe.
public class ReglaLibroEditorialExiste implements IReglaValidacion<Libro> {
  
  /// Repositorio de editoriales.
  private final IRepositorioEditorial repositorioEditorial;
  
  /// Crea una nueva regla que verifica que la editorial asignada al libro existe en el
  /// repositorio.
  ///
  /// @param repositorio Repositorio de editoriales para saber si la editorial existe.
  public ReglaLibroEditorialExiste(
    IRepositorioEditorial repositorio
  ) {
    this.repositorioEditorial = repositorio;
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // no cumple si el id de la editorial que el libro tiene es nulo
    if (libro.getIdEditorial() == null) {
      return false;
    }
    
    // cumple si la búsqueda de editorial por id devuelve un resultado
    // no cumple si la búsqueda de editorial por id devuelve vacío
    return this.repositorioEditorial.buscarPorId(libro.getIdEditorial())
      .isPresent();
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    
    // aquí, el elemento viola la regla
    
    String mensajeError = "Debe seleccionar una editorial existente para el libro.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("editorial", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("editorial", mensajeError)
    );
  }
  
}