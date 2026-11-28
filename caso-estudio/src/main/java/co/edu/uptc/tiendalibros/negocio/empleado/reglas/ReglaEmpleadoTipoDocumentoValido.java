//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el tipo de documento del empleado es válido: CC o
/// CE.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si los datos violan la regla.
public class ReglaEmpleadoTipoDocumentoValido implements IReglaValidacion<DatosEmpleado> {
  
  /// Crea una nueva regla de tipo de documento válido.
  public ReglaEmpleadoTipoDocumentoValido() {
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // cumple si el tipo de documento tiene el valor correcto
    String tipo = datos.tipoDocumento();
    return (tipo != null) && (tipo.equals("CC") || tipo.equals("CE"));
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, DatosEmpleado datos) {
    // si los datos cumplen la regla, devolver vacío
    if (cumple(datos) == true) {
      return Optional.empty();
    }
    // aquí, los datos violan la regla
    
    String mensajeError = "El tipo de documento debe ser CC o CE.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("tipoDocumento", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("tipoDocumento", mensajeError)
    );
  }
}
