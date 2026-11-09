//
package co.edu.uptc.tiendalibros.modelo;

/// Representa una editorial responsable de la publicación de un libro.
public class Editorial extends ElementoBase {
  
  /// Longitud máxima del nombre de la editorial.
  public static final int LONGITUD_MAXIMA_NOMBRE = 60;
  
  /// Nombre de la editorial.
  private String nombre;
  
  /// Crea un nuevo objeto [Editorial] con valores predeterminados.
  public Editorial() {
    this("");
  }
  
  /// Crea un nuevo objeto [Editorial] con el nombre especificados.
  ///
  /// @param nombre Nombre de la editorial.
  public Editorial(String nombre) {
    this(null, nombre);
  }
  
  /// Crea un nuevo objeto [Editorial] con el id y el nombre especificados.
  ///
  /// @param id     ID de la editorial.
  /// @param nombre Nombre de la editorial.
  public Editorial(
    Long id,
    String nombre
  ) {
    super(id);
    this.nombre = nombre;
  }
  
  /// Obtiene el nombre de la editorial.
  ///
  /// @return Nombre de la editorial.
  public String getNombre() { return this.nombre; }
  
  /// Establece el nombre de la editorial.
  ///
  /// @nombre Nombre de la editorial.
  public void setNombre(String nombre) { this.nombre = nombre; }
  
}
