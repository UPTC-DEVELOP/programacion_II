//
package co.edu.uptc.tiendalibros.negocio;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Representa una regla de validación para saber si un elemento cumple la regla del
/// negocio respectiva.
///
/// Retorna una [ViolacionReglaNegocioException] si al validar el elemento viola una regla
/// de negocio.
public interface IReglaValidacion<T> {
  
  /// Determina si el elemento cumple con esta regla de validación.
  ///
  /// @param elemento Elemento del que se quiere determinar si cumple esta regla de
  ///                 validación.
  /// @return `true` si el elemento cumple esta regla de validación. `false` si no la cumple.
  boolean cumple(T elemento);
  
  /// Valida si el elemento cumple con esta regla de validación y la reporta a los errores.
  ///
  /// Si el elemento cumple la regla negocio, se devuelve vacío. Si el elemento no cumple la
  /// regla de negocio, se retorna la excepción de violación de regla de negocio respectiva.
  ///
  /// @param errores  Objeto en el que se reporta que el elemento incumple la regla de
  ///                 validación.
  /// @param elemento Elemento del que se quiere determinar si cumple esta regla de
  ///                 validación.
  /// @return Una excepción [ViolacionReglaNegocioException] si el elemento incumple la regla
  ///         de negocio respectiva.
  Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, T elemento);
  
}
