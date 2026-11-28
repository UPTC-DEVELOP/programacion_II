//
package co.edu.uptc.tiendalibros.interfaces;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Empleado;
import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.modelo.Usuario;

/// Representa el repositorio de datos que almacena los empleados con su cuenta de
/// usuario.
///
/// Un empleado siempre está vinculado a su cuenta de usuario.
public interface IRepositorioEmpleado extends IRepositorioSoloConsultas<EmpleadoConUsuario> {
  
  /// Busca el empleado con el documento de identidad especificado.
  ///
  /// @param tipoDocumento   Tipo de documento de identidad.
  /// @param numeroDocumento Número del documento de identidad.
  /// @return El empleado encontrado. O, un valor vacío si no existe, o si algún argumento
  ///         es nulo.
  Optional<Empleado> buscarPorDocumento(String tipoDocumento, String numeroDocumento);
  
  /// Crea un empleado nuevo: guarda su cuenta de usuario, sus roles y sus datos de empleado
  /// en una sola transacción.
  /// 
  /// Si algún paso falla, no se guarda nada.
  ///
  /// @param usuario  Cuenta de usuario nueva sin ID.
  /// @param empleado Datos del empleado nuevo sin ID.
  /// @return El empleado guardado, con su ID y el ID de su cuenta.
  /// @throws IllegalArgumentException Si algún argumento es nulo, o si ya tiene ID.
  Empleado crear(Usuario usuario, Empleado empleado);
  
  /// Actualiza un empleado que ya existe: guarda sus datos personales, su cuenta de usuario
  /// y su rol en una sola transacción.
  /// 
  /// Si algún paso falla, no se guarda nada.
  ///
  /// @param usuario  Cuenta de usuario existente con su ID.
  /// @param empleado Datos del empleado existente con su ID y con el ID de la cuenta.
  /// @return El empleado actualizado con su cuenta de usuario. O, un valor vacío si el
  ///         empleado ya no existe.
  /// @throws IllegalArgumentException Si algún argumento es nulo o no tiene ID.
  Optional<EmpleadoConUsuario> actualizar(Usuario usuario, Empleado empleado);
  
  /// Elimina el empleado con el ID especificado.
  /// 
  /// El repositorio también eliminará la cuenta de usuario del empleado y sus roles.
  ///
  /// @param idEmpleado ID del empleado, pero no el ID de su cuenta de usuario.
  /// @return `true` si el empleado se eliminó. `false` si no se encontró.
  /// @throws IllegalArgumentException Si el ID es nulo.
  boolean eliminar(Long idEmpleado);
}