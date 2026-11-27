package co.edu.uptc.tiendalibros.negocio.seguridad.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la contraseña tiene la longitud mínima.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si la contraseña tiene menos
/// caracteres que la longitud mínima.
public class ReglaContrasenaLongitudMinima implements IReglaValidacion<char[]> {
  
  /// Cantidad mínima de caracteres que una contraseña debe tener.
  public static final int LONGITUD_MINIMA = 8;
  
  /// Crea una nueva regla.
  public ReglaContrasenaLongitudMinima() {
  }
  
  @Override
  public boolean cumple(char[] contrasena) {
    // cumple si la longitud de la contraseña es mayor o igual que la longitud mínima
    return contrasena.length >= LONGITUD_MINIMA;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, char[] contrasena) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(contrasena) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format("La contraseña debe tener al menos %d caracteres.", LONGITUD_MINIMA);
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("contrasena", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("contrasena", mensajeError)
    );
  }
  
}