//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioUsuario;
import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;

/// Regla de validación para determinar que el empleado que se quiere eliminar no es el último
/// administrador habilitado del sistema.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si es el último administrador
/// habilitado.
public class ReglaEmpleadoNoEsUltimoAdministrador implements IReglaValidacion<EmpleadoConUsuario> {
  
  /// Repositorio de las cuentas de usuario.
  private final IRepositorioUsuario repositorioUsuario;
  
  /// Crea una nueva regla para conservar al menos un administrador.
  ///
  /// @param repositorioUsuario Repositorio para contar los administradores habilitados.
  public ReglaEmpleadoNoEsUltimoAdministrador(IRepositorioUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }
  
  @Override
  public boolean cumple(EmpleadoConUsuario empleado) {
    Usuario cuenta = empleado.getUsuario();
    
    // si no es un administrador habilitado, no cambia la cantidad de administradores
    if ((cuenta.isHabilitado() == false) || (cuenta.tieneRol(Rol.ADMINISTRADOR) == false)) {
      return true;
    }
    
    // es un administrador habilitado: debe haber otro
    return this.repositorioUsuario.contarCuentasHabilitadasConRol(Rol.ADMINISTRADOR) > 1;
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
    
    String mensajeError = "No se puede eliminar al último administrador habilitado del sistema.";
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
  }
}
