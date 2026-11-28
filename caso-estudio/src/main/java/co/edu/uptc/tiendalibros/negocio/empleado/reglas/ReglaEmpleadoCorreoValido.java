//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;
import java.util.regex.Pattern;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el correo electrónico del empleado no es vacío,
/// no supera la longitud máxima y tiene la forma tradicional `usuario@dominio.extensión`.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si los datos violan la regla.
public class ReglaEmpleadoCorreoValido implements IReglaValidacion<DatosEmpleado> {
  
  /// Longitud máxima del correo electrónico, según la columna en la base de datos.
  private static final int LONGITUD_MAXIMA = 120;
  
  /// Forma básica de un correo: texto, arroba, texto, punto y texto, sin espacios.
  private static final Pattern FORMATO_CORREO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
  
  /// Crea una nueva regla de correo electrónico válido.
  public ReglaEmpleadoCorreoValido() {
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // cumple si el valor del correo tiene el formato de correo
    String correo = datos.correoElectronico();
    return (correo != null)
      && (correo.isBlank() == false)
      && (correo.length() <= LONGITUD_MAXIMA)
      && FORMATO_CORREO.matcher(correo)
        .matches();
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, DatosEmpleado datos) {
    // si los datos cumplen la regla, devolver vacío
    if (cumple(datos) == true) {
      return Optional.empty();
    }
    // aquí, los datos violan la regla
    
    String mensajeError = String.format(
      "Escribe un correo electrónico válido, como usuario@dominio.co, de máximo %d caracteres.",
      LONGITUD_MAXIMA
    );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("correoElectronico", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("correoElectronico", mensajeError)
    );
  }
}
