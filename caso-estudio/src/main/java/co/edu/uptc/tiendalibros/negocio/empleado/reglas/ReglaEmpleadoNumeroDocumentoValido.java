//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el número de documento del empleado no es vacío y
/// no supera la longitud máxima.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si los datos violan la regla.
public class ReglaEmpleadoNumeroDocumentoValido implements IReglaValidacion<DatosEmpleado> {
  
  /// Longitud máxima del número de documento. Es la de la columna en la base de datos.
  private static final int LONGITUD_MAXIMA = 20;
  
  /// Crea una nueva regla de número de documento válido.
  public ReglaEmpleadoNumeroDocumentoValido() {
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // cumple si la longitud del número de documento no excede la longitud máxima
    String numero = datos.numeroDocumento();
    return (numero != null) && (numero.isBlank() == false) && (numero.length() <= LONGITUD_MAXIMA);
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, DatosEmpleado datos) {
    // si los datos cumplen la regla, devolver vacío
    if (cumple(datos) == true) {
      return Optional.empty();
    }
    // aquí, los datos violan la regla
    
    String mensajeError = String.format(
      "El número de documento no puede ser vacío ni tener más de %d caracteres.",
      LONGITUD_MAXIMA
    );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("numeroDocumento", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("numeroDocumento", mensajeError)
    );
  }
}
