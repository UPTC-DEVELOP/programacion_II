//
package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;
import java.util.Optional;

/// Representa un repositorio de datos de elementos de tipo `T` en el que solo se pueden
/// hacer consultas, pero no modificaciones a los datos.
/// 
/// @param <T> Tipo de elemento que se almacena en el repositorio.
public interface IRepositorioSoloConsultas<T> {
  
  /// Busca el elemento con el ID especificado.
  ///
  /// @param id ID del elemento que se quiere buscar.
  /// @return El elemento encontrado, o un valor vacío si no existe.
  Optional<T> buscarPorId(Long id);
  
  /// Obtiene la lista de elementos almacenados en el repositorio.
  ///
  /// @return Lista de elementos almacenados. Vacía si no hay elementos almacenados.
  List<T> listar();
}
