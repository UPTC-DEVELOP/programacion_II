//
package co.edu.uptc.tiendalibros.modelo;

/// Representa el estado de un libro en el catálogo de la tienda.
public enum EstadoLibroCatalogo {
  
  /// El libro está pendiente de terminar de crearse. Abreviatura: P.
  PENDIENTE("P"),
  /// El libro está disponible para la venta. Abreviatura: D.
  DISPONIBLE("D"),
  /// El libro no está disponible para venta. Abreviatura: S.
  SUSPENDIDO("S");
  
  /// Abreviatura que se usa para representar el estado del libro.
  private String abreviatura;
  
  /// Crea un nuevo objeto del enum [EstadoLibroCatalogo].
  ///
  /// @param abreviatura Abreviatura que se usa para representar el estado del libro.
  private EstadoLibroCatalogo(String abreviatura) {
    this.abreviatura = abreviatura;
  }
  
  /// Obtiene la Abreviatura que se usa para representar el estado del libro.
  ///
  /// @return Abreviatura que se usa para representar el estado del libro.
  public String getAbreviatura() { return this.abreviatura; }
  
  /// Convierte un texto String a [EstadoLibroCatalogo].
  ///
  /// @param valor Texto abreviatura que se quiere convertir.
  /// @return El objeto [EstadoLibroCatalogo] correspondiente al texto.
  /// @throws IllegalArgumentException Si el texto no se pudo convertir a un objeto
  ///                                  [EstadoLibroCatalogo] válido.
  public static EstadoLibroCatalogo convertirAbreviatura(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String
      .format("No se pudo convertir el valor '%s' a un estado de libro en el catálogo válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    
    // intentar convertir el valor dado
    return switch (valor.toUpperCase()) {
      case "P" -> EstadoLibroCatalogo.PENDIENTE;
      case "D" -> EstadoLibroCatalogo.DISPONIBLE;
      case "S" -> EstadoLibroCatalogo.SUSPENDIDO;
      default  ->
        throw new IllegalArgumentException(mensaje);
    };
    
  }
  
  /// Convierte un texto String a [EstadoLibroCatalogo].
  ///
  /// @param valor Texto String que se quiere convertir.
  /// @return El objeto [EstadoLibroCatalogo] correspondiente al texto.
  /// @throws IllegalArgumentException Si el texto no se pudo convertir a un objeto
  ///                                  [EstadoLibroCatalogo] válido.
  public static EstadoLibroCatalogo convertirString(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String
      .format("No se pudo convertir el valor '%s' a un estado de libro en el catálogo válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    // intentar convertir el valor dado
    try {
      return EstadoLibroCatalogo.valueOf(
        valor.trim()
          .toUpperCase()
      );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(mensaje);
    }
  }
}
