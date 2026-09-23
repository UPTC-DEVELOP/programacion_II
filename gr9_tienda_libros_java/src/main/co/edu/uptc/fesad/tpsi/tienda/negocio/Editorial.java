//
package main.co.edu.uptc.fesad.tpsi.tienda.negocio;

//
public class Editorial {
  
  private Long id;
  private String nombre;
  
  public Editorial() {
    this("");
  }
  
  public Editorial(String nombre) {
    this.nombre = nombre;
  }
  
  public Editorial(
    Long id,
    String nombre
  ) {
    this.id = id;
    this.nombre = nombre;
  }
  
  public Long getId() { return this.id; }
  
  public void setId(Long id) { this.id = id; }
  
  public String getNombre() { return this.nombre; }
  
  public void setNombre(String nombre) { this.nombre = nombre; }
  
}
