package co.edu.uptc.tiendalibros.negocio.seguridad.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Permiso;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;
import co.edu.uptc.tiendalibros.negocio.seguridad.SesionUsuario;

/// Regla de validación para determinar que el usuario de la sesión tiene permiso para
/// hacer una acción.
///
/// El usuario cumple la regla si tiene al menos uno de los permisos aceptados. Si no hay
/// sesión iniciada, no cumple la regla.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si el usuario no tiene el permiso.
///
/// @param <T> Tipo del elemento sobre el que se hace la acción.
public class ReglaUsuarioAutorizado<T> implements IReglaValidacion<T> {
  
  /// Sesión que dice cuál usuario está haciendo la acción.
  private final SesionUsuario sesionUsuario;
  
  /// Descripción de la acción que se usará para el mensaje de error.
  private final String accion;
  
  /// Permisos que aceptan la acción.
  private final Permiso[] permisosAceptados;
  
  /// Crea una nueva regla para verificar que el usuario tiene permiso para la acción.
  ///
  /// @param sesionUsuario     Sesión del usuario que hace la acción.
  /// @param accion            Descripción de la acción: "no tiene permiso para ...".
  /// @param permisosAceptados Permisos que aceptan la acción.
  public ReglaUsuarioAutorizado(
    SesionUsuario sesionUsuario,
    String accion,
    Permiso... permisosAceptados
  ) {
    this.sesionUsuario = sesionUsuario;
    this.accion = accion;
    this.permisosAceptados = permisosAceptados.clone();
  }
  
  @Override
  public boolean cumple(T elemento) {
    // cumple si el usuario tiene uno de los permisos aceptados
    for (Permiso permiso : this.permisosAceptados) {
      if (this.sesionUsuario.tienePermiso(permiso)) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, T elemento) {
    // si el usuario tiene permiso, devolver vacío
    if (cumple(elemento) == true) {
      return Optional.empty();
    }
    // aquí, el usuario no tiene permiso
    
    String mensajeError = String.format(
      "Tu cuenta no tiene permiso para %s. Inicia sesión con una cuenta autorizada.",
      this.accion
    );
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio general
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
  }
}