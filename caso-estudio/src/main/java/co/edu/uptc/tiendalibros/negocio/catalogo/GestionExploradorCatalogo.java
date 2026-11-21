/// 
package co.edu.uptc.tiendalibros.negocio.catalogo;

import java.util.List;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.negocio.libro.GestionLibro;

/// Representa un gestor de la lógica de negocio para explorar el catálogo de libros que
/// el cliente ve en la tienda.
/// 
/// El explorador solo muestra lo que el cliente puede comprar: libros que tienen al menos
/// un formato en estado disponible y con inventario mayor a cero.
public class GestionExploradorCatalogo {
  
  /// Repositorio que consulta los libros junto con sus formatos.
  private final IRepositorioLibroConFormatos repositorioLibroConFormatos;
  
  /// Crea un nuevo objeto [GestionExploradorCatalogo].
  ///
  /// @param repositorioLibroConFormatos Repositorio que consulta los libros junto con
  ///                                    sus formatos.
  public GestionExploradorCatalogo(IRepositorioLibroConFormatos repositorioLibroConFormatos) {
    this.repositorioLibroConFormatos = repositorioLibroConFormatos;
  }
  
  /// Carga la lista de libros que el cliente puede comprar.
  /// 
  /// Cada libro de la lista tiene al menos un formato (impreso o digital) comprable.
  /// 
  /// Un formato comprable es un formato que está en estado disponible y tiene inventario
  /// mayor a cero.
  /// 
  /// Un libro puede tener sus dos formatos comprables, o un formato comprable y el otro es
  /// un objeto vacío.
  /// 
  /// **Nota: El resultado es una vista de solo lectura para el catálogo:** Un libro cargado
  /// con este método puede traer solo uno de sus dos formatos. Ningún libro obtenido con
  /// este método debe pasarse al método [GestionLibro#guardar] porque al guardar, el
  /// repositorio interpreta el formato faltante (vacío) que no está disponible para la
  /// compra como un formato que el libro dejó de ofrecer, y lo eliminaría de la base de
  /// datos.
  /// 
  /// @return Lista de libros con sus formatos comprables. Vacía si no hay ninguno.
  public List<LibroConFormatos> listar() {
    return this.repositorioLibroConFormatos.listar();
  }
}
