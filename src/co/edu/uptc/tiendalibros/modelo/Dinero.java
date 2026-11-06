//
package co.edu.uptc.tiendalibros.modelo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/// Representa dinero. Es un BigDecimal configurado con redondeo.
///
/// @author Andrés Ávila
public class Dinero {
  
  /// Contexto para las operaciones matemáticas.
  public static MathContext CONTEXTO_DINERO = new MathContext(16, RoundingMode.HALF_EVEN);
  
  /// Código ISO 4217 de la moneda predeterminada.
  public static String CODIGO_MONEDA = "COP";
  
  /// Locale predeterminado para formatear cantidades de dinero.
  public static Locale LOCALE_PREDETERMINADO = Locale.of("es", "CO");
  
  /// Moneda predeterminada basada en el código de la moneda.
  public static Currency MONEDA = Currency.getInstance(Dinero.CODIGO_MONEDA);
  
  /// Formateador de moneda.
  public static NumberFormat FORMATEADOR_MONEDA = null;
  
  static {
    Dinero.FORMATEADOR_MONEDA = NumberFormat.getCurrencyInstance(Dinero.LOCALE_PREDETERMINADO);
    Dinero.FORMATEADOR_MONEDA.setCurrency(Dinero.MONEDA);
  }
  
  /// Representa la cantidad cero (0.00) de dinero.
  public static final Dinero CERO = new Dinero("0.00");
  
  /// Representa la cantidad uno (1.00) de dinero.
  public static final Dinero UNO = new Dinero("1.00");
  
  /// Cantidad de dinero.
  private BigDecimal cantidad;
  
  /// Moneda que se usa para el dinero.
  private Currency moneda;
  
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
  public Dinero(
    BigDecimal cantidad,
    Currency moneda
  ) {
    // la cantidad no puede ser un nulo
    if (cantidad == null) {
      throw new IllegalArgumentException("Moneda: el valor de cantidad no puede ser vacío.");
    }
    // la moneda no puede ser vacía
    if (moneda == null) {
      throw new IllegalArgumentException("Moneda: el valor de moneda no puede ser vacío.");
    }
    
    this.setMoneda(moneda);
    
    // establecer la escala y el redondeo.
    // la escala es el número de decimales que puede depender de la moneda.
    this.cantidad = cantidad.setScale(moneda.getDefaultFractionDigits(), Dinero.CONTEXTO_DINERO.getRoundingMode());
  }
  
  /// Crea un objeto [Dinero] con la cantidad especificada y la moneda predeterminada.
  ///
  /// @param cantidad Representación textual de la cantidad de dinero.
  /// @throws NumberFormatException Si el valor de cantidad debe ser un representación válida
  ///                               de [BigDecimal]
  public Dinero(
    String cantidad
  ) {
    this(cantidad, Dinero.MONEDA);
  }
  
  /// Crea un nuevo objeto [Dinero] a partir de los valores especificados.
  ///
  /// @param cantidad Representación textual de la cantidad de dinero.
  /// @param moneda   Moneda que se usa para el dinero.
  /// @throws NumberFormatException Si el valor de cantidad debe ser un representación válida
  ///                               de [BigDecimal]
  public Dinero(
    String cantidad,
    Currency moneda
  ) {
    // primero intentar crear un número BigDecimal a partir de la representación textual.
    BigDecimal laCantidad = BigDecimal.ZERO;
    try {
      laCantidad = new BigDecimal(cantidad);
    } catch (NumberFormatException excepcion) {
      throw new NumberFormatException(
        "Moneda: el valor de cantidad debe ser un representación válida de BigDecimal."
      );
    }
    
    this(laCantidad, moneda);
  }
  
  /// Obtiene la cantidad de dinero.
  ///
  /// @return Cantidad de dinero.
  public BigDecimal getCantidad() { return this.cantidad; }
  
  /// Establecer la cantidad de dinero.
  ///
  /// @param cantidad Cantidad de dinero.
  public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
  
  /// Obtiene la moneda que se usa para el dinero.
  ///
  /// @return Moneda que se usa para el dinero.
  public Currency getMoneda() { return this.moneda; }
  
  /// Establece la moneda que se usa para el dinero.
  ///
  /// @param moneda Moneda que se usa para el dinero.
  public void setMoneda(Currency moneda) { this.moneda = moneda; }
  
  /// Calcula la suma de este objeto [Dinero] con el objeto [Dinero] especificado.
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
  /// @param otro El otro objeto [Dinero] que se quiere restar.
  /// @return La resta de ambos objetos: this - otro.
  /// @throws IllegalArgumentException Si el objeto `otro` tiene una moneda diferente si
  ///                                  este objeto.
  public Dinero restar(Dinero otro) {
    validarMoneda(otro);
    return new Dinero(this.cantidad.subtract(otro.getCantidad()), this.moneda);
  }
  
  /// Calcula la multiplicación de este objeto [Dinero] con el objeto [Dinero] especificado.
  ///
  /// @param otro El otro objeto [Dinero] que se quiere multiplicar.
  /// @return La multiplicación de ambos objetos: this * otro.
  /// @throws IllegalArgumentException Si el objeto `otro` tiene una moneda diferente si
  ///                                  este objeto.
  public Dinero multiplicar(Dinero otro) {
    return new Dinero(this.cantidad.multiply(otro.getCantidad()), this.moneda);
  }
  
  /// Calcula la división de este objeto [Dinero] con el objeto [Dinero] especificado.
  ///
  /// @param otro El otro objeto [Dinero] que se quiere dividir.
  /// @return La multiplicación de ambos objetos: this × otro.
  /// @throws IllegalArgumentException Si el objeto `otro` tiene una moneda diferente si
  ///                                  este objeto.
  public Dinero dividir(Dinero otro) {
    return new Dinero(this.cantidad.divide(otro.getCantidad()), this.moneda);
  }
  
  /// Obtiene un representación textual de la cantidad de dinero usando el objeto locale
  /// predeterminado de la moneda.
  public String formatear() {
    return Dinero.FORMATEADOR_MONEDA.format(this.cantidad);
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
  /// @throws IllegalArgumentException Si el objeto `otro` tiene una moneda diferente si
  ///                                  este objeto.
  private void validarMoneda(Dinero otro) {
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
  
  /// Crea un formateador de número apto para un valor [Dinero] cuando el campo no tenga el
  /// foco. Se incluye el símbolo de la moneda. Ejemplo: $ 20.000,00.
  public static NumberFormatter crearFormateadorDisplay() {
    
    // crear un formato moneda basado en el locale predeterminado con cifras decimales
    NumberFormat formatoNumero = NumberFormat.getCurrencyInstance(Dinero.LOCALE_PREDETERMINADO);
    
    formatoNumero.setMinimumFractionDigits(Dinero.MONEDA.getDefaultFractionDigits());
    formatoNumero.setMaximumFractionDigits(Dinero.MONEDA.getDefaultFractionDigits());
    
    // activar la conversión a BigDecimal
    if (formatoNumero instanceof DecimalFormat formatoDecimal) {
      formatoDecimal.setParseBigDecimal(true);
    }
    
    // crear un formateador numérico
    NumberFormatter formateador = new NumberFormatter(formatoNumero);
    formateador.setValueClass(BigDecimal.class);
    // el campo siempre muestra una cantidad monetaria
    formateador.setAllowsInvalid(false);
    formateador.setCommitsOnValidEdit(false);
    
    return formateador;
  }
  
  /// Crea un formateador de número apto para un valor [Dinero] cuando el usuario esté
  /// editando el campo. No incluye el símbolo de la moneda. Ejemplo: 20000,00.
  public static NumberFormatter crearFormateadorEdicion() {
    
    // crear un formato numérico basado en el locale predeterminado con cifras decimales
    NumberFormat formatoNumero = NumberFormat.getNumberInstance(Dinero.LOCALE_PREDETERMINADO);
    
    formatoNumero.setMinimumFractionDigits(Dinero.MONEDA.getDefaultFractionDigits());
    formatoNumero.setMaximumFractionDigits(Dinero.MONEDA.getDefaultFractionDigits());
    
    // activar la conversión a BigDecimal
    if (formatoNumero instanceof DecimalFormat formatoDecimal) {
      formatoDecimal.setParseBigDecimal(true);
    }
    
    // crear un formateador numérico
    NumberFormatter formateador = new NumberFormatter(formatoNumero);
    formateador.setValueClass(BigDecimal.class);
    // el usuario edita un valor numérico libremente
    formateador.setAllowsInvalid(true);
    formateador.setCommitsOnValidEdit(false);
    
    return formateador;
  }
  
  /// Crea una fábrica de formato predeterminado para los cuatro formateadores: default,
  /// display, edit, null.
  public static DefaultFormatterFactory crearFabricaFormatoPredeterminado() {
    NumberFormatter display = Dinero.crearFormateadorDisplay();
    NumberFormatter edicion = Dinero.crearFormateadorEdicion();
    // el orden de los formateadores es: default, display, edit, null
    return new DefaultFormatterFactory(display, display, edicion, display);
  }
  
  /// Crea un objeto [Dinero] a partir de un objeto.
  ///
  /// @param valor Valor que se quiere convertir a [Dinero].
  /// @return Un objeto [Dinero] equivalente al `valor`, o `Dinero.CERO` si `valor` no se
  ///         puede convertir.
  public static Dinero desde(Object valor) {
    // crear un objeto Dinero si el valor es BigDecimal o si es Number
    if (valor instanceof BigDecimal decimal) {
      return new Dinero(decimal);
    }
    if (valor instanceof Number numero) {
      return new Dinero(BigDecimal.valueOf(numero.doubleValue()));
    }
    // en lugar de lanzar una excepción, devolver un cero
    return Dinero.CERO;
  }
}
