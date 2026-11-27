package co.edu.uptc.tiendalibros.negocio.seguridad;

import co.edu.uptc.tiendalibros.modelo.Permiso;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.modelo.Usuario;

/// Representa la sesión de usuario de la aplicación: guarda el usuario que inició sesión.
///
/// Cada ejecución del programa tiene una sola sesión de usuario.
/// 
/// Únicamente [GestionSeguridad] puede iniciar o cerrar una sesión.
///
/// Los permisos del usuario se leen al iniciar sesión.
/// 
/// Si un administrador modifica la cuenta y sus roles, ese cambio se verá cuando el
/// usuario inicie sesión la próxima vez.
public class SesionUsuario {
  
  /// Usuario que inició sesión. Es `null` si no hay sesión iniciada.
  private Usuario usuario;
  
  /// Crea una sesión sin usuario.
  public SesionUsuario() {
    this.usuario = null;
  }
  
  /// Obtiene el usuario que inició sesión.
  ///
  /// @return El usuario de la sesión. O, `null` si no hay sesión iniciada.
  public Usuario getUsuario() { return this.usuario; }
  
  /// Indica si hay una sesión iniciada.
  ///
  /// @return `true` si hay un usuario con sesión iniciada.
  public boolean haySesion() {
    return this.usuario != null;
  }
  
  /// Indica si el usuario de la sesión tiene el permiso especificado.
  ///
  /// Si no hay sesión iniciada no se concede el permiso.
  ///
  /// @param permiso Permiso que se quiere consultar.
  /// @return `true` si hay sesión y el usuario tiene el permiso.
  public boolean tienePermiso(Permiso permiso) {
    return haySesion() && this.usuario.tienePermiso(permiso);
  }
  
  /// Indica si el usuario de la sesión tiene el rol especificado.
  ///
  /// @param rol Rol que se quiere consultar.
  /// @return `true` si hay sesión y el usuario tiene el rol.
  public boolean tieneRol(Rol rol) {
    return haySesion() && this.usuario.tieneRol(rol);
  }
  
  /// Inicia la sesión con el usuario especificado.
  ///
  /// @param usuario Usuario que inició sesión.
  void iniciar(Usuario usuario) {
    this.usuario = usuario;
  }
  
  /// Cierra la sesión y hace null al usuario para quitar al usuario.
  void cerrar() {
    this.usuario = null;
  }
}