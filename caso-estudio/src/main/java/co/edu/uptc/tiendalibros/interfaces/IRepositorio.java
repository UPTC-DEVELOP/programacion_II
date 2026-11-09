//
package co.edu.uptc.tiendalibros.interfaces;

import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa un repositorio de datos de elementos de tipo `T` en el que se pueden hacer
/// modificaciones a los datos y consultas.
///
/// @param <T> Tipo de elemento que se almacena en el repositorio.
public interface IRepositorio<T> extends IRepositorioSoloConsultas<T> {
  
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
  /// @throws IllegalArgumentException Si el `id` especificado es nulo.
  boolean eliminar(Long id);
  
}