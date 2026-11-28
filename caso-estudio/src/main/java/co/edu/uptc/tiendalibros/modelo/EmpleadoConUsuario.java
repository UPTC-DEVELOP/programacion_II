//
package co.edu.uptc.tiendalibros.modelo;

import co.edu.uptc.tiendalibros.interfaces.IElemento;

/// Representa un empleado junto con su cuenta de usuario.
///
/// Esta clase se utiliza en la capa de negocio cuando el administrador gestiona empleados.
/// 
/// Esta clase incluye los datos personales del empleado ([Empleado]) y los datos de su
/// cuenta ([Usuario]).
///
/// El ID de este elemento es el ID del empleado.
public class EmpleadoConUsuario implements IElemento {
  
  /// Datos personales del empleado.
  private final Empleado empleado;
  
  /// Cuenta de usuario del empleado.
  private final Usuario usuario;
  
  /// Crea un empleado con su cuenta de usuario.
  ///
  /// @param empleado Datos personales del empleado.
  /// @param usuario  Cuenta de usuario del empleado.
  /// @throws IllegalArgumentException Si algún argumento es nulo.
  public EmpleadoConUsuario(
    Empleado empleado,
    Usuario usuario
  ) {
    if ((empleado == null) || (usuario == null)) {
      throw new IllegalArgumentException("El empleado y su cuenta de usuario no pueden ser nulos.");
    }
    this.empleado = empleado;
    this.usuario = usuario;
  }
  
  /// Obtiene el ID del empleado.
  ///
  /// @return ID del empleado. O, `null` si el empleado aún no se ha guardado.
  @Override
  public Long getId() { return this.empleado.getId(); }
  
  /// Obtiene los datos personales del empleado.
  ///
  /// @return Datos personales del empleado.
  public Empleado getEmpleado() { return this.empleado; }
  
  /// Obtiene la cuenta de usuario del empleado.
  ///
  /// @return Cuenta de usuario del empleado.
  public Usuario getUsuario() { return this.usuario; }
  
  /// Muestra solo el ID: los datos personales no deben llegar al log.
  @Override
  public String toString() {
    return String.format("EmpleadoConUsuario[id=%d]", getId());
  }
}