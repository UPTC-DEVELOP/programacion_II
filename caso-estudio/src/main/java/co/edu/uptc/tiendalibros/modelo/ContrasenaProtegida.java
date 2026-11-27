package co.edu.uptc.tiendalibros.modelo;

/// Representa una contraseña ya protegida y lista para guardarse en la base de datos.
///
/// Esta clase nunca guarda la contraseña original.
/// 
/// Esta clase siempre guarda el resultado de aplicar un algoritmo para encriptar la
/// contraseña.
/// 
/// La contraseña queda protegida o encriptada aplicando un `hash` y una `sal` cuyos
/// valores se guardan como valores Base64.
public final class ContrasenaProtegida {
  
  /// Resultado de proteger lka contraseña (hash PBKDF2), en valor Base64.
  private final String hash;
  
  /// Valor aleatorio que se usó para calcular el hash, en valor Base64. Cada usuario tiene
  /// su propio valor sal.
  private final String sal;
  
  /// Crea una contraseña protegida con el hash y la sal especificados.
  ///
  /// @param hash Hash de la contraseña, en valor Base64. No debe ser nulo ni estar en
  ///             blanco.
  /// @param sal  Sal que se usó para calcular el hash, en valor Base64. No debe ser nula ni
  ///             estar en blanco.
  /// @throws IllegalArgumentException Si el hash o la sal son nulos o están en blanco.
  public ContrasenaProtegida(
    String hash,
    String sal
  ) {
    // el valor hash no puede estar vacío
    if ((hash == null) || hash.isBlank()) {
      throw new IllegalArgumentException("El hash de la contraseña no puede estar vacío.");
    }
    // el valor sal no puede estar vacío
    if ((sal == null) || sal.isBlank()) {
      throw new IllegalArgumentException("La sal de la contraseña no puede estar vacía.");
    }
    // asignar el hash y la sal
    this.hash = hash;
    this.sal = sal;
  }
  
  /// Obtiene el hash de la contraseña, en valor Base64.
  ///
  /// @return Hash de la contraseña.
  public String getHash() { return this.hash; }
  
  /// Obtiene la sal que se usó para calcular el hash, en valor Base64.
  ///
  /// @return Sal de la contraseña.
  public String getSal() { return this.sal; }
  
  /// Obtiene el texto de esta contraseña protegida.
  /// 
  /// No muestra el hash ni la sal para que no aparezca en un registro de log.
  @Override
  public String toString() {
    return "ContrasenaProtegida[protegida]";
  }
}