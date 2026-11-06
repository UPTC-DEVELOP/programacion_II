/// 
package co.edu.uptc.tiendalibros.negocio.excepciones;

import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

///
public non-sealed class ViolacionReglaNegocioGeneralException extends ViolacionReglaNegocioException {
  public ViolacionReglaNegocioGeneralException(String mensaje) {
    super(mensaje);
  }
  
  @Override
  public void reportarEn(ResultadoValidacion errores) {
    errores.agregarGeneral(getMessage());
  }
}
