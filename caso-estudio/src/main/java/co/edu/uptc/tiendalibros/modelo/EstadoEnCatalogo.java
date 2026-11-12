//
package co.edu.uptc.tiendalibros.modelo;

/// Representa el estado de un formato de libro en el catálogo de la tienda.
public enum EstadoEnCatalogo {
  
  /// El formato del libro está pendiente de terminar de crearse. Abreviatura: P.
  PENDIENTE("P"),
  /// El formato del libro está disponible para la venta. Abreviatura: D.
  DISPONIBLE("D"),
  /// El formato del libro no está disponible para venta. Abreviatura: S.
  SUSPENDIDO("S");
  
  /// Abreviatura que se usa para representar el estado del formato del libro.
  private final String abreviatura;
  
  /// Crea un nuevo objeto del enum [EstadoEnCatalogo].
  ///
  /// @param abreviatura Abreviatura que se usa para representar el estado del formato
  ///                    del libro.
  private EstadoEnCatalogo(String abreviatura) {
    this.abreviatura = abreviatura;
  }
  
  /// Obtiene la Abreviatura que se usa para representar el estado del formato del libro.
  ///
  /// @return Abreviatura que se usa para representar el estado del formato del libro.
  public String getAbreviatura() { return this.abreviatura; }
  
  /// Convierte un texto String a [EstadoEnCatalogo].
  ///
  /// @param valor Texto abreviatura que se quiere convertir.
  /// @return El objeto [EstadoEnCatalogo] correspondiente al texto.
  /// @throws IllegalArgumentException Si el texto no se pudo convertir a un objeto
  ///                                  [EstadoEnCatalogo] válido.
  public static EstadoEnCatalogo convertirAbreviatura(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String
      .format("No se pudo convertir el valor '%s' a un estado del formato del libro en el catálogo válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    
    // intentar convertir el valor dado
    return switch (valor.toUpperCase()) {
      case "P" -> EstadoEnCatalogo.PENDIENTE;
      case "D" -> EstadoEnCatalogo.DISPONIBLE;
      case "S" -> EstadoEnCatalogo.SUSPENDIDO;
      default  ->
        throw new IllegalArgumentException(mensaje);
    };
    
  }
  
  /// Convierte un texto String a [EstadoEnCatalogo].
  ///
  /// @param valor Texto String que se quiere convertir.
  /// @return El objeto [EstadoEnCatalogo] correspondiente al texto.
  /// @throws IllegalArgumentException Si el texto no se pudo convertir a un objeto
  ///                                  [EstadoEnCatalogo] válido.
  public static EstadoEnCatalogo convertirString(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String
      .format("No se pudo convertir el valor '%s' a un estado de libro en el catálogo válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    // intentar convertir el valor dado
    try {
      return EstadoEnCatalogo.valueOf(
        valor.trim()
          .toUpperCase()
      );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(mensaje);
    }
  }
}
