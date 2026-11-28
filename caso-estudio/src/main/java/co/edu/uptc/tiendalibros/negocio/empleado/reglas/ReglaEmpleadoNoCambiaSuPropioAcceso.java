//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEmpleado;
import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;
import co.edu.uptc.tiendalibros.negocio.seguridad.SesionUsuario;

/// Regla de validación para determinar que un empleado no cambia su propio acceso: ni su
/// rol ni si su cuenta está habilitada.
///
/// Evita que un administrador se quite a sí mismo el acceso a la administración.
/// 
/// Para los otros empleados esta regla siempre se cumple.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si los datos violan la regla.
public class ReglaEmpleadoNoCambiaSuPropioAcceso implements IReglaValidacion<DatosEmpleado> {
  
  /// Sesión que dice cuál usuario está haciendo la acción.
  private final SesionUsuario sesionUsuario;
  
  /// Repositorio de los empleados.
  private final IRepositorioEmpleado repositorio;
  
  /// Crea una nueva regla para proteger el acceso del propio usuario.
  ///
  /// @param sesionUsuario Sesión del usuario que edita al empleado.
  /// @param repositorio   Repositorio para consultar la cuenta guardada del empleado.
  public ReglaEmpleadoNoCambiaSuPropioAcceso(
    SesionUsuario sesionUsuario,
    IRepositorioEmpleado repositorio
  ) {
    this.sesionUsuario = sesionUsuario;
    this.repositorio = repositorio;
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // un empleado nuevo, o sin sesión, no tiene una cuenta propia que proteger
    if ((datos.id() == null) || (this.sesionUsuario.haySesion() == false)) {
      return true;
    }
    
    Optional<EmpleadoConUsuario> guardado = this.repositorio.buscarPorId(datos.id());
    
    // si el empleado ya no existe, otra regla lo reporta
    if (guardado.isEmpty()) {
      return true;
    }
    
    Usuario cuenta = guardado.get().getUsuario();
    
    // si la cuenta que se edita no es la del usuario de la sesión, no hay nada que proteger
    if (cuenta.getId().equals(this.sesionUsuario.getUsuario().getId()) == false) {
      return true;
    }
    
    // es su propia cuenta: debe conservar su rol y seguir habilitada
    // si no se eligió rol, otra regla lo reporta
    boolean conservaRol = (datos.rol() == null) || (cuenta.getRoles().size() == 1 && cuenta.tieneRol(datos.rol()));
    
    return conservaRol && (datos.habilitado() == cuenta.isHabilitado());
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
    
    String mensajeError = "No puedes cambiar tu propio rol ni deshabilitar tu propia cuenta. "
      + "Pídeselo a otro administrador.";
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
  }
}
