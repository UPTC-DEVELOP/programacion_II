package main.co.edu.uptc.fesad.tpsi.tienda.negocio;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IRepositorioCategoria;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Categoria;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.excepciones.ReglaNegocioException;

public class GestionCategoria {
  
  private final IRepositorioCategoria repositorio;
  
  public GestionCategoria(IRepositorioCategoria repositorio) {
    this.repositorio = repositorio;
  }
  
  public Categoria guardar(Categoria categoria) {
    
    validar(categoria);
    
    if (categoria.getId() != null &&
      this.repositorio.buscarPorId(categoria.getId())
        .isEmpty()) {
      
      throw new ReglaNegocioException(
        "La categoría ya no existe."
      );
    }
    
    return this.repositorio.guardar(categoria);
  }
  
  public void validar(Categoria categoria) {
    
    if (categoria == null) {
      throw new ReglaNegocioException(
        "La categoría no puede ser nula."
      );
    }
    
    if (categoria.getNombre() == null ||
      categoria.getNombre()
        .isBlank()) {
      
      throw new ReglaNegocioException(
        "El nombre de la categoría no puede estar vacío."
      );
    }
    
    if (categoria.getDescripcion() == null ||
      categoria.getDescripcion()
        .isBlank()) {
      
      throw new ReglaNegocioException(
        "La descripción de la categoría no puede estar vacía."
      );
    }
    
    if (categoria.getCodigo() == null ||
      categoria.getCodigo()
        .isBlank()) {
      
      throw new ReglaNegocioException(
        "El código de la categoría no puede estar vacío."
      );
    }
  }
  
  public List<Categoria> listar() {
    return this.repositorio.listar();
  }
  
  public List<Categoria> buscarPorId(Long id) {
    return this.repositorio.buscarPorId(id);
  }
  
  public boolean eliminar(Long id) {
    return this.repositorio.eliminar(id);
  }
}