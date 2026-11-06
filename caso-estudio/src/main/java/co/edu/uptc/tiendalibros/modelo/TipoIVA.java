//
package co.edu.uptc.tiendalibros.modelo;

/// Representa el tipo de IVA y su porcentaje respectivo. El porcentaje ya se dividió
/// entre 100.
public enum TipoIVA {
  
  /// Representa el IVA del 19% = 0.19.
  IVA_19("0.19"),
  
  /// Representa el IVA del 5% = 0.05.
  IVA_5("0.05");
  
  /// Valor del porcentaje de IVA ya divido entre 100.
  private final Dinero valor;
  
  /// Crea un nuevo objeto enum [TipoIVA] a partir del valor de texto ya divido entre 100.
  ///
  /// @param valor Porcentaje del IVA ya dividido entre 100.
  private TipoIVA(String valor) {
    this.valor = new Dinero(valor);
  }
  
  /// Calcula el valor del IVA para el monto especificado.
  ///
  /// @param monto Monto del que se quiere calcular el IVA respectivo.
  /// @return Valor del IVA para el monto especificado.
  public Dinero calcularIVA(Dinero monto) {
    return monto.multiplicar(this.valor);
  }
  
  /// Calcula el precio final de un producto según su precio base y tipo de IVA.
  ///
  /// precio final = precio base + IVA * precio base
  /// 
  /// @param precioBase Precio base del producto.
  /// @return Precio de venta del producto que incluye IVA.
  public Dinero calcularPrecioFinal(Dinero precioBase) {
    
    return precioBase
      .sumar(
        this.calcularIVA(precioBase)
      );
  }
  
  /// Convierte un texto String a [TipoIVA].
  ///
  /// @param valor Texto String que se quiere convertir.
  /// @return El objeto [TipoIVA] correspondiente al texto.
  /// @throw IllegalArgumentException Si el texto no se pudo convertir a un objeto
  ///        [TipoIVA] válido.
  public static TipoIVA convertirString(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String.format("No se pudo convertir el valor '%s' a un tipo de IVA válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    // intentar convertir el valor dado
    try {
      return TipoIVA.valueOf(
        valor.trim()
          .toUpperCase()
      );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(mensaje);
    }
  }
}