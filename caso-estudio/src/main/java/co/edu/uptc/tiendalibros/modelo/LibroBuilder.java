//
package co.edu.uptc.tiendalibros.modelo;

import java.util.ArrayList;
import java.util.List;

/// Crea un objeto [Libro] usando el patrón de diseño Builder.
public class LibroBuilder {
  
  /// ID del libro
  private Long id;
  
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
  
  /// Crea un objeto [LibroBuilder] para construir un nuevo objeto [Libro].
  public LibroBuilder() {
    this.id = null;
    this.isbn = "";
    this.titulo = "";
    this.anioPublicacion = 0;
    this.categoria = null;
    this.editorial = null;
    this.imagenLibro = "";
    this.listaAutoria = new ArrayList<>();
  }
  
  /// Construye un nuevo objeto [Libro] a partir de los datos de este builder.
  public Libro construir() {
    return new Libro(this);
  }
  
  /// Establece el ID del libro
  public LibroBuilder id(Long id) {
    this.id = id;
    return this;
  }
  
  /// Obtiene el ID del libro
  public Long id() {
    return this.id;
  }
  
  /// Obtiene el ISBN del libro ((International Standard Book Number).
  public String isbn() {
    return this.isbn;
  }
  
  /// Establece el ISBN del libro ((International Standard Book Number).
  public LibroBuilder isbn(String isbn) {
    this.isbn = isbn;
    return this;
  }
  
  /// Establece el título del libro.
  public LibroBuilder titulo(String titulo) {
    this.titulo = titulo;
    return this;
  }
  
  /// Obtiene el título del libro.
  public String titulo() {
    return this.titulo;
  }
  
  /// Establece el año de publicación del libro.
  public LibroBuilder anioPublicacion(int anioPublicacion) {
    this.anioPublicacion = anioPublicacion;
    return this;
  }
  
  /// Obtiene el año de publicación del libro.
  public int anioPublicacion() {
    return this.anioPublicacion;
  }
  
  /// Establece la categoría que sirve para clasificar el libro.
  public LibroBuilder categoria(Categoria categoria) {
    this.categoria = categoria;
    return this;
  }
  
  /// Obtiene la categoría que sirve para clasificar el libro.
  public Categoria categoria() {
    return this.categoria;
  }
  
  /// Establece la editorial responsable de publicar el libro.
  public LibroBuilder editorial(Editorial editorial) {
    this.editorial = editorial;
    return this;
  }
  
  /// Obtiene la editorial responsable de publicar el libro.
  public Editorial editorial() {
    return this.editorial;
  }
  
  /// Establece la lista que asocia el libro con sus autores.
  public LibroBuilder listaAutoria(List<AutorLibro> listaAutoria) {
    this.listaAutoria = listaAutoria;
    return this;
  }
  
  /// Obtiene la lista que asocia el libro con sus autores.
  public List<AutorLibro> listaAutoria() {
    return this.listaAutoria;
  }
  
  /// Establece el nombre del archivo que sirve como imagen del libro.
  public LibroBuilder imagenLibro(String imagenLibro) {
    this.imagenLibro = imagenLibro;
    return this;
  }
  
  /// Obtiene el nombre del archivo que sirve como imagen del libro.
  public String imagenLibro() {
    return this.imagenLibro;
  }
  
}
