package co.edu.uptc.tiendalibros.negocio.seguridad.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si la contraseña no tiene espacios en blanco.
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si la contraseña tiene algún
/// espacio, tabulador o salto de línea.
public class ReglaContrasenaSinEspacios implements IReglaValidacion<char[]> {
  
  /// Crea una nueva regla.
  public ReglaContrasenaSinEspacios() {
  }
  
  @Override
  public boolean cumple(char[] contrasena) {
    // cumple si ningún caracter es un espacio en blanco
    for (char caracter : contrasena) {
      if (Character.isWhitespace(caracter)) {
        return false;
      }
    }
    return true;
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(ResultadoValidacion errores, char[] contrasena) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(contrasena) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = "La contraseña no puede tener espacios.";
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("contrasena", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio de atributo
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("contrasena", mensajeError)
    );
  }
  
}