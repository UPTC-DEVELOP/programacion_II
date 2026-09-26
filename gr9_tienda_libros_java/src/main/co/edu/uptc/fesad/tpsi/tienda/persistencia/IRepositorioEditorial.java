/// 
package main.co.edu.uptc.fesad.tpsi.tienda.persistencia;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.negocio.Editorial;

/// Representa el repositorio que almacena las editoriales.
public interface IRepositorioEditorial {
  
  /// Guarda una editorial en el repositorio.
  /// 
  /// @return La editorial con su ID generado en el repositorio.
  Editorial guardar(Editorial editorial);
  
  /// Elimina una editorial del repositorio.
  /// 
  /// @param id ID de la editorial que se quiere eliminar.
  /// @return true si la editorial se eliminó. false si la editorial no se encontró.
  boolean eliminar(Long id);
  
  /// Busca una editorial por su ID.
  /// 
  /// @param id ID de la editorial que se quiere buscar.
  /// @return Una lista de las editoriales que coinciden con el ID.
  List<Editorial> buscarPorId(Long id);
  
  /// Obtiene la lista de editoriales en el repositorio.
  /// 
  /// @return Lista de editoriales en el repositorio.
  List<Editorial> listar();
  
}
