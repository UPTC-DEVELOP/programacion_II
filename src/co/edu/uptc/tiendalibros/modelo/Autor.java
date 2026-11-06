package co.edu.uptc.tiendalibros.modelo;

public class Autor extends ElementoBase {
  
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
    super(id);
    this.nombre = nombre;
    this.apellidos = apellidos;
  }
  
  
  public String getNombre() { return this.nombre; }
  
  public void setNombre(String nombre) { this.nombre = nombre; }
  
  public String getApellidos() { return this.apellidos; }
  
  public void setApellidos(String apellidos) { this.apellidos = apellidos; }
}
