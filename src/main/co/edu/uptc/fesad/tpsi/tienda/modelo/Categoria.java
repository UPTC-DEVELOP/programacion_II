package main.co.edu.uptc.fesad.tpsi.tienda.modelo;

public class Categoria {
  
  private Long id;
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
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.codigo = codigo;
  }
  
  public Long getId() { return this.id; }
  
  public void setId(Long id) { this.id = id; }
  
  public String getNombre() { return this.nombre; }
  
  public void setNombre(String nombre) { this.nombre = nombre; }
  
  public String getDescripcion() { return this.descripcion; }
  
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  
  public String getCodigo() { return this.codigo; }
  
  public void setCodigo(String codigo) { this.codigo = codigo; }
}