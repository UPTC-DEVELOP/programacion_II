//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Objects;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el ISBN de un libro es único.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si al validar el libro viola la
/// regla de tener un ISBN único.
public class ReglaLibroIsbnUnico implements IReglaValidacion<Libro> {
  
  /// Repositorio de libros.
  private final IRepositorioLibro repositorio;
  
  /// Crea una nueva regla de ISBN de libro único.
  ///
  /// @repositorio repositorio Repositorio de libros para saber si el ISBN es único.
  public ReglaLibroIsbnUnico(IRepositorioLibro repositorio) {
    this.repositorio = repositorio;
  }
  
  @Override
  public boolean cumple(Libro libro) {
    // buscar un libro que tenga el mismo ISBN.
    Optional<Libro> existente = this.repositorio.buscarPorIsbn(libro.getIsbn());
    // cumple si no existe ningún otro libro con ese ISBN.
    // también cumple si el ISBN existe y es el mismo libro porque el mismo ID
    // significa que
    // se está actualizando el libro
    return existente.isEmpty()
      || Objects.equals(
        existente.get()
          .getId(),
        libro.getId()
      );
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Libro libro) {
    
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libro) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format("Ya existe un libro con el ISBN '%s'.", libro.getIsbn());
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("isbn", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("isbn", mensajeError)
    );
  }
  
}