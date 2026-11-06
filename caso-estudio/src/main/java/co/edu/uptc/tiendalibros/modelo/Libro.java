//
package co.edu.uptc.tiendalibros.modelo;

import java.util.ArrayList;
import java.util.List;

/// Representa un libro como obra intelectual del catálogo de la tienda.
public class Libro extends ElementoBase {
  
  /// ISBN del libro ((International Standard Book Number).
  private String isbn;
  
  /// Título del libro.
  private String titulo;
  
  /// Año de publicación del libro.
  private int anioPublicacion;
  
  /// ID de la categoría que sirve para clasificar el libro.
  private Long idCategoria;
  
  /// ID de la editorial responsable de publicar el libro.
  private Long idEditorial;
  
  /// Lista que asocia el libro con sus autores.
  private List<AutorLibro> listaAutoria;
  
  /// Nombre del archivo que sirve como imagen del libro.
  private String imagenLibro;
  
  /// Crea un nuevo objeto [Libro] con valores predeterminados.
  public Libro() {
    // invocar al constructor que tiene todos los atributos
    this(null, "", "", 0, null, null, new ArrayList<>(), "");
  }
  
  /// Crea un nuevo objeto [Libro] sin ID y con los valores especificados.
  ///
  /// @param isbn            ISBN del del libro.
  /// @param titulo          Título del libro.
  /// @param anioPublicacion Año de publicación del libro.
  /// @param idCategoria     ID de la categoría que sirve para clasificar el libro.
  /// @param idEditorial     ID de la editorial responsable de publicar el libro.
  
  /// @param listaAutoria Lista que asocia el libro con sus autores.
  /// @param imagenLibro  Nombre del archivo que sirve como imagen del libro.
  
  public Libro(
    String isbn,
    String titulo,
    int anioPublicacion,
    Long idCategoria,
    Long idEditorial,
    List<AutorLibro> listaAutoria,
    String imagenLibro
  ) {
    // invocar al constructor que tiene todos los atributos
    this(null, isbn, titulo, anioPublicacion, idCategoria, idEditorial, listaAutoria, imagenLibro);
  }
  
  /// Crea un nuevo objeto [Libro] los con valores especificados.
  ///
  /// @param id              ID del libro.
  /// @param isbn            ISBN del del libro.
  /// @param titulo          Título del libro.
  /// @param anioPublicacion Año de publicación del libro.
  /// @param idCategoria     ID de la categoría que sirve para clasificar el libro.
  /// @param idEditorial     ID de la editorial responsable de publicar el libro.
  /// @param listaAutoria    Lista que asocia el libro con sus autores.
  /// @param imagenLibro     Nombre del archivo que sirve como imagen del libro.
  public Libro(
    Long id,
    String isbn,
    String titulo,
    int anioPublicacion,
    Long idCategoria,
    Long idEditorial,
    List<AutorLibro> listaAutoria,
    String nombreImagenLibro
  ) {
    super(id);
    this.isbn = isbn;
    this.titulo = titulo;
    this.anioPublicacion = anioPublicacion;
    this.idCategoria = idCategoria;
    this.idEditorial = idEditorial;
    this.imagenLibro = nombreImagenLibro;
    
    // evitar listas nulas
    if (listaAutoria == null) {
      this.listaAutoria = new ArrayList<>();
    } else {
      this.listaAutoria = listaAutoria;
    }
  }
  
  /// Crea un nuevo objeto [Libro] a partir del objeto builder especificado.
  ///
  /// @param builder Objeto builder para crear el libro.
  public Libro(LibroBuilder builder) {
    this(
      builder.id(),
      builder.isbn(),
      builder.titulo(),
      builder.anioPublicacion(),
      builder.idCategoria(),
      builder.idEditorial(),
      builder.listaAutoria(),
      builder.imagenLibro()
    );
  }
  
  /// Obtiene el ISBN del libro.
  ///
  /// @return El ISBN del libro.
  public String getIsbn() { return this.isbn; }
  
  /// Establece el ISBN del libro.
  ///
  /// @param isbn ISBN del libro.
  public void setIsbn(String isbn) { this.isbn = isbn; }
  
  /// Obtiene el título del libro.
  ///
  /// @return Título del libro.
  public String getTitulo() { return this.titulo; }
  
  /// Establece el título del libro.
  ///
  /// @param titulo Título del libro.
  public void setTitulo(String titulo) { this.titulo = titulo; }
  
  /// Obtiene el año de publicación del libro.
  ///
  /// @return Año del publicación del libro.
  public int getAnioPublicacion() { return this.anioPublicacion; }
  
  /// Establece el año del publicación del libro
  ///
  /// @param anioPublicacion Año del publicación del libro.
  public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }
  
  /// Obtiene el ID de la editorial responsable de publicar el libro.
  ///
  /// @return ID de la editorial responsable de publicar el libro.
  public Long getIdEditorial() { return this.idEditorial; }
  
  /// Establece el ID de la editorial responsable de publicar el libro.
  ///
  /// @param idEditorial ID de la editorial responsable de publicar el libro.
  public void setIdEditorial(Long idEditorial) { this.idEditorial = idEditorial; }
  
  /// Obtiene el ID de la categoría del libro.
  ///
  /// @return ID de la categoría del libro.
  public Long getIdCategoria() { return this.idCategoria; }
  
  /// Establece el ID de la categoría del libro.
  ///
  /// @param idCategoria ID de la categoría del libro.
  public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
  
  /// Obtiene la lista que asocia el libro con sus autores.
  ///
  /// @return Lista que asocia el libro con sus autores.
  public List<AutorLibro> getListaAutoria() { return this.listaAutoria; }
  
  /// Establece la lista que asocia el libro con sus autores.
  ///
  /// @param listaAutoria Lista que asocia el libro con sus autores.
  public void setListaAutoria(List<AutorLibro> listaAutoria) { this.listaAutoria = listaAutoria; }
  
  /// Obtiene el nombre del archivo que sirve como imagen del libro.
  ///
  /// @return Nombre del archivo que sirve como imagen del libro.
  public String getImagenLibro() { return this.imagenLibro; }
  
  /// Establece el nombre del archivo que sirve como imagen del libro.
  ///
  /// @param imagenLibro Nombre del archivo que sirve como imagen del libro.
  public void setImagenLibro(String imagenLibro) { this.imagenLibro = imagenLibro; }
  
}
