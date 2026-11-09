//
package co.edu.uptc.tiendalibros.persistencia.archivos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroBuilder;

/// Representa el repositorio que almacena los libros guardando los datos en un archivo
/// CSV.
///
/// El archivo CVS usa como separador el caracter: |
///
/// Una fila del archivo CSV representa un libro en esta forma:
///
///    id|isbn|titulo|anioPublicacion|idCategoria|idEditorial|imagenLibro
///
/// La primera fila del archivo CSV contiene los encabezados.
///
/// El archivo CVS se lee por completo para cargar los datos de todos los libros.
///
/// El archivo CVS se escribe por completo cada vez que se guarde un libro, guardando todos
/// los libros a la vez.
public class RepositorioLibroEnCSV extends RepositorioElementoEnCSVBase<Libro> implements IRepositorioLibro {
  
  /// Encabezado de los atributos o columnas en la primera fila del archivo CSV.
  private static final String ENCABEZADO = "id|isbn|titulo|anioPublicacion|idCategoria|idEditorial|imagenLibro";
  
  /// Índice de la columna isbn.
  private static final int COLUMNA_ISBN = 1;
  
  /// Índice de la columna título del libro.
  private static final int COLUMNA_TITULO = 2;
  
  /// Índice de la columna año de publicación del libro.
  private static final int COLUMNA_ANIO_PUBLICACION = 3;
  
  /// Índice de la columna id de la categoría del libro.
  private static final int COLUMNA_ID_CATEGORIA = 4;
  
  /// Índice de la columna id de la editorial del libro.
  private static final int COLUMNA_ID_EDITORIAL = 5;
  
  /// Índice de la columna imagen del libro.
  private static final int COLUMNA_IMAGEN_LIBRO = 6;
  
  /// Crea un nuevo objeto [RepositorioLibroEnCSV] a partir del archivo CSV especificado.
  ///
  /// @param rutaArchivo Ruta del archivo CSV donde se guardarán los libros. Si el archivo VS
  ///                    no existe, se crea automáticamente.
  public RepositorioLibroEnCSV(String rutaArchivo) {
    // total atributos = id + atributos
    super(rutaArchivo, "|", 1 + 6, 0, RepositorioLibroEnCSV.ENCABEZADO);
  }
  
  @Override
  protected Long getId(Libro libro) {
    return libro.getId();
  }
  
  @Override
  protected Libro asignarId(Libro libro, Long id) {
    return new LibroBuilder()
      .id(id)
      .isbn(libro.getIsbn())
      .titulo(libro.getTitulo())
      .anioPublicacion(libro.getAnioPublicacion())
      .idCategoria(libro.getIdCategoria())
      .idEditorial(libro.getIdEditorial())
      .imagenLibro(libro.getImagenLibro())
      .construir();
  }
  
  @Override
  public Optional<Libro> convertirCampos(String[] campos) {
    
    // intentar convertir cada campo a su tipo de dato respectivo
    try {
      // convertir el id
      Long id = Long.parseLong(campos[getColumnaId()].trim());
      // convertir el isbn
      String isbn = campos[RepositorioLibroEnCSV.COLUMNA_ISBN].trim();
      // convertir el título
      String titulo = campos[RepositorioLibroEnCSV.COLUMNA_TITULO].trim();
      // convertir el año de publicación
      int anioPublicacion = Integer.parseInt(campos[RepositorioLibroEnCSV.COLUMNA_ANIO_PUBLICACION].trim());
      // convertir el id de la categoría
      Long idCategoria = convertirALong(campos[RepositorioLibroEnCSV.COLUMNA_ID_CATEGORIA].trim());
      // convertir el id de la editorial
      Long idEditorial = convertirALong(campos[RepositorioLibroEnCSV.COLUMNA_ID_EDITORIAL].trim());
      // convertir la imagen del libro
      String imagenLibro = campos[RepositorioLibroEnCSV.COLUMNA_IMAGEN_LIBRO].trim();
      
      // nota: se crea un objeto libro a partir de los datos del archivo CSV. Pero aún falta que
      // el gestor de libros revise si el libro cumple con las reglas de negocio
      
      // bien, aquí ya se puede retornar el libro con los datos leídos
      Libro elLibro = new LibroBuilder()
        .id(id)
        .isbn(isbn)
        .titulo(titulo)
        .anioPublicacion(anioPublicacion)
        .idCategoria(idCategoria)
        .idEditorial(idEditorial)
        .imagenLibro(imagenLibro)
        .construir();
      
      return Optional.of(elLibro);
    }
    // si al leer los campos ocurren errores, se ignora el resultado
    catch (IllegalArgumentException excepcion) {
      return Optional.empty();
    }
    
  }
  
  @Override
  public String convertirRegistro(Libro libro) {
    // formar una línea String separada con el separador
    return String.join(
      getSeparador(),
      String.valueOf(libro.getId()),
      libro.getIsbn() == null ? "" : libro.getIsbn(),
      libro.getTitulo() == null ? "" : libro.getTitulo(),
      String.valueOf(libro.getAnioPublicacion()),
      libro.getIdCategoria() == null ? "" : String.valueOf(libro.getIdCategoria()),
      libro.getIdEditorial() == null ? "" : String.valueOf(libro.getIdEditorial()),
      libro.getImagenLibro() == null ? "" : String.valueOf(libro.getImagenLibro())
    );
  }
  
  @Override
  public List<Libro> buscarPorTitulo(String titulo) {
    // si el título es vacío, se devuelve una lista vacía
    if (titulo == null || titulo.isBlank()) {
      return new ArrayList<Libro>();
    }
    
    // preparar el criterio de búsqueda de título
    String tituloBusqueda = titulo.trim()
      .toLowerCase();
    
    // lista de resultados
    List<Libro> resultados = new ArrayList<>();
    
    // buscar el libro que concida parcialmente con el título
    for (Libro actual : this.listar()) {
      if (actual.getTitulo()
        .toLowerCase()
        .contains(tituloBusqueda)) {
        // se encontró una coincidencia, entonces agregarla a la lista de resultados
        resultados.add(actual);
      }
    }
    
    return resultados;
  }
  
  @Override
  public Optional<Libro> buscarPorIsbn(String isbn) {
    if (isbn == null || isbn.isBlank()) {
      return Optional.empty();
    }
    // buscar loslibros que tengan el mismo ISBN
    for (Libro libro : getRegistros()) {
      if (isbn.equals(libro.getIsbn())) {
        return Optional.of(libro);
      }
    }
    return Optional.empty();
  }
  
}
