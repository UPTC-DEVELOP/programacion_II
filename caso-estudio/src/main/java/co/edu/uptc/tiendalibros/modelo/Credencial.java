package co.edu.uptc.tiendalibros.modelo;

import java.util.Arrays;

/// Representa los datos que el usuario entrega para iniciar sesión: correo electrónico y
/// contraseña.
///
/// Mantiene la contraseña en claro tal cual sin protegerla. Cuando se termine de usar una
/// credencial, se debe invocar al método [#borrarContrasena()] para que la contraseña no
/// esté en memoria.
///
/// En la base de datos se guarda la [ContrasenaProtegida].
public class Credencial {
  
  /// Correo electrónico del usuario. Es el nombre de usuario.
  private final String correoElectronico;
  
  /// Contraseña que escribió el usuario.
  private final char[] contrasena;
  
  /// Crea una credencial con el correo y la contraseña especificados.
  ///
  /// @param correoElectronico Correo electrónico del usuario. Si es nulo se usa un texto
  ///                          vacío.
  /// @param contrasena        Contraseña sin encriptar o proteger. Si es nula se usa un
  ///                          arreglo vacío.
  public Credencial(
    String correoElectronico,
    char[] contrasena
  ) {
    this.correoElectronico = (correoElectronico == null) ? "" : correoElectronico;
    this.contrasena = (contrasena == null) ? new char[0] : contrasena;
  }
  
  /// Obtiene el correo electrónico del usuario.
  ///
  /// @return Correo electrónico del usuario.
  public String getCorreoElectronico() { return this.correoElectronico; }
  
  /// Obtiene la contraseña en claro tal cual sin protegerla.
  ///
  /// Devuelve el mismo arreglo char[].
  ///
  /// @return Contraseña en claro.
  public char[] getContrasena() { return this.contrasena; }
  
  /// Borra la contraseña en memoria haciendo que la credencial ya no sirva para autenticar
  /// al usuario.
  public void borrarContrasena() {
    Arrays.fill(this.contrasena, '\0');
  }
  
  @Override
  public String toString() {
    return "Credencial[datos protegidos]";
  }
}