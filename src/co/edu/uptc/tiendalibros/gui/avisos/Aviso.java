//
package co.edu.uptc.tiendalibros.gui.avisos;

/// Representa un aviso que se le muestra al usuario.
public class Aviso {
  
  /// Crea un aviso de éxito con el mensaje dado.
  ///
  /// @param mensaje Mensaje que se quiere mostrar en el aviso.
  public static Aviso exito(String mensaje) {
    return new Aviso(TipoAviso.EXITO, mensaje);
  }
  
  /// Crea un aviso de información con el mensaje dado.
  ///
  /// @param mensaje Mensaje que se quiere mostrar en el aviso.
  public static Aviso informacion(String mensaje) {
    return new Aviso(TipoAviso.INFORMACION, mensaje);
  }
  
  /// Crea un aviso de advertencia con el mensaje dado.
  ///
  /// @param mensaje Mensaje que se quiere mostrar en el aviso.
  public static Aviso advertencia(String mensaje) {
    return new Aviso(TipoAviso.ADVERTENCIA, mensaje);
  }
  
  /// Crea un aviso de error con el mensaje dado.
  ///
  /// @param mensaje Mensaje que se quiere mostrar en el aviso.
  public static Aviso error(String mensaje) {
    return new Aviso(TipoAviso.ERROR, mensaje);
  }
  
  /// Tipo del aviso.
  private final TipoAviso tipoAviso;
  
  /// Mensaje que se quiere mostrar en el aviso.
  private final String mensaje;
  
  /// Crea un nuevo objeto de aviso.
  ///
  /// @param tipo    Tipo del aviso.
  /// @param mensaje Mensaje que se quiere mostrar en el aviso.
  protected Aviso(
    TipoAviso tipo,
    String mensaje
  ) {
    this.tipoAviso = tipo;
    this.mensaje = mensaje;
  }
  
  /// Obtiene el tipo de aviso.
  public TipoAviso getTipoAviso() { return this.tipoAviso; }
  
  /// Obtiene el mensaje del aviso.
  public String getMensaje() { return this.mensaje; }
  
}
