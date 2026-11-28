//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEmpleado;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;

/// Regla de validación para determinar que el empleado que se edita todavía existe en el
/// repositorio de datos.
///
/// Nota: un empleado nuevo sin ID cumple esta regla.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si el empleado ya no existe.
public class ReglaEmpleadoExiste implements IReglaValidacion<DatosEmpleado> {
  
  /// Repositorio de los empleados.
  private final IRepositorioEmpleado repositorio;
  
  /// Crea una nueva regla para verificar que el empleado existe.
  ///
  /// @param repositorio Repositorio para saber si el empleado existe.
  public ReglaEmpleadoExiste(IRepositorioEmpleado repositorio) {
    this.repositorio = repositorio;
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // un empleado nuevo (sin ID) no tiene nada previo que deba existir
    if (datos.id() == null) {
      return true;
    }
    
    return this.repositorio.buscarPorId(datos.id())
      .isPresent();
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
    
    String mensajeError = "El empleado ya no existe en el repositorio de datos.";
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
  }
}
