package main.co.edu.uptc.fesad.tpsi.tienda.interfaces;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Autor;

public interface IRepositorioAutor {
  
  /// Guarda un autor en el repositorio.
  /// 
  /// @return el autor con su ID generado en el repositorio.
  Autor guardar(Autor autorl);
  
  /// Elimina el autor del repositorio.
  /// 
  /// @param id ID del autor que se quiere eliminar.
  /// @return true si el autor se eliminó. false si el autor no se encontró.
  boolean eliminar(Long id);
  
  /// Busca el autor por su ID.
  /// 
  /// @param id ID del autor que se quiere buscar.
  /// @return Una lista de los autores que coinciden con el ID.
  List<Autor> buscarPorId(Long id);
  
  /// Obtiene la lista de autores en el repositorio.
  /// 
  /// @return Lista de autores en el repositorio.
  List<Autor> listar();
  
}
