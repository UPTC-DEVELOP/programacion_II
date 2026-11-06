//
package co.edu.uptc.tiendalibros.modelo;

/// Representa un formato comercial de un libro como obra intelectual. Tiene los datos de
/// formato y los datos comerciales del libro.
public class FormatoLibro extends ElementoBase {
  
  /// ID del libro como obra intelectual.
  private Long idLibro;
  
  /// Tipo de formato del formato comercial del libro.
  private TipoFormatoLibro tipoFormato;
  
  /// Precio base del formato comercial del libro antes del IVA.
  private Dinero precioBase;
  
  /// Tipo de IVA que se aplica al formato comercial del libro.
  private TipoIVA tipoIVA;
  
  /// Cantidad de ejemplares disponibles para la venta del formato comercial del libro.
  private int inventario;
  
  /// Cantidad de páginas del formato comercial del libro.
  private int cantidadPaginas;
  
  /// Estado del libro en el catálogo.
  private EstadoLibroCatalogo estadoLibroCatalogo;
  
  /// Crea un nuevo objeto [FormatoLibro] sin ID y con valores predetermindos.
  ///
  public FormatoLibro() {
    // invocar al constructor que tiene todos los atributos
    this(null, TipoFormatoLibro.IMPRESO, Dinero.CERO, TipoIVA.IVA_19, 0, 0, EstadoLibroCatalogo.PENDIENTE);
  }
  
  /// Crea un nuevo objeto [FormatoLibro] sin ID y los valores especificados.
  ///
  /// @param idLibro             ID del libro como obra intelectual.
  /// @param tipoFormato         Tipo de formato del formato comercial del libro.
  /// @param precioBase          Precio base del formato comercial del libro antes del IVA.
  /// @param tipoIva             Tipo de IVA que se aplica a la presentación del libro.
  /// @param inventario          Cantidad de ejemplares disponibles para la venta de la
  ///                            presentación del libro.
  /// @param cantidadPaginas     Cantidad de páginas del formato comercial del libro.
  /// @param estadoLibroCatalogo Estado del libro en el catálogo.
  public FormatoLibro(
    Long idLibro,
    TipoFormatoLibro tipoFormato,
    Dinero precioBase,
    TipoIVA tipoIVA,
    int inventario,
    int cantidadPaginas,
    EstadoLibroCatalogo estadoLibroCatalogo
  ) {
    // invocar al constructor que tiene todos los atributos
    this(null, idLibro, tipoFormato, precioBase, tipoIVA, inventario, cantidadPaginas, estadoLibroCatalogo);
  }
  
  /// Crea un nuevo objeto [FormatoLibro] con su ID y los valores especificados.
  ///
  /// @param id                  ID del formato comercial del libro.
  /// @param idLibro             ID del libro como obra intelectual.
  /// @param tipoFormato         Tipo de formato del formato comercial del libro.
  /// @param precioBase          Precio base del formato comercial del libro antes del IVA.
  /// @param tipoIva             Tipo de IVA que se aplica a la presentación del libro.
  /// @param inventario          Cantidad de ejemplares disponibles para la venta de la
  ///                            presentación del libro.
  /// @param cantidadPaginas     Cantidad de páginas del formato comercial del libro.
  /// @param estadoLibroCatalogo Estado del libro en el catálogo.
  public FormatoLibro(
    Long id,
    Long idLibro,
    TipoFormatoLibro tipoFormato,
    Dinero precioBase,
    TipoIVA tipoIVA,
    int inventario,
    int cantidadPaginas,
    EstadoLibroCatalogo estadoLibroCatalogo
  ) {
    super(id);
    this.idLibro = idLibro;
    this.tipoFormato = tipoFormato;
    this.precioBase = precioBase;
    this.tipoIVA = tipoIVA;
    this.inventario = inventario;
    this.cantidadPaginas = cantidadPaginas;
    this.estadoLibroCatalogo = estadoLibroCatalogo;
  }
  
  /// Crea un nuevo objeto [FormatoLibroBuilder] a partir del objeto builder especificado.
  ///
  /// @param builder Objeto builder para crear el libro.
  public FormatoLibro(FormatoLibroBuilder builder) {
    this(
      builder.id(),
      builder.idLibro(),
      builder.tipoFormatoLibro(),
      builder.precioBase(),
      builder.tipoIVA(),
      builder.inventario(),
      builder.cantidadPaginas(),
      builder.estadoLibroCatalogo()
    );
  }
  
  /// Obtiene el ID del libro como obra intelectual.
  ///
  /// @return ID del libro como obra intelectual.
  public Long getIdLibro() { return this.idLibro; }
  
  /// Establece el ID del libro como obra intelectual.
  ///
  /// @param idLibro ID del libro como obra intelectual.
  public void setIdLibro(Long idLibro) { this.idLibro = idLibro; }
  
  /// Obtiene el tipo de formato del formato comercial del libro.
  ///
  /// @return Tipo de formato del formato comercial del libro.
  public TipoFormatoLibro getTipoFormato() { return this.tipoFormato; }
  
  /// Establece el tipo de formato del formato comercial del libro.
  ///
  /// @param formato Tipo de formato del formato comercial del libro.
  public void setTipoFormato(TipoFormatoLibro formato) { this.tipoFormato = formato; }
  
  /// Obtiene el precio base del formato comercial del libro antes del IVA.
  ///
  /// @return Precio base del formato comercial del libro antes del IVA.
  public Dinero getPrecioBase() { return this.precioBase; }
  
  /// Establece el precio base del formato comercial del libro antes del IVA.
  ///
  /// @param precioBase Precio base del formato comercial del libro antes del IVA.
  public void setPrecioBase(Dinero precioBase) { this.precioBase = precioBase; }
  
  /// Obtiene el tipo de IVA que se aplica a la presentación del libro.
  ///
  /// @return Tipo de IVA que se aplica a la presentación del libro.
  public TipoIVA getTipoIVA() { return this.tipoIVA; }
  
  /// Establece el Tipo de IVA que se aplica a la presentación del libro.
  ///
  /// @param tipoIVA Tipo de IVA que se aplica a la presentación del libro.
  public void setTipoIVA(TipoIVA tipoIVA) { this.tipoIVA = tipoIVA; }
  
  /// Obtiene la cantidad de ejemplares disponibles para la venta del formato comercial del
  /// libro.
  ///
  /// @return Cantidad de ejemplares disponibles para la venta del formato comercial
  ///         del libro.
  public int getInventario() { return this.inventario; }
  
  /// Establece la antidad de ejemplares disponibles para la venta del formato comercial del
  /// libro.
  ///
  /// @param inventario Cantidad de ejemplares disponibles para la venta del formato
  ///                   comercial del libro.
  public void setInventario(int inventario) { this.inventario = inventario; }
  
  /// Obtiene la cantidad de páginas del formato comercial del libro.
  ///
  /// @return Cantidad de páginas del formato comercial del libro.
  public int getCantidadPaginas() { return this.cantidadPaginas; }
  
  /// Establece la cantidad de páginas del formato comercial del libro.
  ///
  /// @param cantidadPaginas Cantidad de páginas del formato comercial del libro.
  public void setCantidadPaginas(int cantidadPaginas) { this.cantidadPaginas = cantidadPaginas; }
  
  /// Obtiene el estado del libro en el catálogo.
  ///
  /// @return Estado del libro en el catálogo.
  public EstadoLibroCatalogo getEstadoLibroCatalogo() { return this.estadoLibroCatalogo; }
  
  /// Establece el estado del libro en el catálogo.
  ///
  /// @param estadoLibroCatalogo Estado del libro en el catálogo.
  public void setEstadoLibroCatalogo(EstadoLibroCatalogo estadoLibroCatalogo) {
    this.estadoLibroCatalogo = estadoLibroCatalogo;
  }
}
