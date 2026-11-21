/// 
package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa el repositorio de datos que guarda un libro con a sus formatos impreso y
/// digital.
/// 
/// Guardar un libro junto a sus formatos impreso y digital se hace en una sola operación
/// en la cual o se guarda todo, o no se guarda nada.
public interface IRepositorioLibroConFormatos {
  
  /// Guarda el libro con sus formatos opcionales
  ///
  /// Si el libro no tiene ID, se crea el libro y se crean sus formatos.
  /// 
  /// Si el libro tiene ID, se actualiza el libro y se actualizan sus formatos (o se crean
  /// los que no tengan ID).
  ///
  /// Si ocurre cualquier error, no se guarda ni el libro ni sus formatos.
  ///
  /// @param libroConFormatos Libro con sus dos formatos que se quiere guardar.
  /// @return El libro con sus formatos guardados, con todos los IDs asignados.
  /// @throws IllegalArgumentException           Si `libroConFormatos` es nulo.
  /// @throws ViolacionElementoNoExisteException Si el libro o alguno de sus formatos tiene
  ///                                            ID pero ya no existe en el repositorio.
  LibroConFormatos guardar(LibroConFormatos libroConFormatos) throws ViolacionElementoNoExisteException;
  
  /// Busca el libro con el ID especificado junto con sus dos formatos.
  ///
  /// @param id ID del libro que se quiere buscar.
  /// @return El libro con sus dos formatos. O, un valor vacío si el libro no existe.
  Optional<LibroConFormatos> buscarPorId(Long id);
  
  /// Elimina el libro con el ID especificado junto con sus formatos.
  ///
  /// @param id ID del libro que se quiere eliminar.
  /// @return `true` si el libro se eliminó. `false` si no se encontró.
  /// @throws IllegalArgumentException Si `id` es nulo.
  boolean eliminar(Long id);
  
  /// Obtiene la lista de libros con formatos almacenados en el repositorio.
  ///
  /// @return Lista de libros con formatos almacenados. Vacía si no hay libros con
  ///         formatos almacenados.
  List<LibroConFormatos> listar();
  
  /// Obtiene la lista de libros que se pueden comprar, cada uno con sus formatos
  /// comprables.
  ///
  /// Un formato se puede comprar si su estado en el catálogo es disponible y su inventario
  /// es mayor a cero. Un libro sin ningún formato comprable no aparece en la lista.
  /// 
  /// En este método, si un libro tiene un solo formato que cumple la condición de ser
  /// comprable, el otro formato de libro estará vacío.
  ///
  /// **Nota: El resultado es una vista de solo lectura para el catálogo:** Un libro cargado
  /// con este método puede traer solo uno de sus dos formatos. Ningún libro obtenido con
  /// este método debe pasarse a [#guardar(LibroConFormatos)] porque al guardar, el
  /// repositorio interpreta el formato faltante (vacío) que no está disponible para la
  /// compra como un formato que el libro dejó de ofrecer, y lo eliminaría de la base de
  /// datos.
  ///
  /// @return Lista de libros con sus formatos comprables. Vacía si no hay ninguno.
  List<LibroConFormatos> listarComprables();
  
}
