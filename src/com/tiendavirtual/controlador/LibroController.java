package com.tiendavirtual.controlador;
import com.tiendavirtual.modelo.Libro; import com.tiendavirtual.negocio.LibroService; import java.util.List;
public class LibroController {
 private final LibroService s; public LibroController(LibroService s){this.s=s;}
 public void registrar(Libro l){s.registrar(l);} public void actualizar(Libro l){s.actualizar(l);} public void eliminar(String i){s.eliminar(i);}
 public List<Libro> listar(){return s.listar();} public Libro buscar(String i){return s.buscar(i);}
 public double precioFinal(double p,double iva){return s.precioFinal(p,iva);}
}
