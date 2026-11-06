//
package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Libro;

/// Representa el repositorio de datos que almacena los libros.
public interface IRepositorioLibro extends IRepositorio<Libro> {
  
  /// Busca un libro por su ISBN.
  ///
  /// @param isbn ISBN del libro que se quiere buscar.
  /// @return El libro encontrado con ese ISBN. O, un valor vacío si no existe o si el ISBN
  ///         es nulo o vacío.
  Optional<Libro> buscarPorIsbn(String isbn);
  
  /// Busca libros con el titulo especificado, mediante una búsqueda aproximada.
  ///
  /// @param titulo Fragmento del título del libro que se quiere buscar.
  /// @return Lista de libros con el título especificado.
  List<Libro> buscarPorTitulo(String titulo);
  
}
