//
package co.edu.uptc.tiendalibros.gui.libro;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroBuilder;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;

/// Representa los datos de un libro que un usuario ingresó en un formulario.
///
/// Estos datos es para la comunicación de la interfaz gráfica hacia el controlador.
public record DatosEntradaLibro(
  Long id, String isbn, String titulo, Integer anioPublicacion, Long idCategoria, Long idEditorial, String imagenLibro,
  Optional<DatosEntradaFormatoLibro> formatoImpreso, Optional<DatosEntradaFormatoLibro> formatoDigital
) {
  
  /// Convierte estos datos de entrada en un objeto libro con sus dos formatos del modelo.
  /// 
  /// La categoría y la editorial del libro son objetos provisionales que solo tienen el ID.
  /// 
  /// En el backend, los repositorios cargan el objeto completo categoría y editorial
  /// consultando el ID.
  /// 
  /// Las reglas de negocio solo necesitan el ID para verificar que la categoría y la
  /// editorial existen.
  /// 
  /// Sin embargo, si se envía IDs nulos para categoría y editorial es porque el usuario no
  /// seleccionó una opción en las listas combobox.
  /// 
  /// `anioPublicacion` no debe ser nulo.
  ///
  /// @return Un [LibroConFormatos] con los mismos datos de este objeto.
  /// @throws IllegalArgumentException Si el formato impreso o el formato digital no son del
  ///                                  tipo que les corresponde.
  public LibroConFormatos aLibroConFormatos() {
    // del formulario libro solo se necesita capturar el ID de la categoría y el ID de la
    // editorial.
    // podemos crear un objeto provisional que solo tenga el ID. En el backend, se utiliza al
    // repositorio respectivo para que busque la categoría o la editorial basado en el ID
    Categoria categoriaProvisional = new Categoria(this.idCategoria(), null, null, null);
    Editorial editorialProvisional = new Editorial(this.idEditorial(), null);
    
    Libro libro = new LibroBuilder()
      .id(this.id)
      .isbn(this.isbn)
      .titulo(this.titulo)
      .anioPublicacion(this.anioPublicacion)
      .categoria(categoriaProvisional)
      .editorial(editorialProvisional)
      .imagenLibro(this.imagenLibro)
      .construir();
    
    return new LibroConFormatos(libro, convertirFormato(this.formatoImpreso), convertirFormato(this.formatoDigital));
  }
  
  /// Convierte los datos de entrada opcionales de un formato en un formato de libro
  /// opcional.
  /// 
  /// Como es opcional que el formulario y el usuario editen un objeto
  /// [DatosEntradaFormatoLibro], se debe extraer el formato de libro para enviarlo al
  /// controlador.
  /// 
  /// @param datosFormato Datos de entrada del formato. Vacío si el libro no lo ofrece.
  /// @return El formato de libro. Vacío si el libro no lo ofrece.
  private static Optional<FormatoLibro> convertirFormato(Optional<DatosEntradaFormatoLibro> datosFormato) {
    if (datosFormato.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
      datosFormato.get()
        .aFormatoLibro()
    );
  }
  
  public static Builder builder() {
    return new Builder();
  }
  
  public Builder aBuilder() {
    return new Builder(this);
  }
  
  /// Representa un constructor para [DatosEntradaLibro] usando el patrón de
  /// diseño builder().
  public static class Builder {
    private Long id;
    private String isbn;
    private String titulo;
    private Integer anioPublicacion;
    private Long idCategoria;
    private Long idEditorial;
    private String imagenLibro;
    private Optional<DatosEntradaFormatoLibro> formatoImpreso = Optional.empty();
    private Optional<DatosEntradaFormatoLibro> formatoDigital = Optional.empty();
    
    private Builder() {
    }
    
    private Builder(DatosEntradaLibro datosEntrada) {
      this.id = datosEntrada.id();
      this.isbn = datosEntrada.isbn();
      this.titulo = datosEntrada.titulo();
      this.anioPublicacion = datosEntrada.anioPublicacion();
      this.idCategoria = datosEntrada.idCategoria();
      this.idEditorial = datosEntrada.idEditorial();
      this.imagenLibro = datosEntrada.imagenLibro();
      this.formatoImpreso = datosEntrada.formatoImpreso();
      this.formatoDigital = datosEntrada.formatoDigital();
    }
    
    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder isbn(String isbn) {
      this.isbn = isbn;
      return this;
    }
    
    public Builder titulo(String titulo) {
      this.titulo = titulo;
      return this;
    }
    
    public Builder anioPublicacion(Integer anioPublicacion) {
      this.anioPublicacion = anioPublicacion;
      return this;
    }
    
    public Builder idCategoria(Long idCategoria) {
      this.idCategoria = idCategoria;
      return this;
    }
    
    public Builder idEditorial(Long idEditorial) {
      this.idEditorial = idEditorial;
      return this;
    }
    
    public Builder imagenLibro(String imagenLibro) {
      this.imagenLibro = imagenLibro;
      return this;
    }
    
    public Builder formatoImpreso(Optional<DatosEntradaFormatoLibro> formatoImpreso) {
      this.formatoImpreso = formatoImpreso;
      return this;
    }
    
    public Builder formatoDigital(Optional<DatosEntradaFormatoLibro> formatoDigital) {
      this.formatoDigital = formatoDigital;
      return this;
    }
    
    public DatosEntradaLibro construir() {
      return new DatosEntradaLibro(
        this.id, this.isbn, this.titulo, this.anioPublicacion, this.idCategoria, this.idEditorial, this.imagenLibro,
        this.formatoImpreso, this.formatoDigital
      );
    }
  }
  
}
