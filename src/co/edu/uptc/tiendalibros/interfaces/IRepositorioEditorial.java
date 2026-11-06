//
package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;

import co.edu.uptc.tiendalibros.modelo.Editorial;

/// Representa el repositorio de datos que almacena las editoriales.
public interface IRepositorioEditorial extends IRepositorio<Editorial> {
  
  /// Busca editoriales con el nombre especificado.
  ///
  /// @param nombre Nombre de la editorial que se quiere buscar.
  /// @return Lista de editoriales con el nombre especificado.
  List<Editorial> buscarPorNombre(String nombre);
}
