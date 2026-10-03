/// 
package main.co.edu.uptc.fesad.tpsi.tienda.negocio;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IRepositorioEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Editorial;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.excepciones.ReglaNegocioException;

/// Representa un gestor de la lógica de negocio para las editoriales.
public class GestionEditorial {
  
  private final IRepositorioEditorial repositorio;
  
  public GestionEditorial(IRepositorioEditorial repositorio) {
    this.repositorio = repositorio;
  }
  
  public Editorial guardar(Editorial editorial) {
    validar(editorial);
    if (editorial.getId() != null && this.repositorio.buscarPorId(editorial.getId())
      .isEmpty()) {
      throw new ReglaNegocioException("La editorial ya no existe.");
    }
    
    return this.repositorio.guardar(editorial);
  }
  

  public void validar(Editorial editorial) {
    if (editorial == null) {
      throw new ReglaNegocioException("La editorial no puede ser nula.");
    }
    if (editorial.getNombre() == null || editorial.getNombre()
      .isBlank()) {
      throw new ReglaNegocioException("El nombre de la editorial no puede estar vacío.");
    }
  }
  
  public List<Editorial> listar() {
    return this.repositorio.listar();
  }
  

  public List<Editorial> buscarPorId(Long id) {
    return this.repositorio.buscarPorId(id);
  }
  
  public boolean eliminar(Long id) {
    
    return this.repositorio.eliminar(id);
  }
  
}
