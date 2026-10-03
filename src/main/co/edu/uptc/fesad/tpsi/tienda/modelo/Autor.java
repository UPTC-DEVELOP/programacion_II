package main.co.edu.uptc.fesad.tpsi.tienda.modelo;

public class Autor {
  
  private Long id;
  private String nombre;
  private String apellidos;
  
  public Autor() {
    this("", "");
  }
  
  public Autor(
    String nombre,
    String apellidos
  ) {
    this(null, nombre, apellidos);
  }
  
  public Autor(
    Long id,
    String nombre,
    String apellidos
  ) {
    this.id = id;
    this.nombre = nombre;
    this.apellidos = apellidos;
  }
  
  public Long getId() { return this.id; }
  
  public void setId(Long id) { this.id = id; }
  
  public String getNombre() { return this.nombre; }
  
  public void setNombre(String nombre) { this.nombre = nombre; }
  
  public String getApellidos() { return this.apellidos; }
  
  public void setApellidos(String apellidos) { this.apellidos = apellidos; }
}
