package co.edu.uptc.tiendalibros.gui.libro;

import java.util.List;

import co.edu.uptc.tiendalibros.gui.componentes.ElementoVista;
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

public class ElementoVistaLibroConFormatos extends ElementoVista<LibroConFormatos> {
  
  /// Lista de categorías disponibles para mostrar en el formulario.
  private final List<Categoria> categorias;
  
  /// Lista de editoriales disponibles para mostrar en el formulario.
  private final List<Editorial> editoriales;
  
  /// Formato impreso del libro
  private final ElementoVistaFormatoLibro formatoImpresoVista;
  
  /// Formato digital del libro
  private final ElementoVistaFormatoLibro formatoDigitalVista;
  
  
  /// Crea un objeto [ElementoVistaLibroConFormatos] con los valores especificados.
  ///
  /// La vista de cada formato se deriva del libro: si el libro no ofrece un formato, su
  /// vista usa valores predeterminados y queda marcada como no ofrecida.
  ///
  /// @param libroConFormatos Libro con sus formatos opcionales.
  /// @param categorias       Lista de categorías disponibles para el formulario.
  /// @param editoriales      Lista de editoriales disponibles para el formulario.
  /// @param errores          Posibles errores de validación.
  public ElementoVistaLibroConFormatos(
    LibroConFormatos libroConFormatos,
    List<Categoria> categorias,
    List<Editorial> editoriales,
    ResultadoValidacion errores
  ) {
    super(libroConFormatos, errores);
    
    // esta vista debe visualizar los formatos del libro, ya sea el formato ofrecido por el
    // libro, o ya sea un formato predeterminado
    
    this.formatoImpresoVista = ElementoVistaFormatoLibro
      .crear(libroConFormatos.getFormatoImpreso(), TipoFormatoLibro.IMPRESO);
    
    this.formatoDigitalVista = ElementoVistaFormatoLibro
      .crear(libroConFormatos.getFormatoDigital(), TipoFormatoLibro.DIGITAL);
    
    // no agregar listas null
    this.categorias = categorias != null ? categorias : List.of();
    this.editoriales = editoriales != null ? editoriales : List.of();
  }
  
  /// Obtiene la lista de categorías disponibles para el formulario.
  ///
  /// @return Lista de categorías.
  public List<Categoria> getCategorias() { return this.categorias; }
  
  /// Obtiene la lista de editoriales disponibles para el formulario.
  ///
  /// @return Lista de editoriales.
  public List<Editorial> getEditoriales() { return this.editoriales; }
  
  /// Obtiene el formato impreso del libro para el formulario.
  public ElementoVistaFormatoLibro getFormatoImpreso() { return this.formatoImpresoVista; }
  
  /// Obtiene el formato digital del libro para el formulario.
  public ElementoVistaFormatoLibro getFormatoDigital() { return this.formatoDigitalVista; }
  
}