//
package co.edu.uptc.tiendalibros.negocio.empleado;

import java.util.Arrays;

import co.edu.uptc.tiendalibros.interfaces.IElemento;
import co.edu.uptc.tiendalibros.modelo.Rol;

/// Representa los datos que el administrador ingresa para crear o editar un empleado y su
/// cuenta de usuario.
///
/// Si `id` es nulo significa que los datos son de un empleado nuevo.
/// 
/// Si `id` tiene valor significa que es el ID de un empleado que ya existe.
///
/// Contraseñas: los arreglos `contrasena` y `confirmacion` contienen contraseñas en claro
/// (sin proteger).
///
/// @param id                ID del empleado.
/// @param tipoDocumento     Tipo de documento de identidad.
/// @param numeroDocumento   Número del documento de identidad.
/// @param nombres           Nombres del empleado.
/// @param apellidos         Apellidos del empleado.
/// @param correoElectronico Correo electrónico de la cuenta. Es su nombre de usuario.
/// @param rol               Rol de la cuenta.
/// @param habilitado        `true` si la cuenta puede iniciar sesión y trabajar en el
///                          programa.
/// @param contrasena        Contraseña en claro (sin proteger).
/// @param confirmacion      Confirmación de la contraseña en claro (sin proteger).
public record DatosEmpleado(
  Long id,
  String tipoDocumento,
  String numeroDocumento,
  String nombres,
  String apellidos,
  String correoElectronico,
  Rol rol,
  boolean habilitado,
  char[] contrasena,
  char[] confirmacion
) implements IElemento {
  
  /// Obtiene el ID del empleado.
  /// 
  /// Es nulo si el empleado es nuevo.
  ///
  /// @return ID del empleado.
  @Override
  public Long getId() { return this.id; }
  
  /// Crea una copia de estos datos del empleado con sus valores texto sin espacios al
  /// inicio ni al final.
  ///
  /// Las contraseñas no se limpian.
  /// 
  /// @return Copia con los textos limpios.
  public DatosEmpleado conTextosLimpios() {
    String tipo = limpiar(this.tipoDocumento);
    
    return new DatosEmpleado(
      this.id,
      (tipo == null) ? null : tipo.toUpperCase(),
      limpiar(this.numeroDocumento),
      limpiar(this.nombres),
      limpiar(this.apellidos),
      limpiar(this.correoElectronico),
      this.rol,
      this.habilitado,
      this.contrasena,
      this.confirmacion
    );
  }
  
  /// Borra de la memoria la contraseña y su confirmación.
  public void borrarContrasenas() {
    if (this.contrasena != null) {
      Arrays.fill(this.contrasena, '\0');
    }
    if (this.confirmacion != null) {
      Arrays.fill(this.confirmacion, '\0');
    }
  }
  
  /// Muestra solo el ID: los datos personales y las contraseñas no deben llegar al log.
  @Override
  public String toString() {
    return String.format("DatosEmpleado[id=%d]", this.id);
  }
  
  /// Quita los espacios al inicio y al final de un texto.
  ///
  /// @param texto Texto que se quiere limpiar. Puede ser nulo.
  /// @return El texto limpio, o `null` si el texto era nulo.
  private static String limpiar(String texto) {
    return (texto == null) ? null : texto.trim();
  }
}