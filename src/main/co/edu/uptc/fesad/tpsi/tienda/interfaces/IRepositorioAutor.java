package main.co.edu.uptc.fesad.tpsi.tienda.interfaces;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Autor;

public interface IRepositorioAutor {
  
  /// Guarda una editorial en el repositorio.
  /// 
  /// @return La editorial con su ID generado en el repositorio.
  Autor guardar(Autor autorl);
  
  /// Elimina una editorial del repositorio.
  /// 
  /// @param id ID de la editorial que se quiere eliminar.
  /// @return true si la editorial se eliminó. false si la editorial no se encontró.
  boolean eliminar(Long id);
  
  /// Busca una editorial por su ID.
  /// 
  /// @param id ID de la editorial que se quiere buscar.
  /// @return Una lista de las editoriales que coinciden con el ID.
  List<Autor> buscarPorId(Long id);
  
  /// Obtiene la lista de editoriales en el repositorio.
  /// 
  /// @return Lista de editoriales en el repositorio.
  List<Autor> listar();
  
}
