//
package co.edu.uptc.tiendalibros.modelo;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IElemento;

/// Representa un libro junto con sus dos formatos comerciales opcionales: impreso y
/// digital.
///
/// Es la unidad con la que trabaja el negocio cuando se registra o se edita un libro: la
/// obra intelectual ([Libro]) y los dos formatos que se venden ([FormatoLibro]).
public class LibroConFormatos implements IElemento {
  
  /// Libro como obra intelectual.
  private final Libro libro;
  
  /// Formato comercial impreso del libro.
  private final Optional<FormatoLibro> formatoImpreso;
  
  /// Formato comercial digital del libro.
  private final Optional<FormatoLibro> formatoDigital;
  
  /// Crea un libro nuevo sin datos y sin formatos.
  public LibroConFormatos() {
    this(
      new Libro(),
      Optional.empty(),
      Optional.empty()
    );
  }
  
  /// Crea un libro con sus formatos opcionales.
  ///
  /// @param libro          Libro como obra intelectual.
  /// @param formatoImpreso Formato comercial impreso del libro. Vacío si no se ofrece.
  /// @param formatoDigital Formato comercial digital del libro. Vacío si no se ofrece.
  /// @throws IllegalArgumentException Si algún parámetro es nulo o si algún formato no es
  ///                                  del tipo que le corresponde.
  public LibroConFormatos(
    Libro libro,
    Optional<FormatoLibro> formatoImpreso,
    Optional<FormatoLibro> formatoDigital
  ) {
    if (libro == null) {
      throw new IllegalArgumentException("El libro no puede ser nulo.");
    }
    LibroConFormatos.verificarFormato(formatoImpreso, TipoFormatoLibro.IMPRESO);
    LibroConFormatos.verificarFormato(formatoDigital, TipoFormatoLibro.DIGITAL);
    
    this.libro = libro;
    this.formatoImpreso = formatoImpreso;
    this.formatoDigital = formatoDigital;
  }
  
  /// Obtiene el ID del libro. ID es `null` si el libro aún no se ha guardado en el
  /// repositorio.
  ///
  /// @return ID del libro.
  @Override
  public Long getId() { return this.libro.getId(); }
  
  /// Obtiene el objeto libro como obra intelectual.
  ///
  /// @return Libro como obra intelectual.
  public Libro getLibro() { return this.libro; }
  
  /// Obtiene el formato comercial impreso del libro.
  ///
  /// @return Formato comercial impreso. Vacío si el libro no se ofrece en formato impreso.
  public Optional<FormatoLibro> getFormatoImpreso() { return this.formatoImpreso; }
  
  /// Obtiene el formato comercial digital del libro.
  ///
  /// @return Formato comercial digital. Vacío si el libro no se ofrece en formato digital.
  public Optional<FormatoLibro> getFormatoDigital() { return this.formatoDigital; }
  
  /// Determina si el libro se ofrece al menos en un formato.
  ///
  /// @return `true` si el libro tiene el formato impreso, el digital, o ambos.
  public boolean tieneAlgunFormato() {
    return (this.formatoImpreso.isPresent()) || (this.formatoDigital.isPresent());
  }
  
  /// Verifica que el formato del libro no sea nulo y que sea del tipo esperado.
  ///
  /// @param formato      Formato que se quiere verificar.
  /// @param tipoEsperado Tipo de formato que debe tener.
  /// @throws IllegalArgumentException Si el formato es nulo o no es del tipo esperado.
  private static void verificarFormato(Optional<FormatoLibro> formato, TipoFormatoLibro tipoEsperado) {
    if (formato == null) {
      throw new IllegalArgumentException(
        String.format("El formato %s no puede ser nulo.", tipoEsperado)
      );
    }
    if (formato.isPresent() && (formato.get()
      .getTipoFormato() != tipoEsperado)) {
      throw new IllegalArgumentException(
        String.format(
          "Se esperaba un formato '%s', pero el formato es '%s'.",
          tipoEsperado,
          formato.get()
            .getTipoFormato()
        )
      );
    }
  }
}
