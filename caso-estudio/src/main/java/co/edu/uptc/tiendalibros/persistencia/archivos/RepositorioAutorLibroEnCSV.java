//
package co.edu.uptc.tiendalibros.persistencia.archivos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutorLibro;
import co.edu.uptc.tiendalibros.modelo.AutorLibro;

//
public class RepositorioAutorLibroEnCSV extends RepositorioElementoEnCSVBase<AutorLibro>
  implements IRepositorioAutorLibro {
  
  /// Encabezado de los atributos o columnas en la primera fila del archivo CSV.
  private static final String ENCABEZADO = "id|idLibro|idAutor|orden";
  
  /// Índice de la columna idLibro.
  private static final int COLUMNA_ID_LIBRO = 1;
  
  /// Índice de la columna idAutor.
  private static final int COLUMNA_ID_AUTOR = 2;
  
  /// Índice de la columna orden.
  private static final int COLUMNA_ORDEN_AUTOR = 3;
  
  /// Crea un nuevo objeto [RepositorioAutorLibroEnCSV] a partir del archivo CSV
  /// especificado.
  ///
  /// @param rutaArchivo Ruta del archivo CSV donde se guardarán los libros. Si el archivo VS
  ///                    no existe, se crea automáticamente.
  public RepositorioAutorLibroEnCSV(String rutaArchivo) {
    super(rutaArchivo, "|", 4, 0, RepositorioAutorLibroEnCSV.ENCABEZADO);
  }
  
  @Override
  protected Long getId(AutorLibro autorLibro) {
    return autorLibro.getId();
  }
  
  @Override
  protected AutorLibro asignarId(AutorLibro autorLibro, Long id) {
    return new AutorLibro(
      id,
      autorLibro.getIdLibro(),
      autorLibro.getIdAutor(),
      autorLibro.getOrdenAutor()
    );
  }
  
  @Override
  public Optional<AutorLibro> convertirCampos(String[] campos) {
    // intentar convertir cada campo a su tipo de dato respectivo
    try {
      // convertir el id
      Long id = Long.parseLong(campos[getColumnaId()].trim());
      // convertir el idLibro
      Long idLibro = Long.parseLong(campos[RepositorioAutorLibroEnCSV.COLUMNA_ID_LIBRO].trim());
      // convertir el idAutor
      Long idAutor = Long.parseLong(campos[RepositorioAutorLibroEnCSV.COLUMNA_ID_AUTOR].trim());
      // convertir el ordenAutor
      int ordenAutor = Integer.parseInt(campos[RepositorioAutorLibroEnCSV.COLUMNA_ORDEN_AUTOR].trim());
      
      AutorLibro autorLibro = new AutorLibro(id, idLibro, idAutor, ordenAutor);
      return Optional.of(autorLibro);
    } // si al leer los campos ocurren errores, se ignora el resultado
    catch (IllegalArgumentException excepcion) {
      return Optional.empty();
    }
  }
  
  @Override
  protected String convertirRegistro(AutorLibro autorLibro) {
    return String.join(
      getSeparador(),
      String.valueOf(autorLibro.getId()),
      String.valueOf(autorLibro.getIdLibro()),
      String.valueOf(autorLibro.getIdAutor()),
      String.valueOf(autorLibro.getOrdenAutor())
    );
  }
  
  @Override
  public List<AutorLibro> buscarPorIdLibro(Long idLibro) {
    // si el id es nulo, devolver una lista vacía
    if (idLibro == null) {
      return new ArrayList<>();
    }
    
    List<AutorLibro> resultados = new ArrayList<>();
    
    // buscar todas las asociaciones autor-libro que tengn el ID del libro dado
    for (AutorLibro autorLibro : getRegistros()) {
      if (idLibro.equals(autorLibro.getIdLibro())) {
        resultados.add(autorLibro);
      }
    }
    return resultados;
  }
  
  @Override
  public boolean eliminarPorLibro(Long idLibro) {
    // si el id es nulo, no se elimina nada
    if (idLibro == null) {
      return false;
    }
    
    // indica si al recorrer la lista se eliminó al menos un autor-libro
    boolean elimino = false;
    
    // obtener la lista de objetos autor-libro que tienen ese id del libro
    List<AutorLibro> autorLibrosEliminar = buscarPorIdLibro(idLibro);
    
    // eliminar los objetos autor-libro indicados
    for (AutorLibro autorLibro : autorLibrosEliminar) {
      // eliminar el autor-libro
      if (super.eliminar(autorLibro.getId())) {
        elimino = true;
      }
    }
    return elimino;
  }
  
  @Override
  public List<AutorLibro> asignarAsociacion(Long idLibro, List<AutorLibro> asociaciones) {
    // TODO Pendiente de crear este método.
    return null;
  }
  
}
