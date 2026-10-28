package com.tiendavirtual.modelo;
public class Libro {
 private String isbn,titulo,autor,categoria,editorial,formato; private int anio,paginas,stock; private double precio,iva; private boolean tieneVentasAsociadas;
 public Libro(String isbn,String titulo,String autor,int anio,String categoria,String editorial,int paginas,double precio,int stock,String formato,double iva){
  this.isbn=isbn;this.titulo=titulo;this.autor=autor;this.anio=anio;this.categoria=categoria;this.editorial=editorial;this.paginas=paginas;this.precio=precio;this.stock=stock;this.formato=formato;this.iva=iva;
 }
 public String getIsbn(){return isbn;} public void setIsbn(String v){isbn=v;}
 public String getTitulo(){return titulo;} public void setTitulo(String v){titulo=v;}
 public String getAutor(){return autor;} public void setAutor(String v){autor=v;}
 public int getAnio(){return anio;} public void setAnio(int v){anio=v;}
 public String getCategoria(){return categoria;} public void setCategoria(String v){categoria=v;}
 public String getEditorial(){return editorial;} public void setEditorial(String v){editorial=v;}
 public int getPaginas(){return paginas;} public void setPaginas(int v){paginas=v;}
 public double getPrecio(){return precio;} public void setPrecio(double v){precio=v;}
 public int getStock(){return stock;} public void setStock(int v){stock=v;}
 public String getFormato(){return formato;} public void setFormato(String v){formato=v;}
 public double getIva(){return iva;} public void setIva(double v){iva=v;}
 public boolean isTieneVentasAsociadas(){return tieneVentasAsociadas;} public void setTieneVentasAsociadas(boolean v){tieneVentasAsociadas=v;}
 public double calcularIva(){return precio*iva;} public double calcularPrecioConIva(){return precio+calcularIva();}
}
