///
package main.co.edu.uptc.fesad.tpsi.tienda.modelo;

///

public class Producto {
  
  /// Valor del precio del producto.
  private double precio;
  
  /// Valor del porcentaje de descuento del producto.
  private double porcentajeDescuento;
  
  /// Valor del porcentaje de IVA del producto.
  private double impuestoIVA;
  
  /// Calcula el precio final del producto aplicando el descuento y el IVA.
  public double calcularPrecioFinal() {
    
    double precioConDescuento = this.precio * (1 - this.porcentajeDescuento / 100.0d);
    
    return precioConDescuento * (1 + this.impuestoIVA / 100.0d);
  }
  
}