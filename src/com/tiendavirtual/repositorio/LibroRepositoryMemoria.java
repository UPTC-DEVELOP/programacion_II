package com.tiendavirtual.repositorio;
import com.tiendavirtual.modelo.Libro; import java.util.*;
public class LibroRepositoryMemoria implements LibroRepository {
 private final List<Libro> libros=new ArrayList<>();
 public void guardar(Libro l){libros.add(l);}
 public void actualizar(Libro l){for(int i=0;i<libros.size();i++)if(libros.get(i).getIsbn().equalsIgnoreCase(l.getIsbn())){libros.set(i,l);return;}}
 public void eliminar(String isbn){libros.removeIf(l->l.getIsbn().equalsIgnoreCase(isbn));}
 public Libro buscarPorIsbn(String isbn){for(Libro l:libros)if(l.getIsbn().equalsIgnoreCase(isbn))return l;return null;}
 public List<Libro> listar(){return new ArrayList<>(libros);}
}
