package co.edu.uptc.tiendalibros.negocio.seguridad.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la contraseña tiene al menos un símbolo.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si la contraseña no tiene
/// ningún símbolo.
public class ReglaContrasenaConSimbolo implements IReglaValidacion<char[]> {
  
  /// Crea una nueva regla.
  public ReglaContrasenaConSimbolo() {
  }
  
  @Override
  public boolean cumple(char[] contrasena) {
    // cumple teniendo en cuenta que un símbolo es un carácter que no es ni letra, ni número,
    // ni
    // espacio en blanco
    for (char caracter : contrasena) {
      if ((Character.isLetterOrDigit(caracter) == false) && (Character.isWhitespace(caracter) == false)) {
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
    
    String mensajeError = "La contraseña debe tener al menos un símbolo (por ejemplo: @ # $ % !).";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("contrasena", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio de atributo
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("contrasena", mensajeError)
    );
  }
  
}