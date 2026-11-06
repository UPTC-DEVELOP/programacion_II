///
package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;

import co.edu.uptc.tiendalibros.modelo.FormatoLibro;

/// Representa el repositorio de datos que almacena las formatos de libros.
public interface IRepositorioFormatoLibro extends IRepositorio<FormatoLibro> {
  
  /// Busca los formatos por el ID del libro al que pertenecen.
  ///
  /// @return Una lista con los formatos que le pertenecen la libro.
  List<FormatoLibro> buscarPorIdLibro(Long idLibro);
  
}
