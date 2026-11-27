package co.edu.uptc.tiendalibros.negocio.seguridad.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Credencial;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la credencial tiene un correo electrónico.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si la credencial no tiene
/// correo electrónico.
public class ReglaCredencialCorreoNoVacio implements IReglaValidacion<Credencial> {
  
  /// Crea una nueva regla.
  public ReglaCredencialCorreoNoVacio() {
  }
  
  @Override
  public boolean cumple(Credencial credencial) {
    // cumple si el correo electrónico no está vacío
    return credencial.getCorreoElectronico()
      .trim()
      .isEmpty() == false;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Credencial credencial) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(credencial) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "Escriba su correo electrónico.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("correoElectronico", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio de atributo
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("correoElectronico", mensajeError)
    );
  }
  
}