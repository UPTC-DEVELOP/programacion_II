/// 
package main.co.edu.uptc.fesad.tpsi.tienda.modelo;

///
public class Producto {
  
  /// Valor del precio del producto.
  private double precio;
  
  /// Valor del porcentaje de descuento del producto.
  private double porcentajeDescuento;
  
  /// Calcula el precio final del producto aplicando el descuento.
  public double calcularPrecioFinal() {
    return this.precio * (1 - this.porcentajeDescuento / 100.0d);
  }
  
}
