//
package co.edu.uptc.tiendalibros.modelo;

import java.util.ArrayList;
import java.util.List;

/// Crea un objeto [Libro] usando el patrón de diseño Builder.
public class LibroBuilder extends ElementoBaseBuilder<Libro> {
  
  /// ID del libro
  private Long id;
  
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
  
  public LibroBuilder() {
    id(null);
    this.isbn = "";
    this.titulo = "";
    this.anioPublicacion = 0;
    this.idCategoria = null;
    this.idEditorial = null;
    this.imagenLibro = "";
    this.listaAutoria = new ArrayList<>();
  }
  
  @Override
  public Libro construir() {
    return new Libro(this);
  }
  
  @Override
  public LibroBuilder id(Long id) {
    this.id = id;
    return this;
  }
  
  @Override
  public Long id() {
    return this.id;
  }
  
  public String isbn() {
    return this.isbn;
  }
  
  public LibroBuilder isbn(String isbn) {
    this.isbn = isbn;
    return this;
  }
  
  public LibroBuilder titulo(String titulo) {
    this.titulo = titulo;
    return this;
  }
  
  public String titulo() {
    return this.titulo;
  }
  
  public LibroBuilder anioPublicacion(int anioPublicacion) {
    this.anioPublicacion = anioPublicacion;
    return this;
  }
  
  public int anioPublicacion() {
    return this.anioPublicacion;
  }
  
  public LibroBuilder idCategoria(Long idCategoria) {
    this.idCategoria = idCategoria;
    return this;
  }
  
  public Long idCategoria() {
    return this.idCategoria;
  }
  
  public LibroBuilder idEditorial(Long idEditorial) {
    this.idEditorial = idEditorial;
    return this;
  }
  
  public Long idEditorial() {
    return this.idEditorial;
  }
  
  public LibroBuilder listaAutoria(List<AutorLibro> listaAutoria) {
    this.listaAutoria = listaAutoria;
    return this;
  }
  
  public List<AutorLibro> listaAutoria() {
    return this.listaAutoria;
  }
  
  public LibroBuilder imagenLibro(String imagenLibro) {
    this.imagenLibro = imagenLibro;
    return this;
  }
  
  public String imagenLibro() {
    return this.imagenLibro;
  }
  
}
