//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;

/// Regla de validación para determinar que el libro existe en el repositorio de datos.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si al validar el libro se viola la
/// regla de que el libro exista en el repositorio de datos.
public class ReglaLibroExiste implements IReglaValidacion<Libro> {
  
  /// Repositorio de libros.
  private final IRepositorioLibro repositorio;
  
  /// Crea una nueva regla que verifica que el libro existe en el repositorio.
  ///
  /// @param repositorio Repositorio de libros para saber si el libro existe.
  public ReglaLibroExiste(IRepositorioLibro repositorio) {
    this.repositorio = repositorio;
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // un libro nuevo sin ID
    if (libro.getId() == null) {
      return true;
    }
    
    // un libro con ID cumple solo si todavía existe en el repositorio
    return this.repositorio.buscarPorId(libro.getId())
      .isPresent();
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format(
      "El libro '%s' ya no existe en el repositorio.",
      libro.getTitulo()
    );
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
    
  }
  
  
}
