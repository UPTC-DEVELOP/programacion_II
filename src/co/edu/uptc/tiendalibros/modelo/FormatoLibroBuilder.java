///
package co.edu.uptc.tiendalibros.modelo;

///
public class FormatoLibroBuilder extends ElementoBaseBuilder<FormatoLibro> {
  
  /// ID del formato libro builder
  private Long id;
  
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

  public FormatoLibroBuilder() {
    id(null);
    this.idLibro = null;
    this.tipoFormato = TipoFormatoLibro.IMPRESO;
    this.precioBase = Dinero.CERO;
    this.tipoIVA = TipoIVA.IVA_19;
    this.inventario = 0;
    this.cantidadPaginas = 0;
    this.estadoLibroCatalogo = EstadoLibroCatalogo.PENDIENTE;
  }
  
  @Override
  public FormatoLibro construir() {
    return new FormatoLibro(this);
  }
  
  @Override
  public FormatoLibroBuilder id(Long id) {
    this.id = id;
    return this;
  }
  
  @Override
  public Long id() {
    return this.id;
  }

  
  
  public Long idLibro() {
    return this.idLibro;
  }
  
  public FormatoLibroBuilder idLibro(Long idLibro) {
    this.idLibro = idLibro;
    return this;
  }
  
  public TipoFormatoLibro tipoFormatoLibro() {
    return this.tipoFormato;
  }
  
  public FormatoLibroBuilder tipoFormatoLibro(TipoFormatoLibro tipoFormato) {
    this.tipoFormato = tipoFormato;
    return this;
  }
  
  public Dinero precioBase() {
    return this.precioBase;
  }
  
  public FormatoLibroBuilder precioBase(Dinero precioBase) {
    this.precioBase = precioBase;
    return this;
  }
  
  public TipoIVA tipoIVA() {
    return this.tipoIVA;
  }
  
  public FormatoLibroBuilder tipoIVA(TipoIVA tipoIVA) {
    this.tipoIVA = tipoIVA;
    return this;
  }
  
  public int inventario() {
    return this.inventario;
  }
  
  public FormatoLibroBuilder inventario(int inventario) {
    this.inventario = inventario;
    return this;
  }
  
  public int cantidadPaginas() {
    return this.cantidadPaginas;
  }
  
  public FormatoLibroBuilder cantidadPaginas(int cantidadPaginas) {
    this.cantidadPaginas = cantidadPaginas;
    return this;
  }
  
  /// Obtiene el estado del libro en el catálogo.
  ///
  /// @return Estado del libro en el catálogo.
  public EstadoLibroCatalogo estadoLibroCatalogo() {
    return this.estadoLibroCatalogo;
  }
  
  /// Establece el estado del libro en el catálogo.
  ///
  /// @param estadoLibroCatalogo Estado del libro en el catálogo.
  public FormatoLibroBuilder estadoLibroCatalogo(EstadoLibroCatalogo estadoLibroCatalogo) {
    this.estadoLibroCatalogo = estadoLibroCatalogo;
    return this;
  }
  
}
