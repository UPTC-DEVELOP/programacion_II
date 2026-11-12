//
package co.edu.uptc.tiendalibros.modelo;

import java.math.BigDecimal;
import java.util.Locale;

/// Representa el tipo de IVA y su porcentaje respectivo. El porcentaje ya se dividió
/// entre 100.
public enum TipoIVA {
  
  /// Representa el IVA del 19% = 0.19.
  IVA_19("IVA_19", "0.19"),
  
  /// Representa el IVA del 5% = 0.05.
  IVA_5("IVA_5", "0.05");
  
  /// Abreviatura que se usa para representar el IVA.
  private final String abreviatura;
  
  /// Valor del porcentaje de IVA ya divido entre 100.
  private final BigDecimal porcentaje;
  
  /// Crea un nuevo objeto enum [TipoIVA] a partir del valor de texto ya divido entre 100.
  ///
  /// @param abreviatura Abreviatura que se usa para representar el IVA.
  /// @param valor       Porcentaje del IVA ya dividido entre 100.
  private TipoIVA(
    String abreviatura,
    String valor
  ) {
    this.abreviatura = abreviatura;
    this.porcentaje = new BigDecimal(valor);
  }
  
  /// Calcula el valor del IVA para el monto especificado.
  ///
  /// @param monto Monto del que se quiere calcular el IVA respectivo.
  /// @return Valor del IVA para el monto especificado.
  public Dinero calcularIVA(Dinero monto) {
    return monto.multiplicar(this.porcentaje);
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
  
  /// Obtiene la Abreviatura que se usa para representar el IVA.
  ///
  /// @return Abreviatura que se usa para representar el IVA.
  public String getAbreviatura() { return this.abreviatura; }
  
  /// Convierte un texto String a [TipoIVA].
  ///
  /// @param valor Texto abreviatura que se quiere convertir.
  /// @return El objeto [TipoIVA] correspondiente al texto.
  /// @throws IllegalArgumentException Si el texto no se pudo convertir a un objeto
  ///                                  [TipoIVA] válido.
  public static TipoIVA convertirAbreviatura(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String
      .format("No se pudo convertir el porcentaje '%s' a un porcentaje de IVA válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    
    // intentar convertir el porcentaje dado, considerando un alfabeto neutro
    return switch (valor.trim()
      .toUpperCase(Locale.ROOT)) {
      case "IVA_19" -> TipoIVA.IVA_19;
      case "IVA_5"  -> TipoIVA.IVA_5;
      default       ->
        throw new IllegalArgumentException(mensaje);
    };
    
  }
  
  /// Convierte un texto String a [TipoIVA].
  ///
  /// @param valor Texto String que se quiere convertir.
  /// @return El objeto [TipoIVA] correspondiente al texto.
  /// @throws IllegalArgumentException Si el texto no se pudo convertir a un objeto
  ///                                  [TipoIVA] válido.
  public static TipoIVA convertirString(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String.format("No se pudo convertir el porcentaje '%s' a un tipo de IVA válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    // intentar convertir el porcentaje dado, considerando un alfabeto neutro
    try {
      return TipoIVA.valueOf(
        valor.trim()
          .toUpperCase(Locale.ROOT)
      );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(mensaje);
    }
  }
}