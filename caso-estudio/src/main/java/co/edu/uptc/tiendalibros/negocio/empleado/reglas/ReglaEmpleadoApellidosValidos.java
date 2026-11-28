//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si los apellidos del empleado no son vacíos y no
/// superan la longitud máxima.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si los datos violan la regla.
public class ReglaEmpleadoApellidosValidos implements IReglaValidacion<DatosEmpleado> {
  
  /// Longitud máxima de los apellidos, (según la columna en la base de datos.
  private static final int LONGITUD_MAXIMA = 100;
  
  /// Crea una nueva regla de apellidos válidos.
  public ReglaEmpleadoApellidosValidos() {
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // cumple si la longitud de los apellidos no execeden la longitud máxima
    String apellidos = datos.apellidos();
    return (apellidos != null) && (apellidos.isBlank() == false) && (apellidos.length() <= LONGITUD_MAXIMA);
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, DatosEmpleado datos) {
    // si los datos cumplen la regla, devolver vacío
    if (cumple(datos) == true) {
      return Optional.empty();
    }
    // aquí, los datos violan la regla
    
    String mensajeError = String.format(
      "Los apellidos no pueden ser vacíos ni tener más de %d caracteres.",
      LONGITUD_MAXIMA
    );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("apellidos", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("apellidos", mensajeError)
    );
  }
}
