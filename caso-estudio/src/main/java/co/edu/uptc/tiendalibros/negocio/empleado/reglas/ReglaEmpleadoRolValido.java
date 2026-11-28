//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el rol del empleado fue elegido y es un rol de
/// empleado: gestor de catálogo, gestor de ventas o administrador.
/// 
/// Es importante porque no debemos permitir que un empleado tenga el rol cliente.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si los datos violan la regla.
public class ReglaEmpleadoRolValido implements IReglaValidacion<DatosEmpleado> {
  
  /// Crea una nueva regla de rol de empleado válido.
  public ReglaEmpleadoRolValido() {
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // cumple si el rol es o gestor de catálogo, o gestor de ventas o administrador
    Rol rol = datos.rol();
    return (rol == Rol.GESTOR_CATALOGO) || (rol == Rol.GESTOR_VENTAS) || (rol == Rol.ADMINISTRADOR);
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, DatosEmpleado datos) {
    // si los datos cumplen la regla, devolver vacío
    if (cumple(datos) == true) {
      return Optional.empty();
    }
    // aquí, los datos violan la regla
    
    String mensajeError = "Elige el rol del empleado: gestor de catálogo, gestor de ventas o administrador.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("rol", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("rol", mensajeError)
    );
  }
}
