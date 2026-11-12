//
package co.edu.uptc.tiendalibros.negocio.libro.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioGeneralException;

/// Regla de validación para determinar si el libro se ofrece al menos en un formato:
/// impreso, digital o ambos.
///
/// Retorna una [ViolacionReglaNegocioGeneralException] si el libro no tiene ningún
/// formato, porque un libro sin formatos no se puede exhibir en el catálogo ni vender
/// al cliente.
public class ReglaLibroConFormatosAlMenosUnFormato implements IReglaValidacion<LibroConFormatos> {
  
  /// Crea una nueva regla de al menos un formato.
  public ReglaLibroConFormatosAlMenosUnFormato() {
  }
  
  @Override
  public boolean cumple(LibroConFormatos libroConFormatos) {
    // cumple si el libro se ofrece en el formato impreso, el digital, o ambos
    return libroConFormatos.tieneAlgunFormato();
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(
    ResultadoValidacion errores,
    LibroConFormatos libroConFormatos
  ) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(libroConFormatos) == true) {
      return Optional.empty();
    }
    
    // aquí, el elemento viola la regla
    String mensajeError = "El libro debe ofrecerse al menos en un formato: impreso o digital.";
    
    // primero, reportar el error (mensajeError)
    errores.agregarGeneral(mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioGeneralException(mensajeError)
    );
  }
  
}
