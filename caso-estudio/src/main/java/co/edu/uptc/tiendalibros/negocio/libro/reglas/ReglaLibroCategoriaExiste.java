///
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar que la categoría asignada al libro exista en el
/// repositorio de datos.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el el libro la
/// categoría no existe.
public class ReglaLibroCategoriaExiste implements IReglaValidacion<Libro> {
  
  /// Repositorio de categorías
  private final IRepositorioCategoria repositorioCategoria;
  
/// Crea una nueva regla que verifica que la categoría asignada al libro existe en el
  /// repositorio.
  ///
  /// @param repositorio Repositorio de categorías para saber si la categoría existe.
public ReglaLibroCategoriaExiste(
    IRepositorioCategoria repositorio
  ) {
    this.repositorioCategoria = repositorio;
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // no cumple si el id de la categoría que el libro tiene es nulo
    if (libro.getIdCategoria() == null) {
      return false;
    }
    
    // cumple si la búsqueda de categorías por id devuelve un resultado
    return (this.repositorioCategoria.buscarPorId(libro.getIdCategoria())
      .size() > 0);
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    
    // aquí, el elemento viola la regla
    
    String mensajeError = "Debe seleccionar una  categoría existente para el libro.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("categoria", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("categoria", mensajeError)
    );
  }
  
}
