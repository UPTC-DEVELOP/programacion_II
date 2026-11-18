//
package co.edu.uptc.tiendalibros.negocio.editorial.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;

/// Regla de validación para determinar que la editorial existe en el repositorio de
/// datos.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si al validar la editorial viola la
/// regla de que la editorial exista en el repositorio de datos.
public class ReglaEditorialExiste implements IReglaValidacion<Editorial> {
  
  /// Repositorio de editoriales.
  private final IRepositorioEditorial repositorio;
  
  /// Crea una nueva regla para verificar que la editorial existe en el repositorio.
  ///
  /// @param repositorio Repositorio para saber si la editorial existe en ese repositorio
  ///                    de datos.
  public ReglaEditorialExiste(
    IRepositorioEditorial repositorio
  ) {
    this.repositorio = repositorio;
  }
  
  @Override
  public boolean cumple(Editorial editorial) {
    // una editorial nueva (sin ID) no tiene nada previo que deba existir
    if (editorial.getId() == null) {
      return true;
    }
    
    // una editorial con ID cumple solo si todavía existe en el repositorio
    return this.repositorio.buscarPorId(editorial.getId())
      .isPresent();
  }

  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Editorial editorial) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(editorial) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format("La editorial '%s' ya no existe en el repositorio.", editorial.getNombre());
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
  }
}
