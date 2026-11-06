//
package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;

import co.edu.uptc.tiendalibros.modelo.AutorLibro;

/// Representa el repositorio de datos que almacena las asociaciones autor-libro de
/// un libro.
public interface IRepositorioAutorLibro {
  
  /// @param idLibro ID del libro del que se quieren obtener sus asociaciones.
  /// @return Asociaciones del libro.
  List<AutorLibro> buscarPorIdLibro(Long idLibro);
  
  /// Asigna todas las asociaciones autor-libro del libro por las especificadas.
  ///
  /// Se reemplazan las asociaciones anteriores y se queda con la asociación especificada.
  ///
  /// Cada asociación de autor-libro tiene el mismo `idLibro`.
  ///
  /// @param idLibro      ID del libro cuyas asociaciones se asignan y reemplazan.
  /// @param asociaciones Asociaciones del libro.
  /// @return Las asociaciones guardadas, con sus IDs asignados.
  List<AutorLibro> asignarAsociacion(Long idLibro, List<AutorLibro> asociaciones);
  
  /// Elimina todas las asociaciones autor-libro del libro especificado.
  ///
  /// @param idLibro ID del libro del que se quiere eliminar sus asociaciones.
  /// @return `true` si se eliminó al menos una asociación.
  boolean eliminarPorLibro(Long idLibro);
  
  /// Obtiene todas las asociaciones autor-libro almacenadas.
  ///
  /// @return Lista de todas las asociaciones autor-libro almacenadas.
  List<AutorLibro> listar();
}