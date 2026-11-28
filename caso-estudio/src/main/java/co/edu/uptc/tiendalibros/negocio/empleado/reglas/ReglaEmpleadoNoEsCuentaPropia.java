//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;
import co.edu.uptc.tiendalibros.negocio.seguridad.SesionUsuario;

/// Regla de validación para determinar que el empleado que se quiere eliminar no es el usuario
/// que tiene la sesión iniciada.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si el empleado es el usuario de la
/// sesión.
public class ReglaEmpleadoNoEsCuentaPropia implements IReglaValidacion<EmpleadoConUsuario> {
  
  /// Sesión que dice cuál usuario está haciendo la acción.
  private final SesionUsuario sesionUsuario;
  
  /// Crea una nueva regla para impedir que un usuario elimine su propia cuenta.
  ///
  /// @param sesionUsuario Sesión del usuario que hace la acción.
  public ReglaEmpleadoNoEsCuentaPropia(SesionUsuario sesionUsuario) {
    this.sesionUsuario = sesionUsuario;
  }
  
  @Override
  public boolean cumple(EmpleadoConUsuario empleado) {
    // sin sesión no hay cuenta propia
    if (this.sesionUsuario.haySesion() == false) {
      return true;
    }
    
    return empleado.getUsuario().getId().equals(this.sesionUsuario.getUsuario().getId()) == false;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(
    ResultadoValidacion errores,
    EmpleadoConUsuario empleado
  ) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(empleado) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "No puedes eliminar tu propia cuenta. Pídeselo a otro administrador.";
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
  }
}
