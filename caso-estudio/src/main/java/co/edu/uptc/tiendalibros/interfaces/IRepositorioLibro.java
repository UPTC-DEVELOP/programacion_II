//
package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.modelo.Editorial;
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
  
  /// Determina si existe al menos un libro asociado a la editorial especificada.
  /// 
  /// Una editorial que no se ha guardado (no tiene ID) no tiene libros asociados.
  /// 
  /// @param editorial Editorial de la que se quiere saber si tiene libros asociados.
  /// @return `true` si al menos un libro tiene esa editorial. `false` si ningún libro tiene
  ///         la editorial.
  /// @throws IllegalArgumentException Si `editorial` es nula.
  boolean existeLibroConEditorial(Editorial editorial);
  
  /// Determina si existe al menos un libro asociado a la categoría especificada.
  /// 
  /// Una categoría que no se ha guardado (no tiene ID) no tiene libros asociados.
  /// 
  /// @param categoria Categoria de la que se quiere saber si tiene libros asociados.
  /// @return `true` si al menos un libro tiene esa categoría. `false` si ningún libro tiene
  ///         la categoría.
  /// @throws IllegalArgumentException Si `categoria` es nula.
  boolean existeLibroConCategoria(Categoria categoria);
  
}
