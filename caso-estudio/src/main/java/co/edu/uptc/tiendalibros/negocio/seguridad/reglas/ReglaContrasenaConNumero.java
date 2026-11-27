package co.edu.uptc.tiendalibros.negocio.seguridad.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la contraseña tiene al menos un número.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si la contraseña no tiene
/// ningún dígito.
public class ReglaContrasenaConNumero implements IReglaValidacion<char[]> {
  
  /// Crea una nueva regla.
  public ReglaContrasenaConNumero() {
  }
  
  @Override
  public boolean cumple(char[] contrasena) {
    // cumple si la contraseña tiene al menos un caracter es un dígito numérico
    for (char caracter : contrasena) {
      if (Character.isDigit(caracter)) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, char[] contrasena) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(contrasena) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "La contraseña debe tener al menos un número.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("contrasena", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio de atributo
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("contrasena", mensajeError)
    );
  }
  
}