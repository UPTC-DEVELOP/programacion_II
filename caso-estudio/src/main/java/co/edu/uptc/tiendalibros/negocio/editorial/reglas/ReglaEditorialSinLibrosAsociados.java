//
package co.edu.uptc.tiendalibros.negocio.editorial.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;

/// Regla de validación para determinar si una editorial no tiene libros asociados antes
/// de ser eliminada.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si al validar la editorial viola la
/// regla de no tener libros asociados al momento de ser eliminada.
public class ReglaEditorialSinLibrosAsociados implements IReglaValidacion<Editorial> {
  
  /// Repositorio de libros
  private final IRepositorioLibro repositorioLibro;
  
  /// @param repositorioLibro Repositorio de libros para verificar si la editorial tiene
  ///                         libros asociados.
  public ReglaEditorialSinLibrosAsociados(IRepositorioLibro repositorioLibro) {
    this.repositorioLibro = repositorioLibro;
  }
  
  @Override
  public boolean cumple(Editorial editorial) {
    // cumple (se puede eliminar) solo si ningún libro tiene esta editorial
    return this.repositorioLibro.existeLibroConEditorial(editorial) == false;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Editorial editorial) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(editorial) == true) {
      return Optional.empty();
    }
    
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format(
      "No se puede eliminar la editorial '%s' porque tiene libros asociados.%nReasigne o elimine esos libros.",
      editorial.getNombre()
    );
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
    
  }
  
}
