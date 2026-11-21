/// 
package co.edu.uptc.tiendalibros.interfaces;

import co.edu.uptc.tiendalibros.modelo.Dinero;
import co.edu.uptc.tiendalibros.modelo.TipoIVA;

/// Representa un elemento que se puede vender y comprar.
public interface IElementoComprable extends IElemento {
  
  /// Obtiene el precio base del elemento antes de impuestos.
  /// 
  /// @return Precio base del elemento antes de impuestos.
  Dinero getPrecioBase();
  
  /// Obtiene el tipo de IVA que se aplica al elemento.
  ///
  /// @return Tipo de IVA que se aplica al elemento.
  public TipoIVA getTipoIVA();
  
  /// Calcula el valor del IVA que le corresponde al elemento.
  /// 
  /// `valor iva = precio base * procentaje IVA`.
  default Dinero calcularIVA() {
    return getTipoIVA().calcularIVA(getPrecioBase());
  }
  
  /// Calcula el precio de venta del elemento.
  default Dinero calcularPrecioVenta() {
    // precio final = precio base + IVA * precio base
    return getTipoIVA().calcularPrecioFinal(getPrecioBase());
  }
}
