package main.co.edu.uptc.fesad.tpsi.tienda.persistencia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IRepositorioCategoria;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Categoria;

public class RepositorioCategoriaEnMemoria implements IRepositorioCategoria {
  
  private final List<Categoria> categorias;
  
  private final AtomicLong contador;
  
  public RepositorioCategoriaEnMemoria() {
    this.categorias = new ArrayList<Categoria>();
    this.contador = new AtomicLong(10);
    
    // Datos de ejemplo
    guardar(
      new Categoria(
        "Novela",
        "Libros de ficción narrativa",
        "CAT001"
      )
    );
    
    guardar(
      new Categoria(
        "Ciencia",
        "Libros relacionados con ciencia y conocimiento",
        "CAT002"
      )
    );
    
    guardar(
      new Categoria(
        "Historia",
        "Libros sobre hechos históricos",
        "CAT003"
      )
    );
  }
  
  @Override
  public Categoria guardar(Categoria elemento) {
    
    if (elemento == null) {
      return null;
    }
    
    Categoria categoria = elemento;
    
    // Crear
    if (categoria.getId() == null) {
      Long idNuevo = this.contador.incrementAndGet();
      categoria.setId(idNuevo);
      this.categorias.add(categoria);
      return categoria;
    }
    
    // Editar
    else {
      for (int i = 0; i < this.categorias.size(); i++) {
        
        if (this.categorias.get(i)
          .getId()
          .equals(categoria.getId())) {
          
          this.categorias.set(i, categoria);
          return categoria;
        }
      }
    }
    
    throw new IllegalArgumentException(
      "No se encontró la categoría con ID: " + categoria.getId()
    );
  }
  
  @Override
  public boolean eliminar(Long id) {
    
    if (id == null) {
      return false;
    }
    
    Iterator<Categoria> iterador = this.categorias.iterator();
    
    while (iterador.hasNext()) {
      
      Categoria categoria = iterador.next();
      
      if (categoria.getId()
        .equals(id)) {
        iterador.remove();
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public List<Categoria> buscarPorId(Long id) {
    
    List<Categoria> resultados = new ArrayList<Categoria>();
    
    if (id == null) {
      return resultados;
    }
    
    for (Categoria categoriaActual : this.categorias) {
      
      if (categoriaActual.getId()
        .equals(id)) {
        resultados.add(categoriaActual);
      }
    }
    
    return resultados;
  }
  
  @Override
  public List<Categoria> listar() {
    return new ArrayList<Categoria>(this.categorias);
  }
}