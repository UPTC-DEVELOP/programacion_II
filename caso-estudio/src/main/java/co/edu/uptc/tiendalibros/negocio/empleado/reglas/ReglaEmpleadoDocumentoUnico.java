//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Objects;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEmpleado;
import co.edu.uptc.tiendalibros.modelo.Empleado;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el documento de identidad del empleado es único.
///
/// Dos empleados no pueden tener el mismo tipo y número de documento.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si los datos violan la regla.
public class ReglaEmpleadoDocumentoUnico implements IReglaValidacion<DatosEmpleado> {
  
  /// Repositorio de los empleados.
  private final IRepositorioEmpleado repositorio;
  
  /// Crea una nueva regla de documento de empleado único.
  ///
  /// @param repositorio Repositorio para saber si ya hay un empleado con ese documento.
  public ReglaEmpleadoDocumentoUnico(IRepositorioEmpleado repositorio) {
    this.repositorio = repositorio;
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // si falta el tipo o el número de documento no hay nada que comparar
    if ((datos.tipoDocumento() == null) || (datos.numeroDocumento() == null)) {
      return true;
    }
    
    Optional<Empleado> existente = this.repositorio
      .buscarPorDocumento(datos.tipoDocumento(), datos.numeroDocumento());
    
    // no hay otro empleado con ese documento
    if (existente.isEmpty()) {
      return true;
    }
    
    // hay un empleado con ese documento. Solo es válido si es el mismo empleado que se edita
    return Objects.equals(existente.get().getId(), datos.id());
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
    
    String mensajeError = String.format(
      "Ya existe otro empleado con el documento %s %s.",
      datos.tipoDocumento(), datos.numeroDocumento()
    );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("numeroDocumento", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("numeroDocumento", mensajeError)
    );
  }
}
