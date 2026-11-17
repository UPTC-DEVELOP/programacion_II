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
  
  /// Categoría que sirve para clasificar el libro.
  private Categoria categoria;
  
  /// Editorial responsable de publicar el libro.
  private Editorial editorial;
  
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
  /// @param categoria       Categoría que sirve para clasificar el libro.
  /// @param editorial       Editorial responsable de publicar el libro.
  /// @param listaAutoria    Lista que asocia el libro con sus autores.
  /// @param imagenLibro     Nombre del archivo que sirve como imagen del libro.
  public Libro(
    String isbn,
    String titulo,
    int anioPublicacion,
    Categoria categoria,
    Editorial editorial,
    List<AutorLibro> listaAutoria,
    String imagenLibro
  ) {
    // invocar al constructor que tiene todos los atributos
    this(null, isbn, titulo, anioPublicacion, categoria, editorial, listaAutoria, imagenLibro);
  }
  
  /// Crea un nuevo objeto [Libro] con los valores especificados.
  ///
  /// @param id              ID del libro.
  /// @param isbn            ISBN del libro.
  /// @param titulo          Título del libro.
  /// @param anioPublicacion Año de publicación del libro.
  /// @param categoria       Categoría que sirve para clasificar el libro.
  /// @param editorial       Editorial responsable de publicar el libro.
  /// @param listaAutoria    Lista que asocia el libro con sus autores.
  /// @param imagenLibro     Nombre del archivo que sirve como imagen del libro.
  public Libro(
    Long id,
    String isbn,
    String titulo,
    int anioPublicacion,
    Categoria categoria,
    Editorial editorial,
    List<AutorLibro> listaAutoria,
    String imagenLibro
  ) {
    super(id);
    this.isbn = isbn;
    this.titulo = titulo;
    this.anioPublicacion = anioPublicacion;
    this.categoria = categoria;
    this.editorial = editorial;
    this.imagenLibro = imagenLibro;
    
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
      builder.categoria(),
      builder.editorial(),
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
  
  /// Obtiene la editorial responsable de publicar el libro.
  ///
  /// @return Editorial responsable de publicar el libro.
  public Editorial getEditorial() { return this.editorial; }
  
  /// Establece la editorial responsable de publicar el libro.
  ///
  /// @param editorial Editorial responsable de publicar el libro.
  public void setEditorial(Editorial editorial) { this.editorial = editorial; }
  
  /// Obtiene la categoría del libro.
  ///
  /// @return Categoría del libro.
  public Categoria getCategoria() { return this.categoria; }
  
  /// Establece la categoría del libro.
  ///
  /// @param categoria Categoría del libro.
  public void setCategoria(Categoria categoria) { this.categoria = categoria; }
  
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
