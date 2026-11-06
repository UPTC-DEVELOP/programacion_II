//
package co.edu.uptc.tiendalibros.modelo;

/// Representa una asociación entre un libro intelectual y sus autores.
public class AutorLibro extends ElementoBase {
  
  /// ID del libro escrito por el autor.
  private Long idLibro;
  
  /// ID del autor del libro.
  private Long idAutor;
  
  /// Orden de autoría del autor.
  private int ordenAutor;
  
  /// Crea un nuevo objeto [AutorLibro] con valores predeterminados.
  public AutorLibro() {
    // invocar al constructor que tiene todos los atributos
    this(null, null, null, 0);
  }
  
  /// Crea un nuevo objeto [AutorLibro] sin ID y con los valores especificados.
  ///
  /// @param idLibro    ID del libro escrito por el autor.
  /// @param idAutor    ID del autor del libro.
  /// @param orderAutor Orden de autoría del autor.
  public AutorLibro(
    Long idLibro,
    Long idAutor,
    int ordenAutor
  ) {
    this(null, idLibro, idAutor, ordenAutor);
  }
  
  /// Crea un nuevo objeto [AutorLibro] con el ID y con los valores especificados.
  ///
  /// @id ID de la asociación del libro y sus autores.
  /// @param idLibro    ID del libro escrito por el autor.
  /// @param idAutor    ID del autor del libro.
  /// @param orderAutor Orden de autoría del autor.
  public AutorLibro(
    Long id,
    Long idLibro,
    Long idAutor,
    int ordenAutor
  ) {
    super(id);
    this.idLibro = idLibro;
    this.idAutor = idAutor;
    this.ordenAutor = ordenAutor;
  }
  
  /// Obtiene el ID del libro escrito por el autor.
  ///
  /// @return ID del libro escrito por el autor.
  public Long getIdLibro() { return this.idLibro; }
  
  /// Establece el ID del libro escrito por el autor.
  ///
  /// @param idLibro ID del libro escrito por el autor.
  public void setIdLibro(Long idLibro) { this.idLibro = idLibro; }
  
  /// Obtiene el ID del autor del libro.
  ///
  /// @return ID del autor del libro.
  public Long getIdAutor() { return this.idAutor; }
  
  /// Establece el ID del autor del libro.
  ///
  /// @param idAutor ID del autor del libro.
  public void setIdAutor(Long idAutor) { this.idAutor = idAutor; }
  
  /// Obtiene el orden de autoría del autor.
  ///
  /// @return Orden de autoría del autor.
  public int getOrdenAutor() { return this.ordenAutor; }
  
  /// Establece el orden de autoría del autor.
  ///
  /// @param ordenAutor Orden de autoría del autor.
  public void setOrdenAutor(int ordenAutor) { this.ordenAutor = ordenAutor; }
}
