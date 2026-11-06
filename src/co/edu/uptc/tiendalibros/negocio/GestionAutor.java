package co.edu.uptc.tiendalibros.negocio;

import java.util.List;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutor;
import co.edu.uptc.tiendalibros.modelo.Autor;
import co.edu.uptc.tiendalibros.negocio.excepciones.ReglaNegocioException;

public class GestionAutor {
  
  private IRepositorioAutor repositorio;
  
  public GestionAutor(IRepositorioAutor repositorio) {
    this.repositorio = repositorio;
  }
  
  public Autor guardar(Autor autor) {
    validar(autor);
    if (autor.getId() != null && this.repositorio.buscarPorId(autor.getId())
      .isEmpty()) {
      throw new ReglaNegocioException("El autor ya no existe.");
    }
    
    return this.repositorio.guardar(autor);
  }
  
  public void validar(Autor autor) {
    if (autor == null) {
      throw new ReglaNegocioException("El autor no puede ser nula.");
    }
    if (autor.getNombre() == null || autor.getNombre()
      .isBlank()) {
      throw new ReglaNegocioException("El nombre del autor no puede estar vacío.");
    }
    
    if (autor.getApellidos() == null || autor.getApellidos()
      .isBlank()) {
      throw new ReglaNegocioException("Los apellidos  del autor no pueden estar vacío.");
    }
  }
  
  public List<Autor> listar() {
    return this.repositorio.listar();
  }
  
  public List<Autor> buscarPorId(Long id) {
    return this.repositorio.buscarPorId(id);
  }
  
  public boolean eliminar(Long id) {
    
    return this.repositorio.eliminar(id);
  }
}
