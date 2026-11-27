package co.edu.uptc.tiendalibros.negocio.seguridad.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la contraseña no supera la longitud máxima.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si la contraseña tiene más
/// caracteres que la longitud máxima.
public class ReglaContrasenaLongitudMaxima implements IReglaValidacion<char[]> {
  
  /// Cantidad máxima de caracteres que una contraseña debe tener.
  public static final int LONGITUD_MAXIMA = 64;
  
  /// Crea una nueva regla.
  public ReglaContrasenaLongitudMaxima() {
  }
  
  @Override
  public boolean cumple(char[] contrasena) {
    // cumple si la longitud de la contraseña no es mayor que la longitud máxima
    return contrasena.length <= ReglaContrasenaLongitudMaxima.LONGITUD_MAXIMA;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, char[] contrasena) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(contrasena) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String
      .format("La contraseña no puede tener más de %d caracteres.", ReglaContrasenaLongitudMaxima.LONGITUD_MAXIMA);
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("contrasena", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio de atributo
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("contrasena", mensajeError)
    );
  }
  
}