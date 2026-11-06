//
package co.edu.uptc.tiendalibros.negocio.editorial.reglas;

import java.util.Objects;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el nombre de una editorial es único.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar la editorial viola
/// la regla de tener un nombre único.
public class ReglaEditorialNombreUnico implements IReglaValidacion<Editorial> {
  
  /// Repositorio de editoriales.
  private final IRepositorioEditorial repositorio;
  
  /// Crea una nueva regla de nombre de editorial único.
  ///
  /// @param repositorio Repositorio de libros para saber si el nombre de la editorial
  ///                    es único.
  public ReglaEditorialNombreUnico(
    IRepositorioEditorial repositorio
  ) {
    this.repositorio = repositorio;
  }
  
  @Override
  public boolean cumple(Editorial editorial) {
    // se busca en el repositorio si hay una editorial existente con el mismo nombre.
    for (Editorial existente : this.repositorio.buscarPorNombre(editorial.getNombre())) {
      // se compara si la editorial existente que tiene el mismo nombre tiene el mismo ID
      // true: la editorial dada y la editorial existente es la misma editorial en el
      // repositorio
      // false: la editorial dada y la editorial existente son dos editoriales diferentes en el
      // repositorio
      // Long se compara con equals. Object.equals mantiene la similitud con long id == long id
      if (Objects.equals(existente.getId(), editorial.getId()) == false) {
        return false;
      }
    }
    
    // aquí, no hay violación de la regla porque la editorial dada o bien es nueva, o es la
    // misma
    return true;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Editorial editorial) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(editorial) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format(
      "Ya existe otra editorial con el nombre '%s'.",
      editorial.getNombre()
    );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("nombre", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("nombre", mensajeError)
    );
  }
  
}