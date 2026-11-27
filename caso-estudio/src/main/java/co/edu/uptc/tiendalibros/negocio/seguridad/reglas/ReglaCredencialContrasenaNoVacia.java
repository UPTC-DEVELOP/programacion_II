package co.edu.uptc.tiendalibros.negocio.seguridad.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Credencial;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la credencial tiene una contraseña.
///
/// Esta regla se aplica al iniciar sesión para pedir que la contraseña no esté vacía.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si la credencial no
/// tiene contraseña.
public class ReglaCredencialContrasenaNoVacia implements IReglaValidacion<Credencial> {
  
  /// Crea una nueva regla.
  public ReglaCredencialContrasenaNoVacia() {
  }
  
  @Override
  public boolean cumple(Credencial credencial) {
    // cumple si la credencial tiene algún valor de contraseña
    return credencial.getContrasena().length > 0;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, Credencial credencial) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(credencial) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "Escriba su contraseña.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("contrasena", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio de atributo
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("contrasena", mensajeError)
    );
  }
  
}