///
package main.co.edu.uptc.fesad.tpsi.tienda.modelo;

///

public class Producto {
  
  /// Valor del precio del producto.
  private double valor;
  
  /// Valor del porcentaje de descuento del producto.
  private double porcentajeDescuento;
  
  /// Valor del porcentaje de IVA del producto.
  private double impuestoIVA;
  
  /// Calcula el precio final del producto aplicando el descuento y el IVA.
  public double calcularPrecioFinal() {
    
    double precioFinal = this.valor * (1 - this.porcentajeDescuento / 100.0d);
    
    return precioFinal * (1 - this.impuestoIVA * 100.0d);
  }
  
}