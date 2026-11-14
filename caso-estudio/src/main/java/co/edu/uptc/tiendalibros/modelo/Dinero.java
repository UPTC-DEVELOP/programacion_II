//
package co.edu.uptc.tiendalibros.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/// Representa una cantidad de dinero en una moneda. Es un BigDecimal configurado con
/// redondeo.
/// 
/// Es un objeto tipo inmutable porque ninguna operación modifica el objeto y toda
/// operación devuelve un nuevo objeto [Dinero].
/// 
/// El redondeo usado es `RoundingMode.HALF_EVEN`.
///
/// @author Andrés Ávila
public class Dinero implements Comparable<Dinero> {
  
  /// Modo de redondeo que se aplica cada vez que se crea un objeto [Dinero].
  public static final RoundingMode MODO_REDONDEO = RoundingMode.HALF_EVEN;
  
  /// Código ISO 4217 de la moneda predeterminada.
  public static final String CODIGO_MONEDA = "COP";
  
  /// Locale predeterminado para formatear cantidades de dinero.
  public static final Locale LOCALE_PREDETERMINADO = Locale.of("es", "CO");
  
  /// Moneda predeterminada basada en el código de la moneda.
  public static final Currency MONEDA = Currency.getInstance(Dinero.CODIGO_MONEDA);
  
  /// Representa la cantidad cero (0.00) de dinero.
  public static final Dinero CERO = new Dinero("0.00");
  
  /// Representa la cantidad uno (1.00) de dinero.
  public static final Dinero UNO = new Dinero("1.00");
  
  /// Cantidad de dinero.
  private final BigDecimal cantidad;
  
  /// Moneda que se usa para el dinero.
  private final Currency moneda;
  
  /// Crea un nuevo objeto [Dinero] con la cantidad cero y la moneda predeterminada.
  public Dinero() {
    this(BigDecimal.ZERO, Dinero.MONEDA);
  }
  
  /// Crea un objeto [Dinero] con la cantidad especificada y la moneda predeterminada.
  ///
  /// @param cantidad Cantidad de dinero.
  public Dinero(BigDecimal cantidad) {
    this(cantidad, Dinero.MONEDA);
  }
  
  /// Crea un nuevo objeto [Dinero] a partir de los valores especificados.
  ///
  /// @param cantidad Cantidad de dinero.
  /// @param moneda   Moneda que se usa para el dinero.
  /// 
  /// @throws IllegalArgumentException Si ocurren estos casos:
  /// - `cantidad` es nulo
  /// - `moneda` es nulo
  /// - `moneda` no tiene números decimales o fracciones
  public Dinero(
    BigDecimal cantidad,
    Currency moneda
  ) {
    // la cantidad no puede ser un nulo
    if (cantidad == null) {
      throw new IllegalArgumentException("Dinero: el valor de cantidad no puede ser vacío.");
    }
    // la moneda no puede ser vacía
    if (moneda == null) {
      throw new IllegalArgumentException("Dinero: el valor de moneda no puede ser vacío.");
    }
    // toda moneda debe tener dígitos decimales o fraccionarios para representar, por ejemplo,
    // centavos
    if (moneda.getDefaultFractionDigits() < 0) {
      throw new IllegalArgumentException(
        String.format("Dinero: la moneda '%s' no tiene decimales definidos.", moneda.getCurrencyCode())
      );
    }
    
    this.moneda = moneda;
    // establecer la escala y el redondeo.
    this.cantidad = cantidad.setScale(moneda.getDefaultFractionDigits(), Dinero.MODO_REDONDEO);
  }
  
  /// Crea un objeto [Dinero] con la cantidad especificada y la moneda predeterminada.
  ///
  /// @param cantidad Representación textual de la cantidad de dinero.
  /// @throws IllegalArgumentException Si `cantidad` es nulo. de [BigDecimal]
  /// @throws NumberFormatException    Si `cantidad` no es un número válido.
  public Dinero(
    String cantidad
  ) {
    this(cantidad, Dinero.MONEDA);
  }
  
  /// Crea un nuevo objeto [Dinero] a partir de los valores especificados.
  ///
  /// @param cantidad                 Representación textual de la cantidad de dinero.
  /// @param moneda                   Moneda que se usa para el dinero.
  /// @param IllegalArgumentException Si `cantidad` es nulo.
  /// @throws NumberFormatException Si el valor de `cantidad` debe ser un representación
  ///                               válida de [BigDecimal]
  public Dinero(
    String cantidad,
    Currency moneda
  ) {
    // verificar que el texto cantidad no sea nulo.
    if (cantidad == null) {
      throw new IllegalArgumentException("Dinero: el texto de cantidad no puede ser nulo.");
    }
    
    // primero intentar crear un número BigDecimal a partir de la representación textual.
    BigDecimal laCantidad = BigDecimal.ZERO;
    try {
      laCantidad = new BigDecimal(cantidad);
    } catch (NumberFormatException excepcion) {
      throw new NumberFormatException(
        String.format(
          "Dinero: el valor de cantidad '%s' debe ser un representación válida de BigDecimal.",
          cantidad
        )
      );
    }
    this(laCantidad, moneda);
  }
  
  /// Obtiene la cantidad de dinero.
  ///
  /// @return Cantidad de dinero.
  public BigDecimal getCantidad() { return this.cantidad; }
  
  /// Obtiene la moneda que se usa para el dinero.
  ///
  /// @return Moneda que se usa para el dinero.
  public Currency getMoneda() { return this.moneda; }
  
  /// Calcula la suma de este objeto [Dinero] con el objeto [Dinero] especificado.
  /// 
  /// El objeto `otro` no debe ser nulo.
  /// 
  /// El objeto `otro` debe tener la misma moneda que este objeto.
  /// 
  /// El resultado es un nuevo objeto [Dinero].
  ///
  /// @param otro El otro objeto [Dinero] que se quiere sumar.
  /// @return La suma de ambos objetos: this + otro.
  /// @throws IllegalArgumentException Si el objeto `otro` tiene una moneda diferente si
  ///                                  este objeto.
  public Dinero sumar(Dinero otro) {
    validarMoneda(otro);
    return new Dinero(this.cantidad.add(otro.getCantidad()), this.moneda);
  }
  
  /// Calcula la resta de este objeto [Dinero] con el objeto [Dinero] especificado.
  /// 
  /// El objeto `otro` no debe ser nulo.
  /// 
  /// El objeto `otro` debe tener la misma moneda que este objeto.
  /// 
  /// El resultado es un nuevo objeto [Dinero].
  ///
  /// @param otro El otro objeto [Dinero] que se quiere restar.
  /// @return La resta de ambos objetos: this - otro.
  /// @throws IllegalArgumentException Si el objeto `otro` tiene una moneda diferente si
  ///                                  este objeto.
  public Dinero restar(Dinero otro) {
    validarMoneda(otro);
    return new Dinero(this.cantidad.subtract(otro.getCantidad()), this.moneda);
  }
  
  /// Calcula la multiplicación de este objeto [Dinero] por un número entero de unidades.
  /// 
  /// @param unidades Número de unidades por el que se multiplica.
  /// @return El resultado de la multiplicación: this × unidades.
  public Dinero multiplicar(int unidades) {
    return multiplicar(BigDecimal.valueOf(unidades));
  }
  
  /// Calcula la multiplicación de este objeto [Dinero] por un factor decimal.
  /// 
  /// @param factor Factor por el que se multiplica.
  /// @return El resultado de la multiplicación: this × factor.
  /// @throws IllegalArgumentException Si `factor` es nulo.
  public Dinero multiplicar(BigDecimal factor) {
    // No se puede hacer la operación si el factor es nulo
    if (factor == null) {
      throw new IllegalArgumentException("Dinero: el parámetro 'factor' no puede ser nulo.");
    }
    return new Dinero(this.cantidad.multiply(factor), this.moneda);
  }
  
  /// Divide este objeto [Dinero] entre un divisor decimal.
  /// 
  /// @param divisor Divisor. No puede ser nulo ni cero.
  /// @return El resultado de la división: this ÷ divisor.
  /// @throws IllegalArgumentException Si `divisor` es nulo.
  /// @throws ArithmeticException      Si `divisor` es cero
  public Dinero dividir(BigDecimal divisor) {
    // No se puede hacer la operación si el divisor es nulo
    if (divisor == null) {
      throw new IllegalArgumentException("Dinero: el divisor no puede ser nulo.");
    }
    // tener en cuenta el redondeo
    return new Dinero(this.cantidad.divide(divisor, this.cantidad.scale(), Dinero.MODO_REDONDEO), this.moneda);
  }
  
  /// Determina si esta cantidad es exactamente cero.
  public boolean esCero() {
    return this.cantidad.signum() == 0;
  }
  
  /// Determina si esta cantidad es mayor que cero.
  public boolean esPositivo() {
    return this.cantidad.signum() > 0;
  }
  
  /// Determina si esta cantidad es menor que cero.
  public boolean esNegativo() {
    return this.cantidad.signum() < 0;
  }
  
  /// Compara esta cantidad con otra.
  /// 
  /// Permite que los objetos [Dinero] se puedan ordenar.
  ///
  /// El objeto `otro` no es nulo y tiene la misma moneda que este objeto.
  ///
  /// @return Un número negativo, cero o positivo, si esta cantidad es menor, igual o mayor
  ///         que la de `otro`.
  /// @throws IllegalArgumentException Si `otro` es nulo, o si `otro` tiene una
  ///                                  moneda diferente.
  @Override
  public int compareTo(Dinero otro) {
    validarMoneda(otro);
    return this.cantidad.compareTo(otro.cantidad);
  }
  
  /// Indica si this &lt; otro.
  public boolean esMenorQue(Dinero otro) {
    return compareTo(otro) < 0;
  }
  
  /// Indica si this &lt;= otro.
  public boolean esMenorOIgualQue(Dinero otro) {
    return compareTo(otro) <= 0;
  }
  
  /// Indica si this &gt; otro.
  public boolean esMayorQue(Dinero otro) {
    return compareTo(otro) > 0;
  }
  
  /// Indica si this &gt;= otro.
  public boolean esMayorOIgualQue(Dinero otro) {
    return compareTo(otro) >= 0;
  }
  
  /// Obtiene un representación textual de la cantidad de dinero usando el objeto locale
  /// predeterminado de la moneda.
  public String formatear() {
    return this.formatear(Dinero.LOCALE_PREDETERMINADO);
  }
  
  /// Obtiene un representación textual de la cantidad de dinero usando el objeto
  /// locale especificado.
  public String formatear(Locale locale) {
    NumberFormat formateador = NumberFormat.getCurrencyInstance(locale);
    formateador.setCurrency(this.moneda);
    return formateador.format(this.cantidad);
  }
  
  /// Valida si el objeto [Dinero] especificado tienen la misma moneda.
  ///
  /// @param otro El otro objeto [Dinero] para validar si son de la misma moneda.
  /// @throws IllegalArgumentException Si el objeto `otro` es nulo.
  /// @throws IllegalArgumentException Si el objeto `otro` tiene una moneda diferente si
  ///                                  este objeto.
  private void validarMoneda(Dinero otro) {
    
    // No se puede hacer una operación si el otro operando es nulo
    if (otro == null) {
      throw new IllegalArgumentException("Dinero: el otro valor de dinero no puede ser nulo.");
    }
    
    // Si este objeto dinero tiene una moneda diferente al otro objeto dinero se lanza una
    // excepción.
    if (!this.moneda.equals(otro.moneda)) {
      throw new IllegalArgumentException(
        String.format(
          "No se puede realizar la operación con monedas diferentes: %s vs %s",
          this.moneda.getCurrencyCode(),
          otro.moneda.getCurrencyCode()
        )
      );
    }
  }
  
  /// Determina si este objeto [Dinero] es igual a otro objeto [Dinero] son iguales. Dos
  /// objetos [Dinero] son iguales si tienen la misma moneda y la misma cantidad.
  @Override
  public boolean equals(Object objeto) {
    if (this == objeto) {
      return true;
    }
    if (objeto instanceof Dinero otro) {
      return (this.moneda.equals(otro.getMoneda())) && (this.cantidad.equals(otro.getCantidad()));
    }
    return false;
  }
  
  /// Calcula el código hash.
  @Override
  public int hashCode() {
    // usar los mismos atributos cantidad y moneda del método equals
    return Objects.hash(this.cantidad, this.moneda);
  }
  
  /// Representación textual de este objeto [Dinero]. Ejemplo: `COP 119000.00`.
  @Override
  public String toString() {
    return String.format("%s %s", this.moneda.getCurrencyCode(), this.cantidad.toPlainString());
  }

  /// Crea un objeto [Dinero] a partir de un objeto.
  ///
  /// @param valor Valor que se quiere convertir a [Dinero].
  /// @return Un objeto [Dinero] equivalente al `valor`, o `Dinero.CERO` si `valor` no se
  ///         puede convertir.
  public static Dinero desde(Object valor) {
    // crear un objeto Dinero si el valor es BigDecimal o si es Number
    // si falla la conversión BigDecimal, devolver cero
    Dinero elDinero = Dinero.CERO;
    
    try {
      if (valor instanceof BigDecimal decimal) {
        elDinero = new Dinero(decimal);
      }
      else if (valor instanceof Number numero) {
        elDinero = new Dinero(new BigDecimal(numero.toString()));
      }
    } catch (NumberFormatException _excepcion) {
      // en lugar de lanzar una excepción, devolver un cero
      return Dinero.CERO;
    }
    
    return elDinero;
  }
}
