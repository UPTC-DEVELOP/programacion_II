/// 
package co.edu.uptc.tiendalibros.negocio.excepciones;

import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

///
public final class ViolacionReglaNegocioAtributoException extends ViolacionReglaNegocioException {
  
  private String atributo;
  
  public ViolacionReglaNegocioAtributoException(
    String atributo,
    String mensaje
  ) {
    super(mensaje);
    this.atributo = atributo;
  }
  
  public String getAtributo() { return this.atributo; }
  
  @Override
  public void reportarEn(ResultadoValidacion errores) {
    errores.agregarAtributo(this.atributo, getMessage());
  }
}
