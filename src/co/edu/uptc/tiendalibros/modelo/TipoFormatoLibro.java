//
package co.edu.uptc.tiendalibros.modelo;

/// Representa un tipo de formato para el [FormatoLibro] de un libro ([Libro]).
public enum TipoFormatoLibro {
  /// Representa la presentación de un libro en formato impreso.
  IMPRESO,
  
  /// Representa la presentación de un libro en formato digital.
  DIGITAL;

  @Override 
  public String toString() {
    return switch (this) {
      case IMPRESO -> "Impreso";
      case DIGITAL -> "Digital";
    };
  }
  
  /// Convierte un texto String a [TipoFormatoLibro].
  ///
  /// @param valor Texto String que se quiere convertir.
  /// @return El objeto [TipoFormatoLibro] correspondiente al texto.
  /// @throw IllegalArgumentException Si el texto no se pudo convertir a un objeto
  ///        [TipoFormatoLibro] válido.
  public static TipoFormatoLibro convertirString(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String.format("No se pudo convertir el valor '%s' a un tipo de formato de libro válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    // intentar convertir el valor dado
    try {
      return TipoFormatoLibro.valueOf(
        valor.trim()
          .toUpperCase()
      );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(mensaje);
    }
  }
}
