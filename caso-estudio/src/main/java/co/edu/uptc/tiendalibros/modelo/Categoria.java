package co.edu.uptc.tiendalibros.modelo;

public class Categoria extends ElementoBase {
  
  private String nombre;
  private String descripcion;
  private String codigo;
  
  public Categoria() {
    this("", "", "");
  }
  
  public Categoria(
    String nombre,
    String descripcion,
    String codigo
  ) {
    this(null, nombre, descripcion, codigo);
  }
  
  public Categoria(
    Long id,
    String nombre,
    String descripcion,
    String codigo
  ) {
    super(id);
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.codigo = codigo;
  }
  
  
  public String getNombre() { return this.nombre; }
  
  public void setNombre(String nombre) { this.nombre = nombre; }
  
  public String getDescripcion() { return this.descripcion; }
  
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  
  public String getCodigo() { return this.codigo; }
  
  public void setCodigo(String codigo) { this.codigo = codigo; }
}