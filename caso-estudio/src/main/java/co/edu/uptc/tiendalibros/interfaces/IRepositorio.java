///
package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa un repositorio de datos de elementos de tipo `T`.
///
/// @param <T> Tipo de elemento que se almacena en el repositorio.
public interface IRepositorio<T> {
  
  /// Guarda un elemento en el repositorio.
  ///
  /// Si el elemento no tiene ID, lo crea y le asigna un ID. Si el elemento tiene ID,
  /// actualiza los datos del elemento existente.
  ///
  /// @param elemento Elemento que se desea guardar.
  /// @return El elemento guardado, con su ID generado por el repositorio.
  /// @throws IllegalArgumentException           Si `elemento` es un objeto nulo.
  /// @throws ViolacionElementoNoExisteException Si se quería actualizar un elemento que no
  ///                                            existe en el repositorio.
  T guardar(T elemento) throws ViolacionElementoNoExisteException;
  
  /// Elimina del repositorio el elemento con el ID especificado.
  ///
  /// @param id ID del elemento que se quiere eliminar.
  /// @return `true` si el elemento se eliminó. `false` si no se encontró.
  boolean eliminar(Long id);
  
  /// Busca el elemento con el ID especificado.
  ///
  /// @param id ID del elemento que se quiere buscar.
  /// @return El elemento encontrado, o un valor vacío si no existe.
  Optional<T> buscarPorId(Long id);
  
  /// Obtiene la lista de elementos almacenados en el repositorio.
  ///
  /// @return Lista de elementos almacenados. Puede estar vacía, pero no se devuelve `null`.
  List<T> listar();
  
}