//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEmpleado;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioUsuario;
import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;

/// Regla de validación para determinar que, al editar un empleado, el sistema no se queda
/// sin administradores habilitados.
///
/// Si el empleado actual es hoy un administrador habilitado y los datos nuevos le quitan
/// el rol o lo deshabilitan, entonces debe haber otro administrador habilitado.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si los datos violan la regla.
public class ReglaEmpleadoMantieneUnAdministrador implements IReglaValidacion<DatosEmpleado> {
  
  /// Repositorio de las cuentas de usuario.
  private final IRepositorioUsuario repositorioUsuario;
  
  /// Repositorio de los empleados.
  private final IRepositorioEmpleado repositorioEmpleado;
  
  /// Crea una nueva regla para conservar al menos un administrador.
  ///
  /// @param repositorioEmpleado Repositorio de los empleados.
  /// @param repositorioUsuario  Repositorio para consultar usuarios y administradores.
  public ReglaEmpleadoMantieneUnAdministrador(
    IRepositorioEmpleado repositorioEmpleado,
    IRepositorioUsuario repositorioUsuario
  ) {
    this.repositorioEmpleado = repositorioEmpleado;
    this.repositorioUsuario = repositorioUsuario;
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // un empleado nuevo no le quita nada al sistema
    if (datos.id() == null) {
      return true;
    }
    
    Optional<EmpleadoConUsuario> guardado = this.repositorioEmpleado.buscarPorId(datos.id());
    
    // si el empleado ya no existe, otra regla lo reporta
    if (guardado.isEmpty()) {
      return true;
    }
    
    Usuario cuenta = guardado.get().getUsuario();
    boolean eraAdministradorHabilitado = cuenta.isHabilitado() && cuenta.tieneRol(Rol.ADMINISTRADOR);
    boolean seguiraSiendolo = datos.habilitado() && (datos.rol() == Rol.ADMINISTRADOR);
    
    // si no era administrador habilitado, o lo seguirá siendo, no cambia la cantidad
    if ((eraAdministradorHabilitado == false) || seguiraSiendolo) {
      return true;
    }
    
    // aquí, el empleado deja de ser administrador habilitado: debe haber otro
    return this.repositorioUsuario.contarCuentasHabilitadasConRol(Rol.ADMINISTRADOR) > 1;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(
    ResultadoValidacion errores,
    DatosEmpleado datos
  ) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(datos) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "No se puede dejar el sistema sin un administrador habilitado.";
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
  }
}
