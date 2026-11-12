/// 
package co.edu.uptc.tiendalibros.gui.estilos;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import co.edu.uptc.tiendalibros.modelo.Dinero;

/// Representa una fábrica de formateadores Swing para campos que muestran o editan
/// cantidades de [Dinero].
public final class FormatosDinero {
  
  private FormatosDinero() {
  }
  
  /// Crea un formateador de número apto para un valor [Dinero] cuando el campo no tiene el
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
  
  /// Crea un formateador de número apto para un valor [Dinero] cuando el usuario edita el
  /// campo. No incluye el símbolo de la moneda. Ejemplo: 20000,00.
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
    NumberFormatter display = FormatosDinero.crearFormateadorDisplay();
    NumberFormatter edicion = FormatosDinero.crearFormateadorEdicion();
    // el orden de los formateadores es: default, display, edit, null
    return new DefaultFormatterFactory(display, display, edicion, display);
  }
  
}
